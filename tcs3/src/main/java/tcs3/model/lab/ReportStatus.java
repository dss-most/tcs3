package tcs3.model.lab;

import java.util.HashMap;
import java.util.Map;

public enum ReportStatus {
	
	/**
	 * public static final int NEW_REPORT = 0;
	public static final int SCIENTIST_APPROVE=1; 	//นักวิทย์ร่างรายงาน
	public static final int HEAD_GROUP_APPROVE=2;	//หน.กลุ่มฝ่ายพิจารณาร่างรายงาน เห็นชอบ
	public static final int HEAD_ORG_APPROVE=3;		//หน.โครงการสำนักพิจารณาร่างรายงาน เห็นชอบ (พร้อมพิมพ์รายงาน)
	public static final int HEAD_GROUP_COMMENT=4;	//หน.กลุ่มฝ่ายพิจารณาร่างรายงาน ไม่เห็นชอบ (ส่งให้นักวิทย์ทบทวน)
	public static final int HEAD_ORG_COMMENT=5;	//หน.โครงการสำนักพิจารณาร่างรายงาน ไม่เห็นชอบ (ส่งให้นักวิทย์ทบทวน)
	public static final int PRINTED=6;				//พิมพ์รายงานแล้วโดยสารบรรณ
	public static final int SEND_TO_SCIENTIST = 7;  //สารบรรณส่งรายงานให้นักวิทยาศาสตร์
	public static final int SCIENTIST_SIGN=8;		//นักวิทย์ลงนาม
	public static final int HEAD_GROUP_SIGN=9;		//หน.กลุ่มฝ่ายลงนามรายงาน เห็นชอบ
	public static final int HEAD_ORG_SIGN=10;		//หน.โครงการสำนักลงนามรายงาน  เห็นชอบ  
	public static final int ADD_REPORT_NO=11;		//สารบรรณออกเลขที่
	public static final int HEAD_ARCHIVE_SIGN=12; 	//หน.สารบรรณลงนามรายงาน เห็นชอบ (พร้อมส่งรายงาน)
	public static final int SENTED=13;				//ส่งรายงานแล้ว
	 */
	
	
	NEW_REPORT(0), SCIENTIST_APPROVE(1), HEAD_GROUP_APPROVE(2),
	HEAD_ORG_APPROVE(3), HEAD_GROUP_COMMENT(4), HEAD_ORG_COMMENT(5),
	PRINTED(6), SEND_TO_SCIENTIST(7), SCIENTIST_SIGN(8), HEAD_GROUP_SIGN(9), HEAD_ORG_SIGN(10),
	ADD_REPORT_NO(11), HEAD_ARCHIVE_SIGN(12), SENTED(13);
	
	private final Integer code;
	
	private static Map<Integer, ReportStatus> map = new HashMap<Integer, ReportStatus>();

    static {
        for (ReportStatus status : ReportStatus.values()) {
            map.put(status.code, status);
        }
    }
    
    public static ReportStatus valueOf(int code) {
        return map.get(code);
    }
	
	private ReportStatus(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() { 
		return this.code;
	}

	public String getHistoryString() {
		String s="";
		switch(this) {
		case NEW_REPORT:
			s= "สร้างรายงานใหม่";
			break;
		case SCIENTIST_APPROVE:
			s ="นักวิทย์ร่างรายงาน";
			break;
		case HEAD_GROUP_APPROVE:
			s = "หน.กลุ่มฝ่ายพิจารณาร่างรายงาน เห็นชอบ";
			break;
		case HEAD_ORG_APPROVE:
			s = "หน.โครงการสำนักพิจารณาร่างรายงาน เห็นชอบ (พร้อมพิมพ์รายงาน)";
			break;	
		case HEAD_GROUP_COMMENT:
			s = "หน.กลุ่มฝ่ายพิจารณาร่างรายงาน ไม่เห็นชอบ (ส่งให้นักวิทย์ทบทวน)";
			break;		
		case HEAD_ORG_COMMENT:
			s = "หน.โครงการสำนักพิจารณาร่างรายงาน ไม่เห็นชอบ (ส่งให้นักวิทย์ทบทวน์";
			break;	
			
		case PRINTED:
			s = "พิมพ์รายงานแล้วโดยสารบรรณ";
			break;	
		case SEND_TO_SCIENTIST:
			s = "สารบรรณส่งรายงานให้นักวิทยาศาสตร์";
			break;	
		case SCIENTIST_SIGN:
			s = "นักวิทย์ลงนาม";
			break;
		case HEAD_GROUP_SIGN:
			s = "หน.กลุ่มฝ่ายลงนามรายงาน เห็นชอบ";
			break;
		case HEAD_ORG_SIGN:
			s = "หน.โครงการสำนักลงนามรายงาน  เห็นชอบ";
			break;
		case ADD_REPORT_NO:
			s = "สารบรรณออกเลขที่";
			break;
		case HEAD_ARCHIVE_SIGN:
			s = "หน.สารบรรณลงนามรายงาน เห็นชอบ (พร้อมส่งรายงาน)";
			break;
		case SENTED:
			s = "ส่งรายงานแล้ว";
			break;
		default:
			break;
			
		}
		return s;
	}
	
}
