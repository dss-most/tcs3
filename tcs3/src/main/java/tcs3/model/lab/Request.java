package tcs3.model.lab;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
	
	public static Logger logger = LoggerFactory.getLogger(Request.class);
	
	
	private static final NumberFormat feeFormat = new DecimalFormat("#,##0.00");

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
	private Date reqDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECEIVE_REQ_DATE")
	private Date receivedReqDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RECEIVE_DATE")
	private Date receivedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PAYMENT_DUE_DATE")
	private Date paymentDueDate;
	
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
	
	
	// กองที่ทำการบันทึกข้อมูล
	@ManyToOne
	@JoinColumn(name="CREATEDBY_DEPARTMENT")
	private Organization createdByOrg;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization mainOrg;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="MAIN_GROUP_ID")
	private Organization groupOrg;
	
	@OneToMany(mappedBy="")
	@OrderColumn(name="REPORT_INDEX")
	private List<Report> reports;
	
	
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

	// ต้องการตัวอย่างคืนหรือไม่
	@Basic 
	@Column(name="IS_RETURN_EXAMPLE")
	private Boolean sampleReturn = false;

	// ต้องการให้ วศ. ดำเนินการหรือ sub contract
	@Basic 
	@Column(name="IS_PROCESS_BYDSS")
	private Boolean processByDSS = false;
	
	// ไม่แน่ใจว่าคืออะไร
	@Basic 
	@Column(name="IS_TAKEOVER")
	private Boolean takeOver = false;	
	
	// ด่วนพิเศษ หรือ ธรรมดา
	@Convert(converter=JobPriorityConverter.class)
	@Column(name="SPEED")
	private JobPriority speed;
	
	// วิธีการส่งรายงาน
	@Convert(converter=ReportDeliveryMethodConverter.class)
	@Column(name="INFORM")
	private ReportDeliveryMethod deliveryMethod;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="request",cascade=CascadeType.ALL)
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

	public String getInvoiceNoBillPayment() {
		if(this.getReqNo() == null) return "";
		
		return  this.getReqNo().substring(1, 3) + 
						"001" +
						this.getReqNo().substring(4, 9);
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
		logger.debug("Getting samples.....");
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
		logger.debug("getInvoices.....");
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

	public Organization getCreatedByOrg() {
		return createdByOrg;
	}

	public void setCreatedByOrg(Organization createdByOrg) {
		this.createdByOrg = createdByOrg;
	}

	public Boolean getSampleReturn() {
		return sampleReturn;
	}

	public void setSampleReturn(Boolean sampleReturn) {
		this.sampleReturn = sampleReturn;
	}

	public Boolean getProcessByDSS() {
		return processByDSS;
	}

	public void setProcessByDSS(Boolean processByDSS) {
		this.processByDSS = processByDSS;
	}

	public Boolean getTakeOver() {
		return takeOver;
	}

	public void setTakeOver(Boolean takeOver) {
		this.takeOver = takeOver;
	}
	
	public Boolean isExpress() {
		if(this.getSpeed() == JobPriority.EXPRESS) {
			return true;
		}
		return false;
	}
	public String getCustomerFullname() {
		return customerName;
	}
	
	public String getResultDeliverSummary() {
		return this.deliveryMethod.toString();
		
	}
	
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getReportDetailSummary() {
		String s = "";
		if(this.reportLanguage == ReportLanguage.TH) {
			s += "ภาษาไทย ";
		} else {
			s += "ภาษาอังกฤษ ";
		}
		
		if(this.separatedReportForSample == true) {
			s += " / แยกรายงานตามตัวอย่าง";
		} else {
			s += " / รายงานรวมทุกตัวอย่าง";
		}
		
		return s;
	}
	
	public String getTranslateSummary() {
		String s = "";
		if(this.translatedReport == true) { 
			s += "มีการแปล ";
		} else {
			s += "ไม่มีการแปล ";
		}
		
		return s;
	}
	
	public String getSpeedSummary() {
		String s = "";
		if(this.speed == JobPriority.NORMAL) { 
			s += "ธรรมดา ";
		} else {
			s += "ด่วนพิเศษ ";
		}
		
		return s;
	}

	public Double getTotalReqExampleFee() {
		Double sum = 0.0;
		logger.debug("getting getTotalReqExampleFee samples.... req_id: " + this.id);
		for(RequestSample reqEx : this.samples) {
			
			logger.debug("reqEx_id: "+ reqEx.getId() + " fee: "+ reqEx.getTotalJobFee());
			 
			sum += reqEx.getTotalJobFee();
		}
		
		return sum;
	
	}
	public String getFormatedTotalReqExampleFee() {
		Double total = this.getTotalReqExampleFee();
		if(this.speed == JobPriority.EXPRESS){
			total = total * 3;
		}
		
		return feeFormat.format(total);
	}
	

	public String getFormatedTranslateFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			
			sum += invoid.getTranslateFee()==null?0.0:invoid.getTranslateFee();
		}
		return feeFormat.format(sum);
	}
	
	public Integer getTranslatedNumber() {
		
		logger.debug("reqID: " + this.getId() + " has this.invoices.size(): " + this.invoices.size());
		
		
		Integer sum = 0;
		
		
		for(Invoice invoid : this.invoices) {
			logger.debug("invoice id#: " + invoid.getId());
			if(invoid.getTranslateItem() != null) {
				sum += invoid.getTranslateItem();
			}
		}

		logger.debug("getTranslatedNumber: " + sum);
		
		return sum;
	}
	
	public Double getTranlatedFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getTranslateFee() != null) {
				sum += invoid.getTranslateFee();
			}
		}
		return sum;
	}
	
	public String getFormatedCopyFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCopyFee() != null) {
				sum += invoid.getCopyFee();
			}
		}
		return feeFormat.format(sum);
	}
	
	public Integer getCopyNumber() {
		Integer sum = 0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCopyItem() != null) {
				sum += invoid.getCopyItem();
			}
		}
		return sum;
	}
	
	public Double getCopyFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCopyFee() != null) {
				sum += invoid.getCopyFee();
			}
		}
		return sum;
	}

	public Integer getCoaNumber() {
		Integer sum = 0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCoaItem() != null) {
				sum += invoid.getCoaItem();
			}
		}
		return sum;
	}
	
	public String getFormatedCoaFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCoaFee() != null) {
				sum += invoid.getCoaFee();
			}
		}
		return feeFormat.format(sum);
	}
	
	public Double getCoaFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getCoaFee() != null) {
				sum += invoid.getCoaFee();
			}
		}
		return sum;
	}
	
	public Double getTotalFee() {
		Double total = 0.0;
		total = this.getTotalReqExampleFee();
		if(this.speed == JobPriority.EXPRESS){
			total = total * 3;
		}
		Double sumTranslated = 0.0;
		Double sumCopy = 0.0;
		Double sumEtc = 0.0;
		Double sumCoa = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getTranslateFee() != null) {
				sumTranslated += invoid.getTranslateFee();
			}
			if(invoid.getCopyFee() != null) {
				sumCopy += invoid.getCopyFee();
			}
			if(invoid.getEtcFee() != null) {
				sumEtc += invoid.getEtcFee();
			}
			if(invoid.getCoaFee() != null) {
				sumCoa += invoid.getCoaFee();
			}
		}
		total = total + sumTranslated + sumCopy + sumEtc + sumCoa;

		return total;
	}
	
	public String getTotalFormatedFee() {
				
		return feeFormat.format(getTotalFee());
		
	}
	
	public String getEtcFeeString() {
		String feeString = "";
		for(Invoice invoid : this.invoices) {
			if(invoid.getEtc() != null && invoid.getEtc().length() >0 ){ 
				feeString += invoid.getEtc() + " ";
			}
		}
		
		return feeString;
	}
	
	public Double getEtcFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getEtcFee() != null) {
				sum += invoid.getEtcFee();
			}
		}
		return sum;
	}
	
	public String getFormatedEtcFee() {
		Double sum = 0.0;
		for(Invoice invoid : this.invoices) {
			if(invoid.getEtcFee() != null) {
				sum += invoid.getEtcFee();
			}
		}
		return feeFormat.format(sum);
	}
	
	
	public Integer getTotalNumberOfReqExample() {
		if(this.samples == null) return 0;
		logger.debug("getTotalNumberOfReqExample...");
		
		return this.samples.size();
		//return 0;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	
	
	
}
