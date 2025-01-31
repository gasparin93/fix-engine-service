package com.honestefforts.fixengine.service.controller;

import com.honestefforts.fixengine.model.endpoint.request.FixMessageRequestV1;
import com.honestefforts.fixengine.model.endpoint.response.FixMessageResponseV1;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.message.tags.TagType;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.security.jwt.utility.ValidationUtil;
import com.honestefforts.fixengine.service.service.FixEngineService;
import com.honestefforts.fixengine.service.validation.header.BeginStringValidator;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"})
public class FixEngineControllerV1 {
	
  private Logger logger = Logger.getLogger(FixEngineControllerV1.class.getName());

  @Autowired
  FixEngineService fixEngineService;
  
  @Autowired
  ValidationUtil validationUtil;

  @PostMapping(value = "/processFixMessages")
  public List<FixMessageResponseV1> processFixMessages(
		  @RequestBody @NonNull FixMessageRequestV1 request, HttpServletRequest httpServletRequest) {
    StringBuffer strB = validationUtil.constructRequestLogsAfterJWTTokenFilterRequest(httpServletRequest);
    logger.log(Level.INFO, strB.toString());
    
    // consumerId attribute/header
    String xConsumerId = httpServletRequest.getAttribute("encodedUserId").toString();
    // consumerUsername attribute/header
    String xConsumerUsername = httpServletRequest.getAttribute("consumerUsername").toString();
    
    // Determine what to do next and how to best handle the information given above:
    // Roles based access using other data governance or roles governance tables?
    // Continue with request regardless?
    
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
            .response(null)
            .errors(List.of(ValidationError.builder().critical(true)
                .error("Provided FIX version " + request.getVersion() + " is not supported")
                .submittedTag(RawTag.builder().value(request.getVersion())
                    .dataType(TagType.CHARACTER).build())
                .build()))
            .build());
  }

}
