package com.honestefforts.fixengine.service.service;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.validation.RequiredComponentValidation.validateRequiredComponentsForMessageType;

import com.honestefforts.fixengine.model.endpoint.request.FixMessageRequestV1;
import com.honestefforts.fixengine.model.endpoint.response.FixMessageResponseV1;
import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.util.PredicateUtil;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.config.TagTypeMapConfig;
import com.honestefforts.fixengine.service.converter.FixConverterFactory;
import com.honestefforts.fixengine.service.converter.messagetypes.BusinessMessageRejectConverter;
import com.honestefforts.fixengine.service.validation.FixValidatorFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO: handle resiliency, have more standardized logging for errors
@RequiredArgsConstructor
@Service
public class FixEngineService {

  @Autowired
  private final TagTypeMapConfig tagTypeMapConfig;
  @Autowired
  private final FixValidatorFactory fixValidatorFactory;
  @Autowired
  private final FixConverterFactory fixConverterFactory;

  public FixMessageResponseV1 processTags(@NonNull final String message,
      @NonNull final String delimiter, @NonNull final String version) {
    ConcurrentLinkedQueue<ValidationError> validationErrors = new ConcurrentLinkedQueue<>();
    FixMessageContext context = parseMessageToContext(message.split(delimiter), version,
        validationErrors);

    return FixMessageResponseV1.builder()
        .response(validateAndTransformTags(context, validationErrors))
        .errors(validationErrors)
        .build();
  }

  public FixMessage validateAndTransformTags(FixMessageContext context,
      ConcurrentLinkedQueue<ValidationError> validationErrors) {
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    boolean hasCriticalErrors = context.processedMessages().values().stream()
        .onClose(executor::shutdown)
        .map(tag -> executor.submit(() ->
            Optional.of(fixValidatorFactory.validateTag(tag, context))
                .filter(ValidationError::hasErrors)
                .map(validationError -> {
                  validationErrors.add(validationError);
                  context.processedMessages().remove(tag.tag());
                   return validationError.isCritical();
                })
                .orElse(false))
        )
        .map(future -> {
          try {
            return future.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
          }
        })
        .anyMatch(PredicateUtil.isTrue);

    List<ValidationError> requiredComponentErrors = validateRequiredComponentsForMessageType(context);
    validationErrors.addAll(requiredComponentErrors);

    if(!requiredComponentErrors.isEmpty()) {
      hasCriticalErrors = true;
    }
    if (hasCriticalErrors) {
      return BusinessMessageRejectConverter.generate("FIX message includes critical errors");
    }
    try {
      return fixConverterFactory.create(context);
    } catch(NullPointerException e) {
      //TODO: have proper logging and eventually an anomaly alert system for things like this
      //TODO: revisit this, I think current validation rails will account for this
      System.err.println("WARNING: VALIDATION DID NOT CATCH THE NULL VALUE HERE:");
      e.printStackTrace();
      return BusinessMessageRejectConverter.generate("Invalid input " + e.getMessage());
    }
  }

