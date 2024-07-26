package com.honestefforts.fixengine.service.converter.messagetypes;

import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.FixMessageContext;

public class BusinessMessageRejectConverter {
  public static FixMessage convert(FixMessageContext context) {
    return null;
  }
  public static FixMessage generate(String errorMessage) {
    System.out.println("REJECTED!");
    return null;
  };
}