package tcs3.model.customer;

import java.io.Serializable;

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

import tcs3.model.global.District;
import tcs3.model.global.Province;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="ADDRESS")
@SequenceGenerator(name="ADDRESS_TCS3_SEQ", sequenceName="ADDRESS_TCS3_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -931592352771872128L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="ADDRESS_TCS3_SEQ")
	@Column(name="ADDRESS_ID")
	private Long id;
	
	@Basic
	@Column(name="line1")
	private String line1;
	
	@Basic
	@Column(name="line2")
	private String line2;
	
	@ManyToOne()
	@JoinColumn(name="PROVINCE")
	private Province province;
	
	@ManyToOne()
	@JoinColumn(name="AMPHUR")
	private District district;
	
	@Basic
	@Column(name="ZIPCODE")
	private String zipCode;
	
	@Basic
	@Column(name="PHONE_NO")
	private String phone;
	
	@Basic
	@Column(name="FAX_NO")
	private String fax;
	
	@Basic
	@Column(name="MOBILE_NO")
	private String mobilePhone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
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

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	
}
