package tcs3.model.lab;

public enum ReportDeliveryMethod {
	POST(1), SELF(2), UNKNOWN(0);
	
	private final Integer code;
	
	private ReportDeliveryMethod(Integer code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}
}
