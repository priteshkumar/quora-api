package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.common.AuthTokenParser;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//encodedData = "Basic " + window.btoa('rusty:test')

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

  @RequestMapping(method = RequestMethod.GET, path = "/question/all",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
      @RequestHeader("authorization") String authorization) throws AuthorizationFailedException {
    String[] authData = authorization.split("Bearer ");
    String accessToken = null;
    if (authorization.startsWith("Bearer ") == true) {
      accessToken = authData[1];
    } else {
      accessToken = authData[0];
    }
    List<QuestionEntity> qList = questionService.getAllQuestions(accessToken);
    List<QuestionDetailsResponse> qDetailsList = new ArrayList<>();
    qList.forEach((q) -> {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.id(q.getUuid());
      questionDetailsResponse.content(q.getContent());
      qDetailsList.add(questionDetailsResponse);
    });
    return new ResponseEntity<List<QuestionDetailsResponse>>(qDetailsList, HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
      @PathVariable("questionId") String questionId,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, InvalidQuestionException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    QuestionEntity deletedQuestion = questionService.deleteQuestion(accessToken, questionId);
    QuestionDeleteResponse questionDeleteResponse =
        new QuestionDeleteResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED");
    return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);
  }


  @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(
      @PathVariable("userId") String userId,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UserNotFoundException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    List<QuestionEntity> qList = questionService.getAllQuestionsByUser(accessToken, userId);
    List<QuestionDetailsResponse> qDetailsList = new ArrayList<>();
    qList.forEach((q) -> {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.content(q.getContent());
      questionDetailsResponse.id(q.getUuid());
      qDetailsList.add(questionDetailsResponse);
    });
    return new ResponseEntity<>(qDetailsList, HttpStatus.OK);
  }

  //editQuestionContent - "/question/edit/{questionId}"
  //to do later
  @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestionContent(
      @PathVariable("questionId") String questionId, final QuestionEditRequest questionEditRequest,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, InvalidQuestionException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    String content = questionEditRequest.getContent();
    QuestionEntity updatedQuestion = questionService.updateQuestion(accessToken, questionId, content);
    QuestionEditResponse questionEditResponse = new QuestionEditResponse();
    questionEditResponse.setId(updatedQuestion.getUuid());
    questionEditResponse.setStatus("QUESTION EDITED");
    return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);
  }
}