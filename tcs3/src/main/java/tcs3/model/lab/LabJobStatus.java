package tcs3.model.lab;

import java.util.HashMap;
import java.util.Map;

public enum LabJobStatus {
	NEW_JOB(0), RETURN(1), FINISH(2), COOPERATE(3), CANCEL(4);
	
	private final Integer code;
	
	private static Map<Integer, LabJobStatus> map = new HashMap<Integer, LabJobStatus>();

    static {
        for (LabJobStatus status : LabJobStatus.values()) {
            map.put(status.code, status);
        }
    }
    
    public static LabJobStatus valueOf(int code) {
        return map.get(code);
    }
	
	private LabJobStatus(Integer code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}
	
	public Integer getCode() { 
		return this.code;
	}
	
}
