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
@SequenceGenerator(name="QUOTATION_SEQ", sequenceName="QUOTATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Quotation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2871550464164534982L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="QUOTATION_SEQ")
	@Column(name="ID")
	private Long id;
	
	// Extra from Quotation Template
	@ManyToOne
	@JoinColumn(name="COMPANY_ID")
	private Company company;
	
	@ManyToOne
	@JoinColumn(name="ADDRESS_ID")
	private Address address;
	
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private Customer contact;
	
	@Temporal(TemporalType.DATE)
	private Date quotationDate;
	
	@Basic
	private Integer estimatedDay;
	
	@Basic
	private String quotationNo;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY_PERSON_ID")
	private Officer createdBy;
	
	@Basic
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

	

	
}
