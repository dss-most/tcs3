package tcs3.controller.error;


import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.google.common.base.Throwables;

@ControllerAdvice(annotations = RestController.class)
public class RESTExceptionHandlerAdvice {
	/**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RESTError exception(Exception exception, WebRequest request) {
    	
    	RESTError error = new RESTError();
    	error.setMessage(exception.getMessage());
    	
    	String trace = Throwables.getStackTraceAsString(exception);
        error.setStackTrace(trace);
        
        error.setDate(new Date());
        
        return error;
    }
	
}
