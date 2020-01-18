package tcs3.model.lab;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import tcs3.model.customer.Address;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name="LAB_ADDRESS")
@SequenceGenerator(name="LAB_ADDRESS_TCS3_SEQ", sequenceName="LAB_ADDRESS_TCS3_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=RequestAddress.class)
public class RequestAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -931592352771872128L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_ADDRESS_TCS3_SEQ")
	@Column(name="ADDRESS_ID")
	private Long id;
	
	@Basic
	@Column(name="ADDRESS")
	private String address;
	
	@Basic
	@Column(name="AMPHUR")
	private String amphur;
	
	@Basic
	@Column(name="PROVINCE")
	private String province;
	
	@Basic
	@Column(name="COUNTRY")
	private String country;
	
	@Basic
	@Column(name="POST_CODE")
	private String zipCode;
	
	@Basic
	@Column(name="PHONE")
	private String phone;
	
	@Basic
	@Column(name="FAX")
	private String fax;
	
	
	

	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getAddress() {
		return address;
	}




	public void setAddress(String address) {
		this.address = address;
	}




	public String getAmphur() {
		return amphur;
	}




	public void setAmphur(String amphur) {
		this.amphur = amphur;
	}




	public String getProvince() {
		return province;
	}




	public void setProvince(String province) {
		this.province = province;
	}




	public String getCountry() {
		return country;
	}




	public void setCountry(String country) {
		this.country = country;
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




	public static RequestAddress parseAddress(Address address) {
		RequestAddress requestAddress = new RequestAddress();
		if(address.getLine1() != null) {
			requestAddress.setAddress(address.getLine1() + " " + address.getLine2());
		} else {
			requestAddress.setAddress(address.getLine1FromOldAddress() + " " + address.getLine2FromOldAddress());
		} 
		requestAddress.setAmphur(address.getDistrict().getName());
		requestAddress.setCountry(null);
		requestAddress.setProvince(address.getProvince().getName());
		requestAddress.setPhone(address.getPhone());
		requestAddress.setFax(address.getFax());
		requestAddress.setZipCode(address.getZipCode());
		
		return requestAddress;
	}


	public void importFromAddressJson(JsonNode node) {
		if(node.path("line1").isNull()) {
			this.setAddress(node.path("line1FromOldAddress").asText() + " " + node.path("line2FromOldAddress").asText());
		} else {
			this.setAddress(node.path("line1").asText() + " " + node.path("line2").asText());
		} 
		this.setAmphur(node.path("district").path("name").asText());
		this.setCountry(node.path("country").asText());
		this.setFax(node.path("fax").asText());
		this.setPhone(node.path("phone").asText());
		this.setProvince(node.path("province").path("name").asText());
		this.setZipCode(node.path("zipCode").asText());
		
	}

	public void importFromJson(JsonNode node) {
		this.setAddress(node.path("address").asText());
		this.setAmphur(node.path("amphur").asText());
		this.setCountry(node.path("country").asText());
		this.setFax(node.path("fax").asText());
		this.setPhone(node.path("phone").asText());
		this.setProvince(node.path("province").asText());
		this.setZipCode(node.path("zipCode").asText());
		
	}
	
}
