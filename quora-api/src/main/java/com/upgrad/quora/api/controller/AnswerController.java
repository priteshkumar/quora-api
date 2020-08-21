package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.common.AuthTokenParser;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

  @Autowired
  AnswerService answerService;

  /**
   * Creates answer for a specific question in api db
   * <p>
   * Requires bearer authorization
   *
   * @param questionId
   * @param answerRequest
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") String questionId,
      AnswerRequest answerRequest, @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, InvalidQuestionException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    AnswerEntity answerEntity = new AnswerEntity();
    answerEntity.setAns(answerRequest.getAnswer());
    AnswerEntity createdAnswer = answerService.createAnswer(accessToken, questionId, answerEntity);
    AnswerResponse answerResponse = new AnswerResponse();
    answerResponse.setId(createdAnswer.getUuid());
    answerResponse.setStatus("ANSWER CREATED");
    return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);
  }

  /**
   * Deletes a specific answer in api db
   * <p>
   * Requires bearer authorization
   *
   * @param answerId
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws AnswerNotFoundException
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
      @PathVariable("answerId") String answerId,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, AnswerNotFoundException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    AnswerEntity deletedAnswer = answerService.deleteAnswer(accessToken, answerId);
    AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
    answerDeleteResponse.setId(deletedAnswer.getUuid());
    answerDeleteResponse.setStatus("ANSWER DELETED");
    return new ResponseEntity<>(answerDeleteResponse, HttpStatus.OK);
  }

  /**
   * Retrieves all answers to a specific question
   * <p>
   * Requires Bearer authorization
   *
   * @param questionId
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
      @PathVariable("questionId") String questionId,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, InvalidQuestionException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    List<AnswerEntity> aList = answerService
        .getAllAnswersToQuestion(accessToken, questionId);
    List<AnswerDetailsResponse> answerDetailsResponseList = new ArrayList<>();
    aList.forEach((a) -> {
      AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
      answerDetailsResponse.setId(a.getUuid());
      answerDetailsResponse.setQuestionContent(a.getQuestion().getContent());
      answerDetailsResponse.setAnswerContent(a.getAns());
      answerDetailsResponseList.add(answerDetailsResponse);
    });
    return new ResponseEntity<>(answerDetailsResponseList, HttpStatus.OK);
  }

  /**
   * Edits a specific answer in api db
   * <p>
   * Requires bearer auth
   *
   * @param answerId
   * @param answerEditRequest
   * @param authorization
   * @return
   * @throws AuthorizationFailedException
   * @throws AnswerNotFoundException
   */
  @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes =
      MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AnswerEditResponse> editAnswerContent(
      @PathVariable("answerId") String answerId,
      AnswerEditRequest answerEditRequest,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, AnswerNotFoundException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    String content = answerEditRequest.getContent();
    AnswerEntity answerEntity = answerService.editAnswer(accessToken, content, answerId);
    AnswerEditResponse answerEditResponse = new AnswerEditResponse();
    answerEditResponse.setId(answerEntity.getUuid());
    answerEditResponse.setStatus("ANSWER EDITED");
    return new ResponseEntity<>(answerEditResponse, HttpStatus.OK);
  }
}
