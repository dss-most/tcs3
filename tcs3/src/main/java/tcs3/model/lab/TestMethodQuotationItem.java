package tcs3.model.lab;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="TEST_METHOD_Q_ITEM")
@SequenceGenerator(name="TEST_METHOD_Q_ITEM_SEQ", sequenceName="TEST_METHOD_Q_ITEM_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=TestMethodQuotationItem.class)
public class TestMethodQuotationItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7345901928357094838L;


	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="TEST_METHOD_Q_ITEM_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TEST_METHOD_ID")
	private TestMethod testMethod;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="QUOTATION_ID")
	private Quotation quotation;
	
	@Basic
	@Column(name="FEE")
	private Double fee;
	
	@Basic
	@Column(name="REMARK")
	private String Remark;
	
	@Basic
	@Column(name="NAME")
	private String name;
	
	@Basic
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@Basic 
	@Column(name="isSubItem")
	private Boolean isSubItem;
	
	@Basic
	@Column(name="ROWNO")
	private Integer rowNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TestMethod getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(TestMethod testMethod) {
		this.testMethod = testMethod;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Boolean getIsSubItem() {
		return isSubItem;
	}

	public void setIsSubItem(Boolean isSubItem) {
		this.isSubItem = isSubItem;
	}


	

	
}

