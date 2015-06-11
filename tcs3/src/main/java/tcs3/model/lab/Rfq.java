package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="Rfq")
@SequenceGenerator(name="RFQ_SEQ", sequenceName="RFQ_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Rfq.class)
public class Rfq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1324101129326067488L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="RFQ_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="customer_name")
	private String customerName;
	
	@Basic
	@Column(name="company_name")
	private String companyName;
	
	@Basic
	@Column(name="address1")
	private String address1;
	
	@Basic
	@Column(name="address2")
	private String address2;
	
	@Basic
	@Column(name="telephone")
	private String telephone;
	
	@Basic
	@Column(name="fax")
	private String fax;
	
	@Basic
	@Column(name="email")
	private String email;
	
	@Basic
	@Column(name="sample_Desc")
	private String sampleDesc;
	
	@Basic
	@Column(name="has_Sent_Before")
	private Boolean hasSentBefore;
	
	@Basic
	@Column(name="reference")
	private String reference;
	
	@Lob
	@Column(name="test_Method")
	private String testMethod;
	
	@Lob
	@Column(name="remarks")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Rfq_date")
	private Date RfqDate;
	
	@Basic
	@Column(name="Status")
	private String Status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSampleDesc() {
		return sampleDesc;
	}

	public void setSampleDesc(String sampleDesc) {
		this.sampleDesc = sampleDesc;
	}

	public Boolean getHasSentBefore() {
		return hasSentBefore;
	}

	public void setHasSentBefore(Boolean hasSentBefore) {
		this.hasSentBefore = hasSentBefore;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public Date getRfqDate() {
		return RfqDate;
	}

	public void setRfqDate(Date rfqDate) {
		RfqDate = rfqDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	
	
	
}
