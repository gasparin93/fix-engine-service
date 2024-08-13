package com.honestefforts.fixengine.service.converter;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixConverterFactory {
  private final Map<MessageType, FixConverter<?>> converterMap;

  @Autowired
  public FixConverterFactory(List<FixConverter<?>> converters) {
    converterMap = converters.stream()
        .collect(Collectors.toMap(FixConverter::supports, Function.identity()));
  }

  public FixMessage create(@NonNull final FixMessageContext context) {
      return Optional.ofNullable(converterMap.get(context.messageType()))
          .map(converter -> converter.convert(context))
          .orElse(null);
    }
}