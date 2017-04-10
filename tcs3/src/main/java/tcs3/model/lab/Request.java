package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.customer.Customer;
import tcs3.model.hrx.Officer;
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
	
	@Basic
	@Column(name="TYPE")
	private Integer type;
	
	// NEW FIELD
	@ManyToOne
	@JoinColumn(name="QUOTATION_TCS3_ID")
	private Quotation quotation;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQ_DATE")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date reqDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECEIVE_REQ_DATE")
	private Date receivedReqDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECEIVE_DATE")
	private Date receivedDate;
	
	@Basic
	@Column(name="CUSTOMER_FULLNAME")
	private String companyName;
	
	@Basic
	@Column(name="estimated_working_day")
	private Integer estimatedWorkingDay;
	
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
	
	
	
	
	@Basic
	@Column(name="SEND_TO")
	private String addressTitle;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="LT_ADDRESS_ID")
	private RequestAddress address;
	
	
	@Basic
	@Column(name="SEND_LETTER_TO")
	private String reportTitle;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="LTH_ADDRESS_ID")
	private RequestAddress reportAddress;
	
	@Basic
	@Column(name="SEND_INV_TO")
	private String invoiceTitle;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="INV_ADDRESS_ID")
	private RequestAddress invoiceAddress;
	
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
	
	
	// หน่วยงานรับตัวอย่าง
	@ManyToOne
	@JoinColumn(name="RECEIVER_DEPT_ID ")
	private Organization sampleReceiverOrg;
	
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
	
	@OneToMany(mappedBy="request", fetch=FetchType.EAGER,
			cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderColumn(name="EXAMPLE_INDEX")
	private List<RequestSample> samples;
	
	@ManyToOne
	@JoinColumn(name="EXAMPLE_ID")
	private SampleType sampleType;

	@OneToMany(mappedBy="request")
	@OrderColumn(name="INVOID_INDEX")
	private List<Invoice> invoices;
	
	@OneToMany(mappedBy="request")
	@OrderColumn(name="INVOID_INDEX")
	private List<RequestPromotionDiscount> promotions;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createdTime;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY")
	private Officer createdBy;
	
	@ManyToOne
	@JoinColumn(name="LAST_UPDATED_BY")
	private Officer lastUpdatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdatedTime;
	
	@OneToMany(mappedBy="request", cascade={CascadeType.ALL})
	@OrderColumn(name="HIS_INDEX")
	private List<RequestHistory> histories;
	
	
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

	public Organization getSampleReceiverOrg() {
		return sampleReceiverOrg;
	}

	public void setSampleReceiverOrg(Organization sampleReceiverOrg) {
		this.sampleReceiverOrg = sampleReceiverOrg;
	}

	public Integer getEstimatedWorkingDay() {
		return estimatedWorkingDay;
	}

	public void setEstimatedWorkingDay(Integer estimatedWorkingDay) {
		this.estimatedWorkingDay = estimatedWorkingDay;
	}

	

	public RequestAddress getAddress() {
		return address;
	}

	public void setAddress(RequestAddress address) {
		this.address = address;
	}
	
//	public void setAddress(Address address) {
//		this.address = RequestAddress.parseAddress(address);
//	}

	public RequestAddress getReportAddress() {
		return reportAddress;
	}

	public void setReportAddress(RequestAddress reportAddress) {
		this.reportAddress = reportAddress;
	}
	
//	public void setReportAddress(Address reportAddress) {
//		this.reportAddress = RequestAddress.parseAddress(reportAddress);
//	}
	

	public RequestAddress getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(RequestAddress invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

//	public void setInvoiceAddress(Address invoiceAddress) {
//		this.invoiceAddress = RequestAddress.parseAddress(invoiceAddress);
//	}

	
	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<RequestPromotionDiscount> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<RequestPromotionDiscount> promotions) {
		this.promotions = promotions;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Officer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Officer createdBy) {
		this.createdBy = createdBy;
	}

	public Officer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Officer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public List<RequestHistory> getHistories() {
		return histories;
	}

	public void setHistories(List<RequestHistory> histories) {
		this.histories = histories;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getAddressTitle() {
		return addressTitle;
	}

	public void setAddressTitle(String addressTitle) {
		this.addressTitle = addressTitle;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	
	
	
	
	
}
