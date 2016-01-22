package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.customer.Company;
import tcs3.model.customer.Customer;
import tcs3.model.hrx.Organization;

@Entity
@Table(name="lab_request")
@SequenceGenerator(name="lab_request_SEQ", sequenceName="lab_request_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Request.class)
public class Request implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8016729242125808341L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="lab_request_SEQ")
	@Column(name="REQ_ID")
	private Long id;

	@Basic
	@Column(name="REQ_NO")
	private String reqNo;
	
	// NEW FIELD
	@ManyToOne
	@JoinColumn(name="QUOTATION_TCS3_ID")
	private Quotation quotation;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQ_DATE")
	private Date reqDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECEIVE_REQ_DATE")
	private Date receivedReqDate;
	
	@Basic
	@Column(name="CUSTOMER_FULLNAME")
	private String companyName;
	
	@Basic
	@Column(name="contactor_fullname")
	private String customerName;
	
	// NEW FIELD
	// ลูกค้า (ถ้ามี)
	@ManyToOne
	@JoinColumn(name="CUSTOMER_TCS3_ID")
	private Customer customer;

	// บริษัทที่ส่งตัวอย่าง
	@ManyToOne
	@JoinColumn(name="COMPANY_ID")
	private Company company;
	
	// สถานะของคำร้อง
	@Enumerated(EnumType.ORDINAL)
	@Column(name="status")
	private RequestStatus status;
	
	@Basic
	@Column(name="tracking_code")
	protected String trackingCode;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization mainOrg;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="MAIN_GROUP_ID")
	private Organization groupOrg;
	
	// ภาษาที่ใช้ในรายงาน
	@Enumerated(EnumType.ORDINAL)
	@Column(name="LANGUAGE")
	private ReportLanguage reportLanguage;
	
	// แยกรายงานแต่ละตัวอย่างหรือไม่
	@Basic 
	@Column(name="IS_SEPERATE")
	private Boolean separatedReportForSample;
	
	// แปลรายงานหรือไม่
	@Basic 
	@Column(name="IS_TRANSLATE")
	private Boolean translatedReport;
	
	// ด่วนพิเศษ หรือ ธรรมดา
	@Enumerated(EnumType.ORDINAL)
	@Column(name="SPEED")
	private JobPriority speed;
	
	// วิธีการส่งรายงาน
	@Enumerated(EnumType.ORDINAL)
	@Column(name="INFORM")
	private ReportDeliveryMethod deliveryMethod;
	
	
	
}
