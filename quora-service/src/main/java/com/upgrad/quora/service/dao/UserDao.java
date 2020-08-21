package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Repository bean for UserEntity
 */
@Repository
public class UserDao {

  @PersistenceContext
  EntityManager entityManager;

  public UserEntity createUser(UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  public void deleteUser(UserEntity userEntity) {
    entityManager.remove(userEntity);
  }

  public UserEntity getUserByUserName(String userName) {
    try {
      return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter(
          "userName", userName).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public UserEntity getUserByEmail(String email) {
    try {
      return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter(
          "email", email).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public UserEntity getUserByUUID(String uuid) {
    try {
      return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter(
          "uuid", uuid).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public UserAuthEntity createUserAuth(UserAuthEntity userAuthEntity) {
    entityManager.persist(userAuthEntity);
    return userAuthEntity;
  }

  public UserAuthEntity getUserAuth(String accessToken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
          .setParameter("accessToken", accessToken).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void updateUserAuth(UserAuthEntity userAuthEntity) {
    entityManager.merge(userAuthEntity);
  }
}
