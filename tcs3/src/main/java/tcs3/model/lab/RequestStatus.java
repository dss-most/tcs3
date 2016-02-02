package tcs3.model.lab;

import java.util.HashMap;
import java.util.Map;

public enum RequestStatus {
	NEW_REQ(0), APPROVE_REQUEST(1), APPROVE_RECEIVE_SAMPLE(2),
	GERNERATE_REQNO(3), AT_ORG(4), WAIT_TO_SEND_TO_PART(5),
	AT_PART(6), WORKING(7), DRAFT(8), REPORT(9), BACK_ARCHIVE(10),
	BACK_ORG(11), FINISH(12), EXPIRED(13), DELETED(99);
	
	private final Integer code;
	
	private static Map<Integer, RequestStatus> map = new HashMap<Integer, RequestStatus>();

    static {
        for (RequestStatus status : RequestStatus.values()) {
            map.put(status.code, status);
        }
    }
    
    public static RequestStatus valueOf(int code) {
        return map.get(code);
    }
	
	private RequestStatus(Integer code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}
	
	public Integer getCode() { 
		return this.code;
	}
	
}
