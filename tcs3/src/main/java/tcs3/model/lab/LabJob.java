package tcs3.model.lab;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.hrx.Organization;

@Entity
@Table(name="LAB_JOB")
@SequenceGenerator(name="lab_job_seq", sequenceName="lab_job_seq", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Request.class)
public class LabJob implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4092882044949404249L;
	
	private static final NumberFormat feeFormat = new DecimalFormat("#,##0.00");
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="lab_job_seq")
	@Column(name="JOB_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="REQ_EXAMPLE_ID")
	private RequestSample sample;
	
	@ManyToOne
	@JoinColumn(name="TEST_METHOD_ID")
	private TestMethod testMethod;
	
	@Column(name="FEE")
	private Integer fee;
	
	@Basic 
	@Column(name="IS_ACTIVE")
	private Boolean active;
	
	@Basic
	@Column(name="GROUP_ITEM")
	private Integer groupItem=0;
	
	
	@Column(name="IS_ACKNOWLEDGE")
	private Integer isAcknowledge=0;
	
	@Basic 
	@Column(name="QUANTITY")
	private Integer quantity;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="ORG_ID")
	private Organization org;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="GROUP_ID")
	private Organization group;
	
	@Convert(converter=LabJobStatusConverter.class)
	@Column(name="STATUS")
	private LabJobStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequestSample getSample() {
		return sample;
	}

	public void setSample(RequestSample sample) {
		this.sample = sample;
	}

	public TestMethod getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(TestMethod testMethod) {
		this.testMethod = testMethod;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public Organization getGroup() {
		return group;
	}

	public void setGroup(Organization group) {
		this.group = group;
	}

	public LabJobStatus getStatus() {
		return status;
	}

	public void setStatus(LabJobStatus status) {
		this.status = status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getGroupItem() {
		return groupItem;
	}

	public void setGroupItem(Integer groupItem) {
		this.groupItem = groupItem;
	}

	public Integer getIsAcknowledge() {
		return isAcknowledge;
	}

	public void setIsAcknowledge(Integer isAcknowledge) {
		this.isAcknowledge = isAcknowledge;
	}
	
	
	public String getFormatedFee() {
		return feeFormat.format(this.fee);
	}
	
	
}
