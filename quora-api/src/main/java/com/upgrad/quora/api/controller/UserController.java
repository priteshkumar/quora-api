package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  SignupBusinessService signupBusinessService;

  @Autowired
  AuthenticationService authenticationService;

  @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes =
      MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest)
      throws SignUpRestrictedException {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(String.valueOf(UUID.randomUUID()));
    userEntity.setFirstName(signupUserRequest.getFirstName());
    userEntity.setLastName(signupUserRequest.getLastName());
    userEntity.setUserName(signupUserRequest.getUserName());
    userEntity.setEmail(signupUserRequest.getEmailAddress());
    userEntity.setPassword(signupUserRequest.getPassword());
    String country = signupUserRequest.getCountry();
    if (country != null) {
      userEntity.setCountry(country);
    }
    String aboutMe = signupUserRequest.getAboutMe();
    if (aboutMe != null) {
      userEntity.setAboutMe(aboutMe);
    }
    String dob = signupUserRequest.getDob();
    if (dob != null) {
      userEntity.setDob(dob);
    }
    String contactNumber = signupUserRequest.getContactNumber();
    if (contactNumber != null) {
      userEntity.setContactNumber(contactNumber);
    }
    userEntity.setRole("nonadmin");
    UserEntity createdUser = signupBusinessService.createUser(userEntity);
    SignupUserResponse signupUserResponse =
        new SignupUserResponse().id(createdUser.getUuid()).status("USER SUCCESSFULLY REGISTERED");
    return new ResponseEntity<>(signupUserResponse, HttpStatus.CREATED);
  }


  @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SigninResponse> signin(
      @RequestHeader("authorization") String authorization) throws AuthenticationFailedException {
    String authData = null;
    if (authorization.startsWith("Basic ")) {
      authData = authorization.split("Basic ")[1];
    } else {
      authData = authorization;
    }

    String[] loginDetails = new String(Base64.getDecoder().decode(authData)).split(":");
    UserAuthEntity userAuthEntity = authenticationService.authenticate(loginDetails[0],
        loginDetails[1]);
    UserEntity userEntity = userAuthEntity.getUser();
    SigninResponse signinResponse = new SigninResponse().id(userEntity.getUuid())
        .message("SIGNED IN "
            + "SUCCESSFULLY");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("access-token", userAuthEntity.getAccessToken());
    return new ResponseEntity<>(signinResponse, httpHeaders, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces =
      MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SignoutResponse> signout(
      @RequestHeader("authorization") String authorization)
      throws AuthenticationFailedException, SignOutRestrictedException {

    //authorization string may contain "Bearer " as prefix
    //handle this case
    String[] authData = authorization.split("Bearer ");
    String accessToken = null;
    if (authorization.startsWith("Bearer ") == true) {
      accessToken = authData[1];
    } else {
      accessToken = authData[0];
    }
    UserEntity userEntity = authenticationService.signout(accessToken);
    SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid())
        .message("SIGNED OUT "
            + "SUCCESSFULLY");

    return new ResponseEntity<>(signoutResponse, HttpStatus.OK);
  }
}
