package com.honestefforts.fixengine.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation.clordid")
public class ClordidValidationConfig {
  private final double falsePositiveProbability;
  private final int expectedInsertions;

  public ClordidValidationConfig(double falsePositiveProbability, int expectedInsertions) {
    this.falsePositiveProbability = falsePositiveProbability;
    this.expectedInsertions = expectedInsertions;
  }

  public double getFalsePositiveProbability() {
    return falsePositiveProbability;
  }

  public int getExpectedInsertions() {
    return expectedInsertions;
  }
}
