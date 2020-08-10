package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

  @Autowired
  UserDao userDao;

  @Autowired
  PasswordCryptographyProvider passwordCryptographyProvider;

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {
    if (userDao.getUserByUserName(userEntity.getUserName()) != null) {
      throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has "
          + "already been taken");
    }
    if (userDao.getUserByEmail(userEntity.getEmail()) != null) {
      throw new SignUpRestrictedException("SGR-002",
          "This user has already been registered, try with any other emailId.");
    }

    if (userEntity.getPassword() == null) {
      userEntity.setPassword("quora@123");
    }
    String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(encryptedText[0]);
    userEntity.setPassword(encryptedText[1]);
    return userDao.createUser(userEntity);
  }
}
