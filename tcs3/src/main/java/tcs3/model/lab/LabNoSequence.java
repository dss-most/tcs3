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
@Table(name="LAB_SEQ")
@SequenceGenerator(name="LAB_SEQ_SEQ", sequenceName="LAB_SEQ_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Request.class)
public class LabNoSequence implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4254181991844598760L;


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_SEQ_SEQ")
	@Column(name="SEQ_ID")
	private Long id;

	
	@Column(name="MAX_NUMBER")
	private Integer maxNumber;
	
	@Basic 
	@Column(name="NAME_SEQ")
	private String name;
	
	@Basic 
	@Column(name="YEAR_SEQ")
	private Integer year;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	

}
