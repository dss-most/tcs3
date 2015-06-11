package tcs3.model.lab;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import tcs3.model.hrx.Organization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="TEST_METHOD")
@SequenceGenerator(name="TEST_METHOD_SEQ", sequenceName="TEST_METHOD_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=TestMethod.class)
public class TestMethod implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815111759252438011L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="TEST_METHOD_SEQ")
	@Column(name="TEST_METHOD_ID")
	private Long id;
	
	@Column(name="NAME_TH")
	private String nameTh;
	
	@Column(name="NAME_EN") 
	private String nameEn;
	
	@Column(name="FEE")
	private Double fee;
	
	@Column(name="PREPARE_FEE")
	private Double preparationFee;
	
	@Column(name="TEST_METHOD_CODE")
	private String code;
	
	@Column(name="REMARK")
	private String remark;
	
	@ManyToOne
	@JoinColumn(name="DEPT_ID")
	private Organization owner;
	
	@Column(name="CER17025")
	private String cer17025;

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

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getPreparationFee() {
		return preparationFee;
	}

	public void setPreparationFee(Double preparationFee) {
		this.preparationFee = preparationFee;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public String getCer17025() {
		return cer17025;
	}

	public void setCer17025(String cer17025) {
		this.cer17025 = cer17025;
	}
	
	
	
}
