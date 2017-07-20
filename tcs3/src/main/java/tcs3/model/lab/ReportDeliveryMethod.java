package tcs3.model.lab;

public enum ReportDeliveryMethod {
	POST(1), SELF(2), UNKNOWN(0);
	
	private final Integer code;
	
	private ReportDeliveryMethod(Integer code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}
	
	public String toString() {
		String s = "";
		switch (this) {
		case POST:
			s+= "ทางไปรษณีย์ ";
			break;
		case SELF:
			s+= "มารับด้วยตนเอง ";
			break;
		default:break;
		}
		return s;
	}
}
