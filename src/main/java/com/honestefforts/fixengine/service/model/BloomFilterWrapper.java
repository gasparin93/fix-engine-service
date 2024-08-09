package com.honestefforts.fixengine.service.model;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.honestefforts.fixengine.service.config.ClordidValidationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class BloomFilterWrapper {

  private final ClordidValidationConfig clordidValidationConfig;
  private BloomFilter<String> bloomFilter;

  @Autowired
  public BloomFilterWrapper(ClordidValidationConfig clordidValidationConfig) {
    this.clordidValidationConfig = clordidValidationConfig;
    this.bloomFilter = createBloomFilter();
  }

  private BloomFilter<String> createBloomFilter() {
    return BloomFilter.create(Funnels.unencodedCharsFunnel(),
        clordidValidationConfig.getExpectedInsertions(),
        clordidValidationConfig.getFalsePositiveProbability());
  }

  public void put(String item) {
    bloomFilter.put(item);
  }

  public boolean mightContain(String item) {
    return bloomFilter.mightContain(item);
  }

  public void reset() {
    this.bloomFilter = createBloomFilter();
  }

}