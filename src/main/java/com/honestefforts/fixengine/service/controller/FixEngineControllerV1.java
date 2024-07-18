package com.honestefforts.fixengine.service.controller;

import com.honestefforts.fixengine.model.endpoint.request.FixMessageRequestV1;
import com.honestefforts.fixengine.model.endpoint.response.FixMessageResponseV1;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.TagType;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.config.TagTypeMapConfig;
import com.honestefforts.fixengine.service.converter.messagetypes.BusinessMessageRejectConverter;
import com.honestefforts.fixengine.service.service.FixEngineService;
import com.honestefforts.fixengine.service.validation.header.BeginStringValidator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"})
public class FixEngineControllerV1 {
  @Autowired
  TagTypeMapConfig tagTypeMapConfig;
  @Autowired
  FixEngineService fixEngineService;

  /*@PostMapping(value = "/processFixMessages")
  public List<FixMessageResponseV1> processFixMessages(@NonNull FixMessageRequestV1 request) {
    if (BeginStringFixValidator.isVersionNotSupported(request.getVersion())) {
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
  }*/

  @PostMapping(value = "/processFixMessages")
  public List<FixMessageResponseV1> processFixMessages(@RequestBody @NonNull FixMessageRequestV1 request) {
    if (BeginStringValidator.isVersionNotSupported(request.getVersion())) {
      return getIncorrectVersionResponse(request);
    }
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    return request.getFixMessages().stream()
        .map(msg -> executor.submit(() ->
            fixEngineService.processTags(msg, request.getDelimiter(), request.getVersion())))
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

  private List<FixMessageResponseV1> getIncorrectVersionResponse(FixMessageRequestV1 request) {
    return List.of(
        FixMessageResponseV1.builder()
            .response(BusinessMessageRejectConverter
                .generate("Provided FIX version " + request.getVersion() + " is not supported"))
            .errors(List.of(ValidationError.builder().critical(true)
                .error("Provided FIX version " + request.getVersion() + " is not supported")
                .submittedTag(RawTag.builder().tag("[JSON] version").value(request.getVersion())
                    .version(request.getVersion()).dataType(TagType.CHARACTER).build())
                .build()))
            .build());
  }

}
