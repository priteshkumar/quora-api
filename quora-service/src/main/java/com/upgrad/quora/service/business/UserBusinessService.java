package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
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

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity deleteUser(String userId, String accessToken)
      throws AuthorizationFailedException, UserNotFoundException {
    UserAuthEntity userAuth = userDao.getUserAuth(accessToken);
    if (userAuth == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuth.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", "User is signed out");
    } else if (userAuth.getUser().getRole().equals("nonadmin")) {
      throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not"
          + " an admin");
    }
    UserEntity userEntity = userDao.getUserByUUID(userId);
    if (userEntity == null) {
      throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not "
          + "exist");
    }
    userDao.deleteUser(userEntity);
    return userEntity;
  }
}
