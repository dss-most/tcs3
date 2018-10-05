package tcs3.model.lab;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.hrx.Organization;

@Entity
@Table(name="QUOTATION_TEMPLATE")
@SequenceGenerator(name="QUOTATION_TEMPLATE_SEQ", sequenceName="QUOTATION_TEMPLATE_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=QuotationTemplate.class)
public class QuotationTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2094374886913675349L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="QUOTATION_TEMPLATE_SEQ")
	@Column(name="ID")
	private Long id;
	
	// กอง
	@ManyToOne
	@JoinColumn(name="MAIN_ORG_ID")
	private Organization mainOrg;
	
	// กลุ่มงาน/ห้องปฏิบัติการ
	@ManyToOne
	@JoinColumn(name="MAIN_GROUP_ID")
	private Organization groupOrg;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ABBR")
	private String code;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="STANDARDREF")
	private String StandardRef;
	
	@Column(name="SAMPLE_NOTE")
	private String sampleNote;
	
	@Column(name="SAMPLE_PREP")
	private String samplePrep;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="quotationTemplate", cascade=CascadeType.ALL)
	@OrderColumn(name="TESTMETHOD_INDEX")
	private List<TestMethodQuotationTemplateItem> testMethodItems;
	
	@ManyToOne
	@JoinColumn(name="EXAMPLE_ID")
	private SampleType sampleType;
	
	@Column(name="IS_ACTIVE")
	private Boolean isActive;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Organization getMainOrg() {
		return mainOrg;
	}

	public void setMainOrg(Organization mainOrg) {
		this.mainOrg = mainOrg;
	}

	public Organization getGroupOrg() {
		return groupOrg;
	}

	public void setGroupOrg(Organization groupOrg) {
		this.groupOrg = groupOrg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSampleNote() {
		return sampleNote;
	}

	public void setSampleNote(String sampleNote) {
		this.sampleNote = sampleNote;
	}

	public String getSamplePrep() {
		return samplePrep;
	}

	public void setSamplePrep(String samplePrep) {
		this.samplePrep = samplePrep;
	}

	public List<TestMethodQuotationTemplateItem> getTestMethodItems() {
		return testMethodItems;
	}

	public void setTestMethodItems(
			List<TestMethodQuotationTemplateItem> testMethodItems) {
		this.testMethodItems = testMethodItems;
	}

	public SampleType getSampleType() {
		return sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	public String getStandardRef() {
		return StandardRef;
	}

	public void setStandardRef(String standardRef) {
		StandardRef = standardRef;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
	
	
}
