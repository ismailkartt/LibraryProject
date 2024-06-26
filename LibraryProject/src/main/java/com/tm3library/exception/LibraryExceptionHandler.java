package com.tm3library.exception;


import com.tm3library.exception.message.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
//import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.*;

@ControllerAdvice
public class LibraryExceptionHandler extends ResponseEntityExceptionHandler {


    Logger logger= LoggerFactory.getLogger(LibraryExceptionHandler.class);

    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {

        logger.error(error.getMessage());

        return new ResponseEntity<>(error,error.getStatus());

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );

        return buildResponseEntity(error);

    }

   @ExceptionHandler(ConflictException.class)
   protected ResponseEntity<Object> handleConflictException(
           ConflictException ex, WebRequest request) {
       ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT,
               ex.getMessage(),
               request.getDescription(false));

       return buildResponseEntity(error);
   }

   @ExceptionHandler(AccessDeniedException.class)
   protected ResponseEntity<Object> handleAccessDeniedException(
           AccessDeniedException ex, WebRequest request) {
       ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN,
               ex.getMessage(),
               request.getDescription(false));

        return buildResponseEntity(error);
    }

//   @ExceptionHandler(AuthenticationException.class)
//   protected ResponseEntity<Object> handleAuthenticationException(
//           AuthenticationException ex, WebRequest request) {
//       ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
//               ex.getMessage(),
//               request.getDescription(false));
//
//       return buildResponseEntity(error);
//   }

   @ExceptionHandler(BadRequestException.class)
   protected ResponseEntity<Object> handleBadRequestException(
           BadRequestException ex, WebRequest request) {
       ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
               ex.getMessage(),
               request.getDescription(false));

       return buildResponseEntity(error);
   }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().
                stream().
                map(e->e.getDefaultMessage()).
                collect(Collectors.toList());

        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                errors.get(0).toString(),
                request.getDescription(false))  ;
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false))  ;
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR
                ,
                ex.getMessage(),
                request.getDescription(false))  ;
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false))  ;
        return buildResponseEntity(error);
    }






    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRunTimeException(RuntimeException ex, WebRequest request){

        ApiResponseError error= new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(),request.getDescription(false));

        return buildResponseEntity(error);

    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request){

        ApiResponseError error= new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(),request.getDescription(false));

        return buildResponseEntity(error);

    }



}
