package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PROMOTION")
@SequenceGenerator(name="PROMOTION_SEQ", sequenceName="PROMOTION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Quotation.class)
public class Promotion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7546489400691154953L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="PROMOTION_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;
	
	@Basic
	@Column(name="PERCENT_DISCOUNT")
	private Integer percentDiscount;
	
	@Basic
	@Column(name="DESCRIPTION")
	private String description;
	
	@Basic
	@Column(name="REMARK")
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getPercentDiscount() {
		return percentDiscount;
	}
	public void setPercentDiscount(Integer percentDiscount) {
		this.percentDiscount = percentDiscount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
