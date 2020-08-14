package com.upgrad.quora.service.common;

/**
 * utility class
 * provides method to parse Bearer authorization in http request header
 */
public class AuthTokenParser {

  /**
   * Checks if "Bearer " is present in authorization header
   * @param authorization
   * @return accessToken
   */
  public static String parseAuthToken(String authorization) {
    String[] authData = authorization.split("Bearer ");
    String accessToken = null;
    if (authorization.startsWith("Bearer ") == true) {
      accessToken = authData[1];
    } else {
      accessToken = authData[0];
    }
    return accessToken;
  }
}
