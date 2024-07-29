package com.honestefforts.fixengine.service.config;

import com.honestefforts.fixengine.model.message.tags.TagType;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "tag")
public class TagTypeMapConfig {
  private final Map<String, TagType> typeMap;

  public TagType getTypeOfTag(String tag) {
    return typeMap.getOrDefault(tag, TagType.STRING);
  }
}
