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
	@Column(name="ADDRESS_NO")
	private String addressNo;
	@Basic 
	@Column(name="STREET")
	private String street;
	
	@Basic
	@Column(name="SOI")
	private String soi;
	
	@Basic
	@Column(name="BUILDING")
	private String building;
	
	@Basic
	@Column(name="TUMBON")
	private String tumbon;
	
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

	public String getAddressNo() {
		return addressNo;
	}

	public void setAddressNo(String addressNo) {
		this.addressNo = addressNo;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSoi() {
		return soi;
	}

	public void setSoi(String soi) {
		this.soi = soi;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getLine1FromOldAddress(){
		String str = "";
		if(this.addressNo != null && !this.addressNo.trim().equals("-")) {
			str += this.addressNo;
		}
		
		if(this.building != null && this.building.length() > 1) {
			if((str + "  " +this.building).length() > 25) return str; 
			str += "  " + this.building;
		}
		
		if(this.street != null && this.street.length() > 1  ) {
			if((str + "  ถ. " + this.street).length() > 25) return str;
			str += "  ถ. " + this.street;
		}
		
		if(this.soi != null && this.soi.length() > 0) {
			if((str + "  ซ. " + this.soi).length() > 25) return str;
			str += "  ซ. " + this.soi;
		}
		
		if(this.tumbon != null && this.tumbon.length() > 0) {
			if((str + "  " + this.tumbon).length() > 25) return str;
			str += "  " + this.tumbon;
		}
		
		return str.trim();
	}
	
	public String getLine2FromOldAddress(){
		String str = "";
		Boolean found = false;
		if(this.addressNo != null && !this.addressNo.trim().equals("-")) {
			str += this.addressNo;
		}
		
		if(this.building != null && this.building.length() > 1) {
			if((str + "  " +this.building).length() > 25 && found == false) {
				str=this.building; 
				found = true;
			} else {
				str += "  " + this.building;
			}
		}
		
		if(this.street != null && this.street.length() > 1  ) {
			if((str + "  ถ. " + this.street).length() > 25 && found == false) {
				str = "ถ. " + this.street;
				found = true;
			} else {
				str += "  ถ. " + this.street;
			}
		}
		
		if(this.soi != null && this.soi.length() >0) {
			if((str + "  ซ. " + this.soi).length() > 25 && found == false) {
				str = "ซ. " + this.soi;
				found = true;
			} else {
				str += "  ซ. " + this.soi;
			}
		}
		
		if(this.tumbon != null && this.tumbon.length()>0 ) {
			if((str + "  " + this.tumbon).length() > 25 && found == false ) {
				str = this.tumbon;
			} else {
				str += "  " + this.tumbon;
			}
		}
		
		return str.trim();
	}
	
}
