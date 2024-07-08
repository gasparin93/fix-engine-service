package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.model.validation.Validator;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MessageTypeValidator implements Validator {

  //tag, isSupported
  private static final Map<String, Boolean> acceptedValues = Map.<String, Boolean>ofEntries(
      Map.entry("0", false),  //Heartbeat <0>
      Map.entry("1", false),  //Test Request <1>
      Map.entry("2", false),  //Resend Request <2>
      Map.entry("3", false),  //Reject <3>
      Map.entry("4", false),  //Sequence Reset <4>
      Map.entry("5", false),  //Logout <5>
      Map.entry("6", false),  //Indication of Interest <6>
      Map.entry("7", false),  //Advertisement <7>
      Map.entry("8", false),  //Execution Report <8>
      Map.entry("9", false),  //Order Cancel Reject <9>
      Map.entry("A", false),  //Logon <A>
      Map.entry("B", false),  //News <B>
      Map.entry("C", false),  //Email <C>
      Map.entry("D", true),   //New Order Single <D>
      Map.entry("E", false),  //New Order List <E>
      Map.entry("F", false),  //Order Cancel Request <F>
      Map.entry("G", false),  //Order Cancel/Replace Request <G>
      Map.entry("H", false),  //Order Status Request <H>
      Map.entry("J", false),  //Allocation Instruction <J>
      Map.entry("K", false),  //List Cancel Request <K>
      Map.entry("L", false),  //List Execute <L>
      Map.entry("M", false),  //List Status Request <M>
      Map.entry("N", false),  //List Status <N>
      Map.entry("P", false),  //Allocation Instruction Ack <P>
      Map.entry("Q", false),  //Don't Know Trade <Q> (DK)
      Map.entry("R", false),  //Quote Request <R>
      Map.entry("S", false),  //Quote <S>
      Map.entry("T", false),  //Settlement Instructions <T>
      Map.entry("V", false),  //Market Data Request <V>
      Map.entry("W", false),  //Market Data-Snapshot/Full Refresh <W>
      Map.entry("X", false),  //Market Data-Incremental Refresh <X>
      Map.entry("Y", false),  //Market Data Request Reject <Y>
      Map.entry("Z", false),  //Quote Cancel <Z>
      Map.entry("a", false),  //Quote Status Request <a>
      Map.entry("b", false),  //Mass Quote Acknowledgement <b>
      Map.entry("c", false),  //Security Definition Request <c>
      Map.entry("d", false),  //Security Definition <d>
      Map.entry("e", false),  //Security Status Request <e>
      Map.entry("f", false),  //Security Status <f>
      Map.entry("g", false),  //Trading Session Status Request <g>
      Map.entry("h", false),  //Trading Session Status <h>
      Map.entry("i", false),  //Mass Quote <i>
      Map.entry("j", false),  //Business Message Reject <j>
      Map.entry("k", false),  //Bid Request <k>
      Map.entry("l", false),  //Bid Response <l> (lowercase L)
      Map.entry("m", false),  //List Strike Price <m>
      Map.entry("n", false),  //XML message <n> (e.g. non-FIX MsgType)
      Map.entry("o", false),  //Registration Instructions <o>
      Map.entry("p", false),  //Registration Instructions Response <p>
      Map.entry("q", false),  //Order Mass Cancel Request <q>
      Map.entry("r", false),  //Order Mass Cancel Report <r>
      Map.entry("s", false),  //New Order Cross <s>
      Map.entry("t", false),  //Cross Order Cancel/Replace Request <t> (a.k.a. Cross Order Modification Request)
      Map.entry("u", false),  //Cross Order Cancel Request <u>
      Map.entry("v", false),  //Security Type Request <v>
      Map.entry("w", false),  //Security Types <w>
      Map.entry("x", false),  //Security List Request <x>
      Map.entry("y", false),  //Security List <y>
      Map.entry("z", false),  //Derivative Security List Request <z>
      Map.entry("AA", false), //Derivative Security List <AA>
      Map.entry("AB", false), //New Order Multileg <AB>
      Map.entry("AC", false), //Multileg Order Cancel/Replace <AC> (a.k.a. Multileg Order Modification Request)
      Map.entry("AD", false), //Trade Capture Report Request <AD>
      Map.entry("AE", false), //Trade Capture Report <AE>
      Map.entry("AF", false), //Order Mass Status Request <AF>
      Map.entry("AG", false), //Quote Request Reject <AG>
      Map.entry("AH", false), //RFQ Request <AH>
      Map.entry("AI", false), //Quote Status Report <AI>
      Map.entry("AJ", false), //Quote Response <AJ>
      Map.entry("AK", false), //Confirmation <AK>
      Map.entry("AL", false), //Position Maintenance Request <AL>
      Map.entry("AM", false), //Position Maintenance Report <AM>
      Map.entry("AN", false), //Request For Positions <AN>
      Map.entry("AO", false), //Request For Positions Ack <AO>
      Map.entry("AP", false), //Position Report <AP>
      Map.entry("AQ", false), //Trade Capture Report Request Ack <AQ>
      Map.entry("AR", false), //Trade Capture Report Ack <AR>
      Map.entry("AS", false), //Allocation Report <AS> (aka Allocation Claim)
      Map.entry("AT", false), //Allocation Report Ack <AT> (aka Allocation Claim Ack)
      Map.entry("AU", false), //Confirmation Ack <AU> (aka Affirmation)
      Map.entry("AV", false), //Settlement Instruction Request <AV>
      Map.entry("AW", false), //Assignment Report <AW>
      Map.entry("AX", false), //Collateral Request <AX>
      Map.entry("AY", false), //Collateral Assignment <AY>
      Map.entry("AZ", false), //Collateral Response <AZ>
      Map.entry("BA", false), //Collateral Report <BA>
      Map.entry("BB", false), //Collateral Inquiry <BB>
      Map.entry("BC", false), //Network (Counterparty System) Status Request <BC>
      Map.entry("BD", false), //Network (Counterparty System) Status Response <BD>
      Map.entry("BE", false), //User Request <BE>
      Map.entry("BF", false), //User Response <BF>
      Map.entry("BG", false), //Collateral Inquiry Ack <BG>
      Map.entry("BH", false)  //Confirmation Request <BH>
  );

  @Override
  public ValidationError validate(RawTag rawTag, Map<String, RawTag> context) {
    return Optional.ofNullable(acceptedValues.get(rawTag.value()))
        .map(isSupported -> isSupported ? ValidationError.empty()
            : ValidationError.builder().submittedTag(rawTag).error("Message Type is not currently supported!")
                .build())
        .orElse(ValidationError.builder().critical(true).submittedTag(rawTag)
            .error(rawTag.tag() == null ? REQUIRED_ERROR_MSG : "Message type is not valid!")
                .build());
  }

  @Override
  public String supports() {
    return "35";
  }

}
