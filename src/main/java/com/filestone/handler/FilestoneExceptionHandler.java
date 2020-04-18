package com.filestone.handler;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import com.filestone.pojo.Message;
import com.filestone.util.AppUtil;

/**
 * A {@link @ControllerAdvice} class will provides a global handling for all exceptions throughout this application. 
 * @author Hoffman
 *
 */
@ControllerAdvice
public class FilestoneExceptionHandler {
	public static final Logger logger = Logger.getLogger(FilestoneExceptionHandler.class);


	/**
	 * Main handler method for proxying caught exception
	 * @param ex
	 * @param request
	 * @return
	 */
    @ExceptionHandler({ FilestoneMediaFileException.class})
    public final ResponseEntity<Message> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof FilestoneMediaFileException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            FilestoneMediaFileException unfe = (FilestoneMediaFileException) ex;

            return handleFilestoneMediaFileException(unfe, headers, status, request);
        } 
        
        else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

   /**
    * A Custom error response method will handle  all {@link FilestoneMediaFileException} Exception type.
    * @param ex
    * @param headers
    * @param status
    * @param request
    * @return {@link ResponseEntity<Message>}
    */
    protected ResponseEntity<Message> handleFilestoneMediaFileException(FilestoneMediaFileException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       //Uncomment in case you want to return a cellection of error to the user
    	// List<String> errors = Collections.singletonList(ex.getMessage());
    	String errors = ex.getMessage();
    	logger.error("A FilestoneMediaFileException has been thrown, Error Message: :" + errors);
        return handleExceptionInternal(ex, new Message(errors), headers, status, request);
    }

    /**
     * Main method that handle the response body of all application Exception types that been caught
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
    * @return {@link ResponseEntity<Message>}
     */
    protected ResponseEntity<Message> handleExceptionInternal(Exception ex, Message body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}