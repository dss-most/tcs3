package tcs3.webUI;

public class ResponseJSend<T> {
	public ResponseJSend(ResponseStatus status, T data) {
		this.status = status;
		this.data = data;
	}

	public ResponseStatus status;
	
	public T data;
	
}
