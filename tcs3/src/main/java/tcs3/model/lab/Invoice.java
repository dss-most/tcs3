package tcs3.model.lab;

import java.io.Serializable;

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="INVOID")
@SequenceGenerator(name="INVOID_SEQ", sequenceName="INVOID_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Invoice.class)
public class Invoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6501971416199528966L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="INVOID_SEQ")
	@Column(name="INVOID_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="REQ_ID")
	private Request request;
	
	// จำนวนการแปล
	@Basic
	@Column(name="TRANSLATE_ITEM")
	private Integer translateItem;
	
	// ค่าแปล 
	@Basic
	@Column(name="TRANSLATE_FEE")
	private Integer translateFee;
	
	// จำนวนใบรับรอง
	@Basic
	@Column(name="COA_ITEM")
	private Integer coaItem;
	
	// ค่าใบรับรอง
	@Basic
	@Column(name="COA_FEE")
	private Integer coaFee;
	
	// จำนวนสำเนา
	@Basic
	@Column(name="COPY_ITEM")
	private Integer copyItem;
	
	// ค่าสำเนา
	@Basic
	@Column(name="COPY_FEE")
	private Integer copyFee;

	// ค่าเตรียมตัวอย่าง (ไม่ใช้แล้ว)
	@Basic
	@Column(name="PREPARE_FEE")
	private Integer prepareFee = 0;
	
	// ไม่ทราบค่าอะไร (ไม่ใช้แล้ว)
	@Basic
	@Column(name="VOUCHER_FEE")
	private Integer voucherFee = 0;
	
	// ไม่ทราบค่าอะไร (ไม่ใช้แล้ว)
	@Basic
	@Column(name="OWE_FEE")
	private Integer oweFee;
	
	// ค่าอื่นๆ (รายละเอียด)
	@Basic
	@Column(name="ETC")
	private String etc;
	
	// ค่าอื่นๆ เป็นเงิน
	@Basic
	@Column(name="ETC_FEE")
	private Integer etcFee;
	
	

	public Invoice() {
		super();
		this.coaFee=0;
		this.coaItem=0;
		this.copyFee=0;
		this.copyItem=0;
		this.etcFee=0;
		this.translateFee=0;
		this.translateItem=0;
		this.oweFee=0;
		this.voucherFee=0;
		this.prepareFee=0;
	}

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

	public Integer getTranslateItem() {
		return translateItem;
	}

	public void setTranslateItem(Integer translateItem) {
		this.translateItem = translateItem;
	}

	public Integer getTranslateFee() {
		return translateFee;
	}

	public void setTranslateFee(Integer translateFee) {
		this.translateFee = translateFee;
	}

	public Integer getCoaItem() {
		return coaItem;
	}

	public void setCoaItem(Integer coaItem) {
		this.coaItem = coaItem;
	}

	public Integer getCoaFee() {
		return coaFee;
	}

	public void setCoaFee(Integer coaFee) {
		this.coaFee = coaFee;
	}

	public Integer getCopyItem() {
		return copyItem;
	}

	public void setCopyItem(Integer copyItem) {
		this.copyItem = copyItem;
	}

	public Integer getCopyFee() {
		return copyFee;
	}

	public void setCopyFee(Integer copyFee) {
		this.copyFee = copyFee;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public Integer getEtcFee() {
		return etcFee;
	}

	public void setEtcFee(Integer etcFee) {
		this.etcFee = etcFee;
	}

	public Integer getPrepareFee() {
		return prepareFee;
	}

	public void setPrepareFee(Integer prepareFee) {
		this.prepareFee = prepareFee;
	}

	public Integer getVoucherFee() {
		return voucherFee;
	}

	public void setVoucherFee(Integer voucherFee) {
		this.voucherFee = voucherFee;
	}

	public Integer getOweFee() {
		return oweFee;
	}

	public void setOweFee(Integer oweFee) {
		this.oweFee = oweFee;
	}

	
	
	
}

