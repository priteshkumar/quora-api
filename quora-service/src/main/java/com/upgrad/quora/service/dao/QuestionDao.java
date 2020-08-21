package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Repository bean for Question entity
 */
@Repository
public class QuestionDao {

  @PersistenceContext
  EntityManager entityManager;

  public QuestionEntity createQuestion(QuestionEntity question) {
    entityManager.persist(question);
    return question;
  }

  public List<QuestionEntity> getAllQuestions() {
    return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
  }

  public List<QuestionEntity> getAllQuestionsByUser(long userId) {
    return entityManager.createNamedQuery("allQuestionsByUser", QuestionEntity.class)
        .setParameter("user_id", userId).getResultList();
  }

  public QuestionEntity getQuestionByUUID(String uuid) {
    try {
      return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter(
          "uuid",
          uuid).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public QuestionEntity updateQuestion(QuestionEntity question) {
    entityManager.merge(question);
    return question;
  }

  public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
    entityManager.remove(questionEntity);
    return questionEntity;
  }
}
