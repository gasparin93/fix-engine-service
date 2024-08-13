package com.honestefforts.fixengine.service.service;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.validation.RequiredComponentValidation.validateRequiredComponentsForMessageType;

import com.honestefforts.fixengine.model.endpoint.response.FixMessageResponseV1;
import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.util.PredicateUtil;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.config.TagTypeMapConfig;
import com.honestefforts.fixengine.service.converter.FixConverterFactory;
import com.honestefforts.fixengine.service.validation.FixValidatorFactory;
import com.honestefforts.fixengine.service.validation.header.MessageTypeValidator;
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

    return Optional.ofNullable(parseMessageToContext(message.split(delimiter), version, validationErrors))
        .map(context -> FixMessageResponseV1.builder()
            .response(validateAndTransformTags(context, validationErrors))
            .errors(validationErrors)
            .build())
        .orElse(null);
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
      return null;
    }
    try {
      return fixConverterFactory.create(context);
    } catch(Exception e) {
      //TODO: have proper logging and eventually an anomaly alert system for things like this
      //TODO: revisit this, I think current validation rails will account for this
      System.err.println("WARNING: VALIDATION DID NOT CATCH THE NULL VALUE HERE:");
      e.printStackTrace();
      return null;
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
            RawTag.builder().position(index+1).tag(tag).value(keyValue[1])
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

    ValidationError messageTypeValidation = MessageTypeValidator.validate(map.get(35));
    if(!messageTypeValidation.isEmpty()) {
      badlyFormattedTags.add(messageTypeValidation);
      return null;
    }

    return FixMessageContext.builder()
        .processedMessages(map)
        .messageType(MessageType.getMessageType(map.get(35).value()))
        .messageLength(keyValPair.length)
        .version(version)
        .build();
  }

}
