package tcs3.model.fin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.hrx.Officer;

@Entity(name = "FinInvoice")
@Table(name="FIN_INVOICE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Officer.class)
@SequenceGenerator(name="FIN_INVOICE_SEQ", sequenceName="DSS.FIN_INVOICE_SEQ", allocationSize=1)
public class Invoice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4883976351206907928L;

	@Id
	@Column(name="INVOICE_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="FIN_INVOICE_SEQ")
	private Long id;
	
	@Basic
	@Column(name="INVOICE_NO")
	private String invoiceNo;
	
	
	@OneToMany(mappedBy="invoice", fetch=FetchType.EAGER,
			cascade=CascadeType.ALL, orphanRemoval=true)
	private List<InvoiceDetail> details;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="CUSTOMER_ID")
	private InvoiceAddress invoiceAddress;
	
	@Basic
	@Column(name="QTY")
	private Integer quantity;
	
	@Basic
	@Column(name="TOTAL")
	private Double total;
	
	@Type(type="yes_no")
	@Column(name="HAS_DETAIL")
	private Boolean hasDetail;
	
	@Basic
	@Column(name="REMARK")
	private String remark;
	
	@Basic
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createdDate;
	
	// ไม่ได้ใช้งานแล้ว
	@Basic
	@Column(name="REF_CODE")
	private String refCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public InvoiceAddress getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(InvoiceAddress invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Boolean getHasDetail() {
		return hasDetail;
	}

	public void setHasDetail(Boolean hasDetail) {
		this.hasDetail = hasDetail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public List<InvoiceDetail> getDetails() {
		return details;
	}

	public void setDetails(List<InvoiceDetail> details) {
		this.details = details;
	}

}
