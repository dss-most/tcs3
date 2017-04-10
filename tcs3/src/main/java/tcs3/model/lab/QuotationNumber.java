package tcs3.model.lab;

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

import tcs3.model.hrx.Organization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="Qutation_NO_SEQ")
@SequenceGenerator(name="QUOTATION_NO_SEQ_SEQ", sequenceName="QUOTATION_NO_SEQ_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=QuotationNumber.class)
public class QuotationNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -743934642879488175L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="QUOTATION_NO_SEQ_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="CURRENT_NO")	
	private Integer currentNumber;
	
	@Basic
	@Column(name="YEAR")
	private Integer year;
	
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization organization;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String generateQuotationNumber() {
		currentNumber = currentNumber + 1;
		
		String numberStr = String.format("%05d", currentNumber);
		String quotationNumber;
		
		if(organization.getCode().length() == 8) {
			quotationNumber = "Q"+year.toString().substring(2,4)+"/"+organization.getCode().substring(0, 2) +"/"+ numberStr;
		} else {
		
			quotationNumber = "Q"+year.toString().substring(2,4)+"/"+organization.getCode().substring(2, 5) +"/"+ numberStr;
		}
		return quotationNumber;
	}
	
}
