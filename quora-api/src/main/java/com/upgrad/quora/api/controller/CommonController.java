package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.common.AuthTokenParser;
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

/**
 * RestController bean to handle userprofile endpoint
 */
@RestController
public class CommonController {

  @Autowired
  CommonService commonService;

  /**
   * gets profile of a registered user
   * <p>
   * Requires bearer authorization via jwt token
   *
   * @param userID
   * @param authorization
   * @return UserDetailsResponse with HTTP code 200
   * @throws AuthorizationFailedException
   * @throws UserNotFoundException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces =
      MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable("userId") String userID,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UserNotFoundException {
    String accessToken = AuthTokenParser.parseAuthToken(authorization);
    UserEntity userEntity = commonService.getUserProfile(userID, accessToken);
    UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
    userDetailsResponse.setFirstName(userEntity.getFirstName());
    userDetailsResponse.setLastName(userEntity.getLastName());
    userDetailsResponse.setUserName(userEntity.getUserName());
    userDetailsResponse.setEmailAddress(userEntity.getEmail());
    if (userEntity.getCountry() != null) {
      userDetailsResponse.setCountry(userEntity.getCountry());
    }
    if (userEntity.getAboutMe() != null) {
      userDetailsResponse.setAboutMe(userEntity.getAboutMe());
    }
    if (userEntity.getDob() != null) {
      userDetailsResponse.setDob(userEntity.getDob());
    }
    if (userEntity.getContactNumber() != null) {
      userDetailsResponse.setContactNumber(userEntity.getContactNumber());
    }
    return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
  }
}
