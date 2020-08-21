package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * service bean for handling Question endpoint access
 */
@Service
public class QuestionService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private QuestionDao questionDao;

  /**
   * Creates question in api db via QuestionDao
   * Signed in users are allowed to create question
   *
   * @param accessToken
   * @param question
   * @return
   * @throws AuthorizationFailedException
   */
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

  /**
   * Gets all questions from api db via question dao
   * Signed in users are allowed to access this endpoint
   *
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   */
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

  /**
   * Deletes a specific question in api db via question dao
   * Only signed in admin or owner of question can delete it
   *
   * @param accessToken
   * @param uuid
   * @return
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity deleteQuestion(String accessToken, String uuid)
      throws AuthorizationFailedException, InvalidQuestionException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to "
          + "delete a question");
    }

    QuestionEntity question = questionDao.getQuestionByUUID(uuid);
    if (question == null) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
    UserEntity authUser = userAuth.getUser();
    if (authUser.getRole().equals("nonadmin") && !authUser.getUserName()
        .equals(question.getUser().getUserName())) {
      throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can "
          + "delete the question");
    }
    QuestionEntity deletedQuestion = questionDao.deleteQuestion(question);
    return deletedQuestion;
  }

  /**
   * Gets all question by a specific user
   * signed in users can access this endpoint
   *
   * @param accessToken
   * @param userId
   * @return
   * @throws AuthorizationFailedException
   * @throws UserNotFoundException
   */
  public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId)
      throws AuthorizationFailedException, UserNotFoundException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get "
          + "all questions posted by a specific user");
    }
    UserEntity userEntity = userDao.getUserByUUID(userId);
    if (userEntity == null) {
      throw new UserNotFoundException("USR-001", "User with entered uuid whose question details "
          + "are to be seen does not exist");
    }
    return questionDao.getAllQuestionsByUser(userEntity.getId());
  }

  /**
   * Edits a specific question content in api db
   * Needs signed in user/owner of the question
   *
   * @param accessToken
   * @param questionId
   * @param content
   * @return
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity updateQuestion(String accessToken, String questionId, String content)
      throws AuthorizationFailedException, InvalidQuestionException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit"
          + " the question");
    }
    QuestionEntity question = questionDao.getQuestionByUUID(questionId);
    if (question == null) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
    if (!question.getUser().getUserName().equals(userAuth.getUser().getUserName())) {
      throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the "
          + "question");
    }
    question.setContent(content);
    return questionDao.updateQuestion(question);
  }
}