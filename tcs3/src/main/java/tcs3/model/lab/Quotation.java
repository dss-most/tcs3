package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.customer.Customer;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;

@Entity
@Table(name="QUOTATION_TCS3")
@SequenceGenerator(name="QUOTATION_TCS3_SEQ", sequenceName="QUOTATION_TCS3_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Quotation.class)
public class Quotation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2871550464164534982L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="QUOTATION_TCS3_SEQ")
	@Column(name="ID")
	private Long id;
	
	// Extra from Quotation Template
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="COMPANY_ID")
	private Company company;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ADDRESS_ID")
	private Address address;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CUSTOMER_ID")
	private Customer contact;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTATIONDATE")
	private Date quotationDate;
	
	@Basic
	@Column(name="ESTIMATEDDAY")
	private Integer estimatedDay;
	
	@Basic
	@Column(name="QUOTATIONNO")
	private String quotationNo;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY_PERSON_ID")
	private Officer createdBy;
	
	@Basic
	@Column(name="CANCELFLAG")
	private String cancelFlag;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization mainOrg;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="MAIN_GROUP_ID")
	private Organization groupOrg;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ABBR")
	private String code;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="SAMPLE_NOTE")
	private String sampleNote;
	
	@Column(name="SAMPLE_PREP")
	private String samplePrep;
	
	@Column(name="SAMPLE_NUM")
	private Integer sampleNum;
	
	@Column(name="TRANSLATE_ITEM")
	private Integer translateNum;
	
	@Column(name="TRANSLATE_FEE")
	private Integer translateFee;
	
	@Column(name="COA_ITEM")
	private Integer coaNum;
	
	@Column(name="COA_FEE")
	private Integer coaFee;
	
	@Column(name="COPY_ITEM")
	private Integer copyNum;
	
	@Column(name="copy_FEE")
	private Integer copyFee;
	
	@Column(name="ETC")
	private String etc;
	
	@Column(name="ETC_FEE")
	private Integer etcFee;
	
	@ManyToOne
	@JoinColumn(name="EXAMPLE_ID")
	private SampleType sampleType;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="quotation", cascade=CascadeType.ALL)
	@OrderColumn(name="TESTMETHOD_INDEX")
	private List<TestMethodQuotationItem> testMethodItems;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSampleNote() {
		return sampleNote;
	}

	public void setSampleNote(String sampleNote) {
		this.sampleNote = sampleNote;
	}

	public String getSamplePrep() {
		return samplePrep;
	}

	public void setSamplePrep(String samplePrep) {
		this.samplePrep = samplePrep;
	}

	public List<TestMethodQuotationItem> getTestMethodItems() {
		return testMethodItems;
	}

	public void setTestMethodItems(
			List<TestMethodQuotationItem> testMethodItems) {
		this.testMethodItems = testMethodItems;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Customer getContact() {
		return contact;
	}

	public void setContact(Customer contact) {
		this.contact = contact;
	}

	public Date getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	public Integer getEstimatedDay() {
		return estimatedDay;
	}

	public void setEstimatedDay(Integer estimatedDay) {
		this.estimatedDay = estimatedDay;
	}

	public String getQuotationNo() {
		return quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
	}

	public Officer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Officer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	
	

	public SampleType getSampleType() {
		return sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}
	
	
	

	public Integer getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(Integer sampleNum) {
		this.sampleNum = sampleNum;
	}

	public Integer getTranslateNum() {
		return translateNum;
	}

	public void setTranslateNum(Integer translateNum) {
		this.translateNum = translateNum;
	}

	public Integer getTranslateFee() {
		return translateFee;
	}

	public void setTranslateFee(Integer translateFee) {
		this.translateFee = translateFee;
	}

	public Integer getCoaNum() {
		return coaNum;
	}

	public void setCoaNum(Integer coaNum) {
		this.coaNum = coaNum;
	}

	public Integer getCoaFee() {
		return coaFee;
	}

	public void setCoaFee(Integer coaFee) {
		this.coaFee = coaFee;
	}

	public Integer getCopyNum() {
		return copyNum;
	}

	public void setCopyNum(Integer copyNum) {
		this.copyNum = copyNum;
	}

	public Integer getCopyFee() {
		return copyFee;
	}

	public void setCopyFee(Integer copyFee) {
		this.copyFee = copyFee;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public Integer getEtcFee() {
		return etcFee;
	}

	public void setEtcFee(Integer etcFee) {
		this.etcFee = etcFee;
	}

	public void reCalculateTestMethodItemsRowNo() {
		int runningNo = 1;
		for(TestMethodQuotationItem item : testMethodItems) {
			if(item.getTestMethod() != null) {
				item.setRowNo(runningNo++);
			} else {
				item.setRowNo(null);
				item.setQuantity(0);
				item.setFee(0.0);
			}
		}
	}
	

	
}
