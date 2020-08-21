package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


/**
 * Global exception handler
 * <p>
 * Implements handlers for needed api access exceptions/HTTP errors
 * Below exceptions are handled:
 * SignUpRestrictedException : HTTP 409
 * AuthenticationFailedException : HTTP 401
 * AuthorizationFailedException : HTTP 403
 * SignOutRestrictedException : HTTP 401
 * UserNotFoundException : HTTP 404
 * InvalidQuestionException : HTTP 404
 *
 */
@ControllerAdvice
public class QuoraExceptionHandler {

  @ExceptionHandler(SignUpRestrictedException.class)
  public ResponseEntity<ErrorResponse> signupRestrictedException(SignUpRestrictedException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.CONFLICT
    );
  }

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> authenticationException(AuthenticationFailedException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
        HttpStatus.UNAUTHORIZED
    );
  }

  @ExceptionHandler(SignOutRestrictedException.class)
  public ResponseEntity<ErrorResponse> signOutException(SignOutRestrictedException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
        HttpStatus.UNAUTHORIZED
    );
  }

  @ExceptionHandler(AuthorizationFailedException.class)
  public ResponseEntity<ErrorResponse> authFailedException(AuthorizationFailedException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
        HttpStatus.FORBIDDEN
    );
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(InvalidQuestionException.class)
  public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(AnswerNotFoundException.class)
  public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exe,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }
}
