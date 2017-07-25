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
		this.code = code;
	}
	
	public Integer getCode() { 
		return this.code;
	}

	public String getHistoryString() {
		String s="";
		switch(this) {
		case NEW_REQ:
			s= "รอส่งใบคำร้องให้สารบรรณทำการออกเลขรับ";
			break;
		case APPROVE_REQUEST:
			s ="รับใบคำร้องแล้ว";
			break;
		case GERNERATE_REQNO:
			s = "สร้างใบคำร้อง";
			break;
		case AT_ORG:
			s = "รอการจ่ายงานที่โครงการ";
			break;	
		case WAIT_TO_SEND_TO_PART:
			s = "รอธุรการส่งเอกสารจ่ายงานให้กลุ่ม/ฝ่าย";
			break;		
		case AT_PART:
			s = "รอการจ่ายงานให้นักวิทยาศาสตร์";
			break;	
			
		case WORKING:
			s = "กำลังดำเนินการ";
			break;	
		case DRAFT:
			s = "กำลังพิจารณาร่างรายงาน";
			break;	
		case REPORT:
			s = "ดำเนินการลงนามรายงาน";
			break;
		case BACK_ARCHIVE:
			s = "โครงการกลุ่มส่งคืน";
			break;
		case BACK_ORG:
			s = "หัวหน้ากลุ่มส่งคืน";
			break;
		case FINISH:
			s = "จบการทำงาน";
			break;
		default:
			break;
			
		}
		return s;
	}
	
}
