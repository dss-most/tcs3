package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
	@Convert(converter=RequestStatusConverter.class)
	@Column(name="status")
	private RequestStatus status;
	
	@Basic
	@Column(name="tracking_code")
	private String trackingCode;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization mainOrg;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="MAIN_GROUP_ID")
	private Organization groupOrg;
	
	// ภาษาที่ใช้ในรายงาน
	@Convert(converter=ReportLanguageConverter.class)
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
	@Convert(converter=JobPriorityConverter.class)
	@Column(name="SPEED")
	private JobPriority speed;
	
	// วิธีการส่งรายงาน
	@Convert(converter=ReportDeliveryMethodConverter.class)
	@Column(name="INFORM")
	private ReportDeliveryMethod deliveryMethod;
	
	@OneToMany(mappedBy="request", fetch=FetchType.EAGER)
	@OrderColumn(name="EXAMPLE_INDEX")
	private List<RequestSample> samples;
	
	@ManyToOne
	@JoinColumn(name="EXAMPLE_ID")
	private SampleType sampleType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	public Date getReceivedReqDate() {
		return receivedReqDate;
	}

	public void setReceivedReqDate(Date receivedReqDate) {
		this.receivedReqDate = receivedReqDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public Organization getMainOrg() {
		return mainOrg;
	}

	public void setMainOrg(Organization mainOrg) {
		this.mainOrg = mainOrg;
	}

	public Organization getGroupOrg() {
		return groupOrg;
	}

	public void setGroupOrg(Organization groupOrg) {
		this.groupOrg = groupOrg;
	}

	public ReportLanguage getReportLanguage() {
		return reportLanguage;
	}

	public void setReportLanguage(ReportLanguage reportLanguage) {
		this.reportLanguage = reportLanguage;
	}

	public Boolean getSeparatedReportForSample() {
		return separatedReportForSample;
	}

	public void setSeparatedReportForSample(Boolean separatedReportForSample) {
		this.separatedReportForSample = separatedReportForSample;
	}

	public Boolean getTranslatedReport() {
		return translatedReport;
	}

	public void setTranslatedReport(Boolean translatedReport) {
		this.translatedReport = translatedReport;
	}

	public JobPriority getSpeed() {
		return speed;
	}

	public void setSpeed(JobPriority speed) {
		this.speed = speed;
	}

	public ReportDeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(ReportDeliveryMethod deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public List<RequestSample> getSamples() {
		return samples;
	}

	public void setSamples(List<RequestSample> samples) {
		this.samples = samples;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}
	
}
