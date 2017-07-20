package tcs3.model.fin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import tcs3.model.global.District;
import tcs3.model.global.Province;
import tcs3.model.hrx.Officer;

@Entity
@Table(name="DSS_CUSTOMER")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Officer.class)
@SequenceGenerator(name="DSS_CUSTOMER_SEQ", sequenceName="DSS.DSS_CUSTOMER_SEQ", allocationSize=1)
public class InvoiceAddress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -984613354507519172L;


	@Id
	@Column(name="CUSTOMER_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="DSS_CUSTOMER_SEQ")
	private Long id;
	
	@Basic
	@Column(name="CUSTOMER_CODE")
	private String customerCode;
	
	@Basic
	@Column(name="CUSTOMER_TYPE")
	private Integer customerType;
	

	@Basic
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Basic
	@Column(name="ADDRESS1")
	private String address1;
	
	@Basic
	@Column(name="ADDRESS2")
	private String address2;
	
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private Province province;
	
	@ManyToOne
	@JoinColumn(name="AMPHUR_ID")
	private District district;
	
	@Basic
	@Column(name="ZIP_CODE")
	private String zipCode;

	@Basic
	@Column(name="TEL")
	private String phone;
	
	@Basic
	@Column(name="FAX")
	private String fax;
	
	@Basic
	@Column(name="EMAIL")
	private String email;
	
	@Basic
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createdDate;
	
	@Basic
	@Column(name="LAST_UPDATED_BY")
	private Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdatedDate;
	
	@Convert(converter=InvoiceLangugeConverter.class)
	@Column(name="LANG_FLG")
	private InvoiceLanguage language;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public InvoiceLanguage getLanguage() {
		return language;
	}

	public void setLanguage(InvoiceLanguage language) {
		this.language = language;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

		
}
