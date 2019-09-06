package com.solstice.exchangerate.exception;

import com.solstice.exchangerate.model.GenericResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<ExchangeRateNotFoundException> handleExchangeRateNotFoundException(ExchangeRateNotFoundException e) {
    logger.error("Exchange Rate Not Found");
    return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request){

      GenericResponse response = new GenericResponse("Invalid Request");
      logger.error("To Or From Param Missing");
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ResourceAlreadyExistsException> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        logger.error("Exchange Rate already exists");
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        GenericResponse requestBodyGenericResponse = new GenericResponse("Request Body Empty");
        logger.error("Request Body Empty");
        return new ResponseEntity<>(requestBodyGenericResponse, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<GenericResponse> handleConstraintViolationException(TransactionSystemException ex){
        Throwable cause = ((TransactionSystemException) ex).getRootCause();

        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
            // do something here

            Iterator iterator = constraintViolations.iterator();

            StringBuilder s = new StringBuilder();

            while(iterator.hasNext()){
                ConstraintViolation exception = (ConstraintViolation) iterator.next();
                s.append(exception.getPropertyPath()).append(" ").append(exception.getMessage()).append( ", ");
            }

            s.setLength(s.length() - 2);

            GenericResponse response = new GenericResponse(s.toString());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        GenericResponse response = new GenericResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
