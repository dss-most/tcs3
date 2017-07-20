package tcs3.model.fin;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import tcs3.model.hrx.Officer;

@Entity
@Table(name="FIN_INVOICE_DETAIL")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Officer.class)
@SequenceGenerator(name="FIN_INVOICE_DETAIL_SEQ", sequenceName="DSS.FIN_INVOICE_DETAIL_SEQ", allocationSize=1)
public class InvoiceDetail implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5919563945987508547L;


	@Id
	@Column(name="INV_DET_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="FIN_INVOICE_DETAIL_SEQ")
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name="INVOICE_ID")
	private Invoice invoice;
	
	@Basic
	@Column(name="TYPE_CODE")
	private String typeCode;
	
	@Basic
	@Column(name="DET_CODE")
	private String detailCode;
	
	@Basic
	@Column(name="DET_DESC")
	private String detailDescription;
	
	@Basic
	@Column(name="QTY")
	private Integer quantity;
	
	@Basic
	@Column(name="AMT")
	private Double amount;
	
	@Basic
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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
	
	
	
}
