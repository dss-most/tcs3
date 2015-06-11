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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="EXAMPLE")
@SequenceGenerator(name="HIBERNATE_SEQUENCE", sequenceName="HIBERNATE_SEQUENCE", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SampleType.class)
public class SampleType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7762546502190698685L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="HIBERNATE_SEQUENCE")
	@Column(name="EXAMPLE_ID")
	private Long id;
	
	@Basic
	@Column(name="FEE_TYPE_ID")
	private  Integer feeTypeId;
	
	@Basic
	@Column(name="NAME_TH")
	private String nameTh;
	
	@Basic
	@Column(name="NAME_EN")
	private String nameEn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(Integer feeTypeId) {
		this.feeTypeId = feeTypeId;
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
	
	
	
	
	
	
	
}
