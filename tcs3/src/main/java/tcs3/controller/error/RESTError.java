package tcs3.controller.error;


import java.util.Date;

public class RESTError {

	public String message;
	public String stackTrace;
	public Date date;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	public Date getDate() {
		return date;
	}
	public String getTimestamp() {
		return date.toString();
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
