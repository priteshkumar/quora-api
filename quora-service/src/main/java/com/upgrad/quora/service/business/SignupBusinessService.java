package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * wrapper service class for user creation
 */
@Service
public class SignupBusinessService {

  @Autowired
  UserDao userDao;

  @Autowired
  UserBusinessService userBusinessService;

  /**
   * wrapper service method for user creation in api db
   * calls UserBusinessService
   *
   * @param userEntity
   * @return
   * @throws SignUpRestrictedException
   */
  public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {
    return userBusinessService.createUser(userEntity);
  }
}
