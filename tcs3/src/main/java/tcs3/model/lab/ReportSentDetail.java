package tcs3.model.lab;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "LAB_REPORT_SENTED_DETAIL")
@SequenceGenerator(name = "LAB_REPORT_SENTED_DETAIL_SEQ", sequenceName = "LAB_REPORT_SENTED_DETAIL_SEQ", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ReportSentDetail.class)
public class ReportSentDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -419558481474896569L;

	private final SimpleDateFormat thaiBuddistDateFormatter = new SimpleDateFormat("d MMM yyyy", new Locale("th", "TH"));

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LAB_REPORT_SENTED_DETAIL_SEQ")
	@Column(name = "SEND_ID")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_DATE")
	private Date sendDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INV_DATE")
	private Date invDate;

	@Basic
	@Column(name = "INV_FLAG")
	private Boolean invFlag;

	@Basic
	@Column(name = "INV_NO")
	private String invNo;

	@Basic
	@Column(name = "INFORM")
	private Integer inform;

	@Basic
	@Column(name = "RECEIVER")
	private String receiver;

	@Basic
	@Column(name = "R_NO")
	private String postRegNo;

	@ManyToOne
	@JoinColumn(name = "REPORT_ID")
	private Report report;

	public String getDetail() {
		String s = "";
		if (inform == 2) {
			s += "ลูกค้า (" + this.receiver + ") มารับรายงานด้วยตนเอง";
		} else if (inform == 1) {
			s += "ส่งรายงานให้ลูกค้า (" + this.receiver + ") ทางไปรษณีย์ลงทะเบียนเลขที่ " + this.postRegNo;
		} else {
			return "";
		}

		s += " เมื่อวันที่ " + thaiBuddistDateFormatter.format(this.sendDate);

		if (this.invFlag == true) {
			s += " โดยมีการจ่ายค่าบริการเพิ่มเติมเมื่อวันที่ " + thaiBuddistDateFormatter.format(this.invDate)
					+ " ใบเสร็จเลขที่ " + this.invNo;
		}

		return s;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Date getInvDate() {
		return invDate;
	}

	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}

	public Boolean getInvFlag() {
		return invFlag;
	}

	public void setInvFlag(Boolean invFlag) {
		this.invFlag = invFlag;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public Integer getInform() {
		return inform;
	}

	public void setInform(Integer inform) {
		this.inform = inform;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPostRegNo() {
		return postRegNo;
	}

	public void setPostRegNo(String postRegNo) {
		this.postRegNo = postRegNo;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public SimpleDateFormat getThaiBuddistDateFormatter() {
		return thaiBuddistDateFormatter;
	}

}
