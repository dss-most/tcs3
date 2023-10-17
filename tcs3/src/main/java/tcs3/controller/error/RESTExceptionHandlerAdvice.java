package tcs3.controller.error;


import java.util.Date;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class)
public class RESTExceptionHandlerAdvice {
	/**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RESTError exception(Exception exception, WebRequest request) {
    	exception.printStackTrace();
    	
    	RESTError error = new RESTError();
    	error.setMessage(exception.getMessage());
    	
        

    	String trace = exception.getStackTrace().toString();
        error.setStackTrace(trace);
        
        error.setDate(new Date());
        
        return error;
    }
	
}
