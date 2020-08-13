package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

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
}
