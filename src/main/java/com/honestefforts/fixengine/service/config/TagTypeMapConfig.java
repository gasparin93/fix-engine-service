package com.honestefforts.fixengine.service.config;

import com.honestefforts.fixengine.model.validation.TagType;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "tag")
public class TagTypeMapConfig {
  private final Map<String, TagType> typeMap;

  public TagType getTypeOfTag(String tag) {
    //TODO: there are a lot of STRING types, consider removing those and doing getOrDefault(STRING)
    //for the time being, it's useful to keep track of supported tags
    return typeMap.get(tag);
  }
}
