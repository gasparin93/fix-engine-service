package com.honestefforts.fixengine.service.converter;

import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class TestUtility {

  /**
   * Get a simple RawTag where the only important things are tag and value
   */
  public static RawTag getRawTag(Integer tag, String value) {
    return RawTag.builder().tag(tag).value(value).dataType(STRING).build();
  }

  public static Map.Entry<Integer, RawTag> getRawTagEntry(Integer tag, String value) {
    return Map.entry(tag, getRawTag(tag, value));
  }

}
