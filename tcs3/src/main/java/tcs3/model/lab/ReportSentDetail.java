package tcs3.model.lab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="LAB_REPORT_SENTED_DETAIL")
@SequenceGenerator(name="LAB_REPORT_SENTED_DETAIL_SEQ", sequenceName="LAB_REPORT_SENTED_DETAIL_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Request.class)
public class ReportSentDetail {
	private final SimpleDateFormat thaiBuddistDateFormatter= new SimpleDateFormat ("d MMM yyyy", new Locale("th", "TH"));
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_REPORT_SENTED_DETAIL_SEQ")
	@Column(name="SEND_ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SEND_DATE")
	private Date sendDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INV_DATE")
	private Date invDate;
	
	@Basic
	@Column(name="INV_FLAG")
	private Boolean invFlag;
	
	@Basic
	@Column(name="INV_NO")
	private String invNo;
	
	@Basic
	@Column(name="INFORM")
	private Integer inform;
	
	@Basic
	@Column(name="RECEIVER")
	private String receiver;
	
	@Basic
	@Column(name="R_NO")
	private String postRegNo;
	
	@JoinColumn(name="REPORT_ID")
	private Report report;
	
	public String getDetail() {
		String s = "";
		if(inform == 2) {
			s += "ลูกค้า (" + this.receiver + ") มารับรายงานด้วยตนเอง";
		} else if (inform == 1) {
			s += "ส่งรายงานให้ลูกค้า (" + this.receiver + ") ทางไปรษณีย์ลงทะเบียนเลขที่ " + this.postRegNo;
		} else {
			return "";
		}
		
		s += " เมื่อวันที่ " + thaiBuddistDateFormatter.format(this.sendDate);
		
		if(this.invFlag == true) {
			s += " โดยมีการจ่ายค่าธรรมเนียมเพิ่มเติมเมื่อวันที่ " + thaiBuddistDateFormatter.format(this.invDate)
				+ " ใบเสร็จเลขที่ " + this.invNo;
		}
		
		return s;
	}
	
}
