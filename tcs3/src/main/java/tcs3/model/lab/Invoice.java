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
	
	@Basic
	@Column(name="TRANSLATE_ITEM")
	private Integer translateItem;
	
	@Basic
	@Column(name="TRANSLATE_FEE")
	private Integer translateFEE;
	
	@Basic
	@Column(name="COA_ITEM")
	private Integer coaItem;
	
	@Basic
	@Column(name="COA_FEE")
	private Integer coaFee;
	
	@Basic
	@Column(name="COPY_ITEM")
	private Integer copyItem;
	
	@Basic
	@Column(name="COPY_FEE")
	private Integer copyFee;

	
	@Basic
	@Column(name="ETC")
	private String etc;
	
	@Basic
	@Column(name="ETC_FEE")
	private Integer etcFee;

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

	public Integer getTranslateFEE() {
		return translateFEE;
	}

	public void setTranslateFEE(Integer translateFEE) {
		this.translateFEE = translateFEE;
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

	
	
	
}

