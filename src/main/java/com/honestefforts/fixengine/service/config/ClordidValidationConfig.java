package com.honestefforts.fixengine.service.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "validation.clordid")
public class ClordidValidationConfig {
  @NonNull
  private double falsePositiveProbability;
  @NonNull
  private int expectedInsertions;
}
