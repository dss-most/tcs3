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
@Table(name="PROMOTION_DISCOUNT")
@SequenceGenerator(name="PROMOTION_DISCOUNT_SEQ", sequenceName="PROMOTION_DISCOUNT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Quotation.class)
public class PromotionDiscount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382251640596528821L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="PROMOTION_DISCOUNT_SEQ")
	@Column(name="ID")
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name="QUOTATION_ID")
	private Quotation quotation;
	
	@ManyToOne
	@JoinColumn(name="PROMOTION_ID")
	private Promotion promotion;
	
	@Basic
	@Column(name="discount")
	private Integer discount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
	
	
}
