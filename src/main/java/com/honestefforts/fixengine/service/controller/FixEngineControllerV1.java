package com.honestefforts.fixengine.service.controller;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"})
public class FixEngineControllerV1 {
  @PostMapping(value = "/processFixMessages")
  public List<String> processFixMessages() {
    return List.of("This is a placeholder!");
  }
}
