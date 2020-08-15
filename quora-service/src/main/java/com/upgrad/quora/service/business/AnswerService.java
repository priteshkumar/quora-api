package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

  @Autowired
  UserDao userDao;

  @Autowired
  QuestionDao questionDao;

  @Autowired
  AnswerDao answerDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity createAnswer(String accessToken, String questionId,
      AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException {
    UserAuthEntity userAuthEntity = userDao.getUserAuth(accessToken);
    if (userAuthEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to "
          + "post an answer");
    }
    QuestionEntity question = questionDao.getQuestionByUUID(questionId);
    if (question == null) {
      throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
    }
    answerEntity.setUuid(String.valueOf(UUID.randomUUID()));
    answerEntity.setQuestion(question);
    answerEntity.setPostedDate(ZonedDateTime.now());
    answerEntity.setUser(userAuthEntity.getUser());
    return answerDao.createAnswer(answerEntity);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity deleteAnswer(String accessToken, String answerId)
      throws AuthorizationFailedException, AnswerNotFoundException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to "
          + "delete an answer");
    }
    AnswerEntity answer = answerDao.getAnswerByUUID(answerId);
    if (answer == null) {
      throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
    }
    if (!userAuth.getUser().getRole().equals("admin") && !answer.getUser().getUserName()
        .equals(userAuth.getUser().getUserName())) {
      throw new AuthorizationFailedException("ATHR-003",
          "Only the answer owner or admin can delete "
              + "the answer");
    }
    AnswerEntity deletedAnswer = answerDao.deleteAnswer(answer);
    return deletedAnswer;
  }
}
