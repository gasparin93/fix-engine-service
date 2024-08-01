package com.honestefforts.fixengine.service.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.NewOrderSingle;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FixConverterFactoryTest {

  @Mock
  private FixConverter<NewOrderSingle> newOrderSingleConverter;

  private FixConverterFactory fixConverterFactory;

  @BeforeEach
  void setUp() {
    when(newOrderSingleConverter.supports()).thenReturn("D");
    fixConverterFactory = new FixConverterFactory(List.of(newOrderSingleConverter));
  }

  @Test
  void create_happyPath() {
    NewOrderSingle fixMessage = mock(NewOrderSingle.class);
    when(newOrderSingleConverter.convert(any())).thenReturn(fixMessage);
    assertThat(fixConverterFactory.create(FixMessageContext.builder().messageType("D").build()))
        .isEqualTo(fixMessage);
  }

  @Test
  void create_null_expectException() {
    assertThatThrownBy(() -> fixConverterFactory.create(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void create_nullMessageType_expectNull() {
    assertThat(fixConverterFactory.create(FixMessageContext.builder().build()))
        .isNull();
  }

  @Test
  void create_unsupportedMessageType_expectNull() {
    assertThat(fixConverterFactory.create(FixMessageContext.builder().messageType("9999").build()))
        .isNull();
  }

}