package tcs3.model.hrx;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.auth.model.DssUser;

@Entity
@Table(name="ORGANIZATION_PERSONS")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Officer.class)
public class Officer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1623378571548378537L;

	@Id
	@Column(name="PERSON_ID")
	private Long id;
	
	@Column(name="USER_TITLE")
	private String title;
	
	@Column(name="USER_FIRSTNAME")
	private String firstName;
	
	@Column(name="USER_LASTNAME")
	private String LastName;
	
	@OneToOne
	@JoinColumn(name="USER_ID")
	private DssUser dssUser;
	
	@ManyToOne
	@JoinTable(
		name="OFFICERS",
		joinColumns={ @JoinColumn(name="PERSON_ID", referencedColumnName="PERSON_ID") },
		inverseJoinColumns={ @JoinColumn(name="DEP_ID", referencedColumnName="ORG_ID", unique=true) } 
	)
	private Organization workAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public Organization getWorkAt() {
		return workAt;
	}

	public void setWorkAt(Organization workAt) {
		this.workAt = workAt;
	}

	public DssUser getDssUser() {
		return dssUser;
	}

	public void setDssUser(DssUser dssUser) {
		this.dssUser = dssUser;
	}
	
	
}
