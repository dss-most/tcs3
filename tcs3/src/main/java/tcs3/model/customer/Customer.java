package tcs3.model.customer;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import tcs3.model.global.Sex;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="CUSTOMER_TCS3")
@SequenceGenerator(name="CUSTOMER_TCS3_SEQ", sequenceName="CUSTOMER_TCS3_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Customer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378623361709196484L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="CUSTOMER_TCS3_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="SEX")
	private Sex sex;
	
	@Basic
	@Column(name="TITLE")
	private String title;
	
	@Basic
	@Column(name="FIRSTNAME")
	private String firstName;

	@Basic
	@Column(name="LASTNAME")
	private String lastName;
	
	@Basic
	@Column(name="MIDDLENAME")
	private String middleName;
	
	@Basic
	@Column(name="email")
	private String email;
	
	@Basic
	@Column(name="MOBILEPHONE")
	private String mobilePhone;
	
	@Basic
	@Column(name="OFFICEPHONE")
	private String officePhone;
	
	@Basic
	@Column(name="FAX")
	private String fax;
	
	@ManyToOne()
	@JoinColumn(name="COMPANY_ID")
	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
}
