package tcs3.model.hrx;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="V_ORGANIZATION")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Organization.class)
public class Organization implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7194786922685582911L;

	@Id
	@Column(name="ORG_ID")
	private Long id;
	
	@Column(name="ORG_CODE")
	private String code;
	
	@Column(name="ORG_NAME")
	private String name;
	
	@Column(name="ORG_ABBR")
	private String abbr;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ORG_ORG_ID")
	private Organization parent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}


	
	
	
}
