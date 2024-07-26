package com.honestefforts.fixengine.service.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "validation.clordid")
public class ClordidValidationConfig {
  @NonNull
  private final double falsePositiveProbability;
  @NonNull
  private final int expectedInsertions;

}
