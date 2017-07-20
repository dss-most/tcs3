package tcs3.model.lab;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="REQ_EXAMPLE_12")
@PrimaryKeyJoinColumn(name = "REQ_EXAMPLE_ID")
@DiscriminatorValue(value="REQ_EX12")
public class RequestSample12 extends RequestSample {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3993462036280840519L;
	
//	@Basic
//	@Column(name="BRAND_TH")
//	private String brandTh;
//	
//
//	@Basic
//	@Column(name="BRAND_EN")
//	private String brandEn;
	

	@Basic
	@Column(name="CONTAINER_TH")
	private String containerTh;
	

	@Basic
	@Column(name="CONTAINER_EN")
	private String containerEn;


//	public String getBrandTh() {
//		return brandTh;
//	}
//
//
//	public void setBrandTh(String brandTh) {
//		this.brandTh = brandTh;
//	}
//
//
//	public String getBrandEn() {
//		return brandEn;
//	}
//
//
//	public void setBrandEn(String brandEn) {
//		this.brandEn = brandEn;
//	}


	public String getContainerTh() {
		return containerTh;
	}


	public void setContainerTh(String containerTh) {
		this.containerTh = containerTh;
	}


	public String getContainerEn() {
		return containerEn;
	}


	public void setContainerEn(String containerEn) {
		this.containerEn = containerEn;
	}

	
	
}
