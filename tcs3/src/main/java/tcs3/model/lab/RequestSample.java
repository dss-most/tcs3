package tcs3.model.lab;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="req_example")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="req_example_SEQ", sequenceName="req_example_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Request.class)
public abstract class RequestSample implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9184084528183535668L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="req_example_SEQ")
	@Column(name="REQ_EXAMPLE_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="REQ_ID")
	private Request request;
	
	@OneToMany(mappedBy="sample", cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderColumn(name="JOB_INDEX")
	private List<LabJob> jobs;
	
	@Column(name="LAB_NO")
	private String labNo;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="NAME_EN")
	private String nameEn;
	
	@Column(name="BRAND")
	private String brand;
	
	@Column(name="BRAND_EN")
	private String brandEn;
	
	@Column(name="ITEM")
	private Integer item;
	
	
	@Column(name="SEND_STATUS")
	private Integer sendStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ESTIMATED_REPORT_DATE")
	private Date estimatedReportDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getLabNo() {
		return labNo;
	}

	public void setLabNo(String labNo) {
		this.labNo = labNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandEn() {
		return brandEn;
	}

	public void setBrandEn(String brandEn) {
		this.brandEn = brandEn;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Date getEstimatedReportDate() {
		return estimatedReportDate;
	}

	public void setEstimatedReportDate(Date estimatedReportDate) {
		this.estimatedReportDate = estimatedReportDate;
	}

	public List<LabJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<LabJob> jobs) {
		this.jobs = jobs;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	
	
	
}
