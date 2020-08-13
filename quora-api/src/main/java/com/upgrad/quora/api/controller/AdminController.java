package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

  @Autowired
  UserBusinessService userBusinessService;

  @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") String userId,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UserNotFoundException {
    String[] authData = authorization.split("Bearer ");
    String accessToken = null;
    if (authorization.startsWith("Bearer ") == true) {
      accessToken = authData[1];
    } else {
      accessToken = authData[0];
    }

    UserEntity deletedUser = userBusinessService.deleteUser(userId, accessToken);
    UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(deletedUser.getUuid())
        .status("USER SUCCESSFULLY DELETED");
    return new ResponseEntity<>(userDeleteResponse, HttpStatus.OK);
  }
}