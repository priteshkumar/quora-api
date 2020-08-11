package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

  @Autowired
  UserDao userDao;

  @Autowired
  PasswordCryptographyProvider passwordCryptographyProvider;

  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthEntity authenticate(String userName,String password)
      throws AuthenticationFailedException {
    UserEntity userEntity = userDao.getUserByUserName(userName);
    if(userEntity == null)
      throw new AuthenticationFailedException("ATH-001","This username does not exist");

    String encryptedText = passwordCryptographyProvider.encrypt(password,userEntity.getSalt());
    if(encryptedText.equals(userEntity.getPassword()) == false)
      throw new AuthenticationFailedException("ATH-002","Password failed");

    UserAuthEntity userAuthEntity = new UserAuthEntity();
    userAuthEntity.setUuid(String.valueOf(UUID.randomUUID()));
    userAuthEntity.setUser(userEntity);
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime expires = now.plusHours(8);
    userAuthEntity.setLoginAt(now);
    userAuthEntity.setExpiresAt(expires);
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedText);
    userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expires));
    UserAuthEntity createdUserAuth = userDao.createUserAuth(userAuthEntity);
    return createdUserAuth;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signout(String accessToken) throws SignOutRestrictedException {
    UserAuthEntity userAuthEntity = userDao.getUserAuth(accessToken);
    if(userAuthEntity == null)
      throw new SignOutRestrictedException("SGR-001","User is not Signed in");
    UserEntity userEntity = userAuthEntity.getUser();
    userAuthEntity.setLogoutAt(ZonedDateTime.now());
    userDao.updateUserAuth(userAuthEntity);
    return userEntity;
  }
}
