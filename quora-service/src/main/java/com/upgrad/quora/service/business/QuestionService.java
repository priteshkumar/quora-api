package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private QuestionDao questionDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(String accessToken, QuestionEntity question)
      throws AuthorizationFailedException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post"
          + " a question");
    }
    question.setUser(userAuth.getUser());
    QuestionEntity createdQuestion = questionDao.createQuestion(question);
    return createdQuestion;
  }

  public List<QuestionEntity> getAllQuestions(String accessToken)
      throws AuthorizationFailedException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get"
          + " all questions");
    }
    return questionDao.getAllQuestions();
  }
}