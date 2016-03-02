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
@Table(name="REQUEST_PROMOTION_DISCOUNT")
@SequenceGenerator(name="REQUEST_PROMOTION_DISCOUNT_SEQ", sequenceName="REQUEST_PROMOTION_DISCOUNT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=RequestPromotionDiscount.class)
public class RequestPromotionDiscount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382251640596528821L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="REQUEST_PROMOTION_DISCOUNT_SEQ")
	@Column(name="ID")
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name="REQUEST_ID")
	private Request request;
	
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


	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
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
