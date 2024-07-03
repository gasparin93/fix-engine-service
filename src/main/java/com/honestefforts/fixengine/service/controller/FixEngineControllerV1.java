package com.honestefforts.fixengine.service.controller;

import com.honestefforts.fixengine.service.validation.BeginStringValidator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"})
public class FixEngineControllerV1 {

  @PostMapping(value = "/processFixMessages")
  public List<FixMessageResponseV1> processFixMessages(@NonNull FixMessageRequestV1 request) {
    if (BeginStringValidator.isVersionNotSupported(request.getVersion())) {
      return getIncorrectVersionResponse(request);
    }
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    return request.getFixMessages().stream()
        .map(msg -> executor.submit(() ->
            processTags(msg, request.getDelimiter(), request.getVersion())))
        .map(future -> {
          try {
            return future.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
          }
        })
        .filter(Objects::nonNull)
        .toList();
  }

  @PostMapping(value = "/processFixMessages")
  public List<FixMessageResponseV1> convertFixMessages(@NonNull FixMessageRequestV1 request) {
    if (BeginStringValidator.isVersionNotSupported(request.getVersion())) {
    }
  }

}
