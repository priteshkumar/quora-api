package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDao {

  @PersistenceContext
  EntityManager entityManager;

  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    entityManager.persist(answerEntity);
    return answerEntity;
  }

  public AnswerEntity getAnswerByUUID(String uuid) {
    try {
      return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid",
          uuid).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
    entityManager.remove(answerEntity);
    return answerEntity;
  }

  public AnswerEntity editAnswer(AnswerEntity answerEntity) {
    entityManager.merge(answerEntity);
    return answerEntity;
  }

  public List<AnswerEntity> getAllAnswersByQuestion(int question_id) {
    return entityManager.createNamedQuery("allAnswersByQuestion", AnswerEntity.class).setParameter(
        "question_id", question_id).getResultList();
  }
}
