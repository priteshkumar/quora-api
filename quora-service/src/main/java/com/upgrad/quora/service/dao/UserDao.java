package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

  @PersistenceContext
  EntityManager entityManager;

  public UserEntity createUser(UserEntity userEntity){
    entityManager.persist(userEntity);
    return userEntity;
  }

  public UserEntity getUserByUserName(String userName){
    try{
      return entityManager.createNamedQuery("userByUserName",UserEntity.class).setParameter(
          "userName",userName).getSingleResult();
    }catch(NoResultException e){
      return null;
    }
  }

  public UserEntity getUserByEmail(String email){
    try{
      return entityManager.createNamedQuery("userByEmail",UserEntity.class).setParameter(
          "email",email).getSingleResult();
    }catch(NoResultException e){
      return null;
    }
  }
}
