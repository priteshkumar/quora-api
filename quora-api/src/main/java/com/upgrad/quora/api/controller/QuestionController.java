package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

  @Autowired
  QuestionService questionService;

  @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes =
      MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(
      @RequestHeader("authorization") String authorization,
      QuestionRequest questionRequest) throws AuthorizationFailedException {
    String[] authData = authorization.split("Bearer ");
    String accessToken = null;
    if (authorization.startsWith("Bearer ") == true) {
      accessToken = authData[1];
    } else {
      accessToken = authData[0];
    }
    QuestionEntity question = new QuestionEntity();
    question.setUuid(String.valueOf(UUID.randomUUID()));
    question.setContent(questionRequest.getContent());
    question.setCreatedDate(ZonedDateTime.now());
    QuestionEntity createdQuestion = questionService.createQuestion(accessToken, question);
    QuestionResponse questionResponse = new QuestionResponse();
    questionResponse.setId(createdQuestion.getUuid());
    questionResponse.setStatus("QUESTION CREATED");
    return new ResponseEntity<>(questionResponse, HttpStatus.CREATED);
  }
}
