package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  SignupBusinessService signupBusinessService;

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
}
