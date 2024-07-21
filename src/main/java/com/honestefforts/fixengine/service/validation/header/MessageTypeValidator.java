package com.honestefforts.fixengine.service.validation.header;

import ch.qos.logback.core.util.StringUtil;
import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for tag 35 (MsgType)
 * <br>
 * Will use SpringBoot injection to determine what converters are available and hence supported
 * by the application.
 */
@Component
public class MessageTypeValidator implements FixValidator {

  private final Set<String> supportedMsgTypes;

  @Autowired
  private MessageTypeValidator(List<FixConverter<?>> fixConverters) {
    this.supportedMsgTypes = fixConverters.stream()
        .map(FixConverter::supports)
        .collect(Collectors.toSet());
  }

  //if private type (U*) support is added, must be added here also
  private static final Set<String> acceptedValues = Set.of(
      "0",  //Heartbeat <0>
      "1",  //Test Request <1>
      "2",  //Resend Request <2>
      "3",  //Reject <3>
      "4",  //Sequence Reset <4>
      "5",  //Logout <5>
      "6",  //Indication of Interest <6>
      "7",  //Advertisement <7>
      "8",  //Execution Report <8>
      "9",  //Order Cancel Reject <9>
      "A",  //Logon <A>
      "B",  //News <B>
      "C",  //Email <C>
      "D",   //New Order Single <D>
      "E",  //New Order List <E>
      "F",  //Order Cancel Request <F>
      "G",  //Order Cancel/Replace Request <G>
      "H",  //Order Status Request <H>
      "J",  //Allocation Instruction <J>
      "K",  //List Cancel Request <K>
      "L",  //List Execute <L>
      "M",  //List Status Request <M>
      "N",  //List Status <N>
      "P",  //Allocation Instruction Ack <P>
      "Q",  //Don't Know Trade <Q> (DK)
      "R",  //Quote Request <R>
      "S",  //Quote <S>
      "T",  //Settlement Instructions <T>
      "V",  //Market Data Request <V>
      "W",  //Market Data-Snapshot/Full Refresh <W>
      "X",  //Market Data-Incremental Refresh <X>
      "Y",  //Market Data Request Reject <Y>
      "Z",  //Quote Cancel <Z>
      "a",  //Quote Status Request <a>
      "b",  //Mass Quote Acknowledgement <b>
      "c",  //Security Definition Request <c>
      "d",  //Security Definition <d>
      "e",  //Security Status Request <e>
      "f",  //Security Status <f>
      "g",  //Trading Session Status Request <g>
      "h",  //Trading Session Status <h>
      "i",  //Mass Quote <i>
      "j",  //Business Message Reject <j>
      "k",  //Bid Request <k>
      "l",  //Bid Response <l> (lowercase L)
      "m",  //List Strike Price <m>
      "n",  //XML message <n> (e.g. non-FIX MsgType)
      "o",  //Registration Instructions <o>
      "p",  //Registration Instructions Response <p>
      "q",  //Order Mass Cancel Request <q>
      "r",  //Order Mass Cancel Report <r>
      "s",  //New Order Cross <s>
      "t",  //Cross Order Cancel/Replace Request <t> (a.k.a. Cross Order Modification Request)
      "u",  //Cross Order Cancel Request <u>
      "v",  //Security Type Request <v>
      "w",  //Security Types <w>
      "x",  //Security List Request <x>
      "y",  //Security List <y>
      "z",  //Derivative Security List Request <z>
      "AA", //Derivative Security List <AA>
      "AB", //New Order Multileg <AB>
      "AC", //Multileg Order Cancel/Replace <AC> (a.k.a. Multileg Order Modification Request)
      "AD", //Trade Capture Report Request <AD>
      "AE", //Trade Capture Report <AE>
      "AF", //Order Mass Status Request <AF>
      "AG", //Quote Request Reject <AG>
      "AH", //RFQ Request <AH>
      "AI", //Quote Status Report <AI>
      "AJ", //Quote Response <AJ>
      "AK", //Confirmation <AK>
      "AL", //Position Maintenance Request <AL>
      "AM", //Position Maintenance Report <AM>
      "AN", //Request For Positions <AN>
      "AO", //Request For Positions Ack <AO>
      "AP", //Position Report <AP>
      "AQ", //Trade Capture Report Request Ack <AQ>
      "AR", //Trade Capture Report Ack <AR>
      "AS", //Allocation Report <AS> (aka Allocation Claim)
      "AT", //Allocation Report Ack <AT> (aka Allocation Claim Ack)
      "AU", //Confirmation Ack <AU> (aka Affirmation)
      "AV", //Settlement Instruction Request <AV>
      "AW", //Assignment Report <AW>
      "AX", //Collateral Request <AX>
      "AY", //Collateral Assignment <AY>
      "AZ", //Collateral Response <AZ>
      "BA", //Collateral Report <BA>
      "BB", //Collateral Inquiry <BB>
      "BC", //Network (Counterparty System) Status Request <BC>
      "BD", //Network (Counterparty System) Status Response <BD>
      "BE", //User Request <BE>
      "BF", //User Response <BF>
      "BG", //Collateral Inquiry Ack <BG>
      "BH"  //Confirmation Request <BH>
  );

  @Override
  public ValidationError validate(RawTag rawTag, Map<String, RawTag> context) {
    ValidationError.ValidationErrorBuilder validationErrorBuilder = ValidationError.builder()
        .critical(true).submittedTag(rawTag);
    if(StringUtil.isNullOrEmpty(rawTag.value())) {
      return validationErrorBuilder.error(REQUIRED_ERROR_MSG).build();
    }
    if(rawTag.position() != 3) {
      return validationErrorBuilder
          .error("MsgType (35) tag must be the third tag in the message!").build();
    }
    if(!acceptedValues.contains(rawTag.value())) {
      if(isCustomType(rawTag)) {
        return validationErrorBuilder.error("Unknown private message format!").build();
      }
      return validationErrorBuilder.error("Message type is invalid!").build();
    }
    if(!supportedMsgTypes.contains(rawTag.value())) {
      return validationErrorBuilder.error("Message Type is not currently supported!").build();
    }
    return ValidationError.empty();
  }

  private boolean isCustomType(RawTag rawTag) {
    return rawTag.value().charAt(0) == 'U';
  }

  @Override
  public String supports() {
    return "35";
  }

}
