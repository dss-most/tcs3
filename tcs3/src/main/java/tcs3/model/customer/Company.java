package tcs3.model.customer;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="COMPANIES")
@SequenceGenerator(name="COMPANY_TCS3_SEQ", sequenceName="COMPANY_TCS3_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -131706119346065444L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="COMPANY_TCS3_SEQ")
	@Column(name="COMPANY_ID")
	private Long id;
	
	@Basic
	@Column(name="TH_NAME")
	private String nameTh;
	
	@Basic
	@Column(name="ENG_NAME")
	private String nameEn;
	
	@ManyToOne
	@JoinColumn(name="ADDRESS_ID")
	private Address oldAddress;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="COMPANY_ADDRESSES",
		joinColumns={ @JoinColumn(name="COMPANY_ID", referencedColumnName="COMPANY_ID") },
		inverseJoinColumns={ @JoinColumn(name="ADDRESS_ID", referencedColumnName="ADDRESS_ID", unique=true) } 
	)
	private Set<Address> addresses;
	
	@OneToMany(mappedBy="company", fetch=FetchType.EAGER)
	private Set<Customer> people;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameTh() {
		return nameTh;
	}

	public void setNameTh(String nameTh) {
		this.nameTh = nameTh;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<Customer> getPeople() {
		return people;
	}

	public void setPeople(Set<Customer> people) {
		this.people = people;
	}

	public Address getOldAddress() {
		return oldAddress;
	}

	public void setOldAddress(Address oldAddress) {
		this.oldAddress = oldAddress;
	}
	
}
