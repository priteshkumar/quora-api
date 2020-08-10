package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupBusinessService {

  @Autowired
  UserDao userDao;

  @Autowired
  UserBusinessService userBusinessService;

  //@Transactional(propagation = Propagation.REQUIRED)
  public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {
    return userBusinessService.createUser(userEntity);
  }
}