  public FixMessageContext parseMessageToContext(final String[] keyValPair,
      final String version, ConcurrentLinkedQueue<ValidationError> badlyFormattedTags) {
    Map<Integer, RawTag> map = new ConcurrentHashMap<>();
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    IntStream.range(0, keyValPair.length).mapToObj(index -> executor.submit(() -> {
      String pair = keyValPair[index];
      String[] keyValue = pair.split("=");
      Integer tag = keyValue.length == 2 ? parseInt(keyValue[0]) : null;
      if (tag != null) {
        map.put(tag,
            RawTag.builder().position(index+1).tag(tag).value(keyValue[1]).version(version)
                .dataType(tagTypeMapConfig.getTypeOfTag(keyValue[0]))
                .build());
      } else {
        badlyFormattedTags.add(
            ValidationError.builder().error("Unknown tag: " + pair + "!").build());
      }
      return null;
    })).forEach(future -> {
      try {
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });

    executor.shutdown();

    return FixMessageContext.builder()
        .processedMessages(map)
        .messageType(Optional.ofNullable(map.get(35)).map(RawTag::value).orElse(null))
        .messageLength(keyValPair.length)
        .build();
  }

  public static void main(String[] args) {
    FixMessageRequestV1 request = FixMessageRequestV1.builder().fixMessages(List.of(
            "8=FIX.4.4|9=176|35=D|34=2|49=CLIENT12|52=20230628-12:30:05|56=BROKER12|11=13579|21=1|55=AAPL|54=1|60=20230628-12:30:05|38=100|40=2|44=150.50|10=157|",
            "8=FIX.4.4|9=178|35=D|34=3|49=CLIENT12|52=20230628-12:30:07|56=BROKER12|11=13580|21=1|55=GOOG|54=2|60=20230628-12:30:07|38=200|40=2|44=2500.00|10=158|",
            "8=FIX.4.4|9=175|35=D|34=4|49=CLIENT12|52=20230628-12:30:09|56=BROKER12|11=13581|21=1|55=MSFT|54=1|60=20230628-12:30:09|38=150|40=2|44=300.25|10=159|",
            "8=FIX.4.4|9=174|35=D|34=5|49=CLIENT12|52=20230628-12:30:11|56=BROKER12|11=13582|21=1|55=TSLA|54=2|60=20230628-12:30:11|38=50|40=2|44=650.75|10=160|",
            "8=FIX.4.4|9=176|35=D|34=6|49=CLIENT12|52=20230628-12:30:13|56=BROKER12|11=13583|21=1|55=AMZN|54=1|60=20230628-12:30:13|38=30|40=2|44=3450.50|10=161|",
            "8=FIX.4.4|9=175|35=D|34=7|49=CLIENT12|52=20230628-12:30:15|56=BROKER12|11=13584|21=1|55=FB|54=2|60=20230628-12:30:15|38=70|40=2|44=325.75|10=162|",
            "8=FIX.4.4|9=174|35=D|34=8|49=CLIENT12|52=20230628-12:30:17|56=BROKER12|11=13585|21=1|55=NFLX|54=1|60=20230628-12:30:17|38=120|40=2|44=510.00|10=163|",
            "8=FIX.4.4|9=176|35=D|34=9|49=CLIENT12|52=20230628-12:30:19|56=BROKER12|11=13586|21=1|55=NVDA|54=2|60=20230628-12:30:19|38=85|40=2|44=650.50|10=164|",
            "8=FIX.4.4|9=178|35=D|34=10|49=CLIENT12|52=20230628-12:30:21|56=BROKER12|11=13587|21=1|55=INTC|54=1|60=20230628-12:30:21|38=100|40=2|44=55.25|10=165|",
            "8=FIX.4.4|9=177|35=D|34=11|49=CLIENT12|52=20230628-12:30:23|56=BROKER12|11=13588|21=1|55=AMD|54=2|60=20230628-12:30:23|38=65|40=2|44=95.75|10=166|",
            "8=FIX.4.4|9=179|35=D|34=12|49=CLIENT12|52=20230628-12:30:25|56=BROKER12|11=13589|21=1|55=BA|54=1|60=20230628-12:30:25|38=40|40=2|44=225.00|10=167|",
            "8=FIX.4.4|9=178|35=D|34=13|49=CLIENT12|52=20230628-12:30:27|56=BROKER12|11=13590|21=1|55=BABA|54=2|60=20230628-12:30:27|38=90|40=2|44=180.50|10=168|",
            "8=FIX.4.4|9=176|35=D|34=14|49=CLIENT12|52=20230628-12:30:29|56=BROKER12|11=13591|21=1|55=UBER|54=1|60=20230628-12:30:29|38=110|40=2|44=45.75|10=169|",
            "8=FIX.4.4|9=177|35=D|34=15|49=CLIENT12|52=20230628-12:30:31|56=BROKER12|11=13592|21=1|55=LYFT|54=2|60=20230628-12:30:31|38=95|40=2|44=60.25|10=170|",
            "8=FIX.4.4|9=175|35=D|34=16|49=CLIENT12|52=20230628-12:30:33|56=BROKER12|11=13593|21=1|55=TWTR|54=1|60=20230628-12:30:33|38=75|40=2|44=75.00|10=171|",
            "8=FIX.4.4|9=178|35=D|34=17|49=CLIENT12|52=20230628-12:30:35|56=BROKER12|11=13594|21=1|55=SNAP|54=2|60=20230628-12:30:35|38=65|40=2|44=18.50|10=172|",
            "8=FIX.4.4|9=175|35=D|34=18|49=CLIENT12|52=20230628-12:30:37|56=BROKER12|11=13595|21=1|55=PINS|54=1|60=20230628-12:30:37|38=80|40=2|44=25.25|10=173|",
            "8=FIX.4.4|9=176|35=D|34=19|49=CLIENT12|52=20230628-12:30:39|56=BROKER12|11=13596|21=1|55=ZM|54=2|60=20230628-12:30:39|38=55|40=2|44=400.75|10=174|",
            "8=FIX.4.4|9=177|35=D|34=20|49=CLIENT12|52=20230628-12:30:41|56=BROKER12|11=13597|21=1|55=SQ|54=1|60=20230628-12:30:41|38=45|40=2|44=220.50|10=175|",
            "8=FIX.4.4|9=178|35=D|34=21|49=CLIENT12|52=20230628-12:30:43|56=BROKER12|11=13598|21=1|55=PYPL|54=2|60=20230628-12:30:43|38=60|40=2|44=270.25|10=176|"))
        .build();
    /*process(request)
        .forEach(System.out::println);*/
    ;
  }
}
