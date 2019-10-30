package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.hrx.Officer;

@Entity
@Table(name="LAB_REPORT_HISTORY")
@SequenceGenerator(name="LAB_REPORT_HISTORY_SEQ", sequenceName="LAB_REPORT_HISTORY_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=ReportHistory.class)
public class ReportHistory  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8498794940213455048L;


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_REPORT_HISTORY_SEQ")
	@Column(name="HIS_ID")
	private Long id;

	
	@Column(name="HISTORY")
	private String history;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="HIS_DATE")
	private Date timestamp;
	
	@ManyToOne
	@JoinColumn(name="PERSON_ID")
	private Officer createdBy;
	
	@ManyToOne
	@JoinColumn(name="REPORT_id")
	private Report report;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Officer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Officer createdBy) {
		this.createdBy = createdBy;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
}
