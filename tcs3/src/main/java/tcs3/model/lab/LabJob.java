package tcs3.model.lab;

import java.io.Serializable;
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
	
	
	
}
