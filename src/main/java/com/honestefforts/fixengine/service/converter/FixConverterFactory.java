package com.honestefforts.fixengine.service.converter;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixConverterFactory {
  private static final Map<String, FixConverter<?>> converterMap = new HashMap<>();

  @Autowired
  private FixConverterFactory(List<FixConverter<?>> converters) {
    converters.forEach(converter -> converterMap.put(converter.supports(), converter));
  }

  public FixMessage create(@NonNull final Map<String, RawTag> tagMap) {
      return Optional.ofNullable(converterMap.get(tagMap.get("35").tag()))
          .map(converter -> converter.convert(tagMap))
          .orElse(null);
    }
}