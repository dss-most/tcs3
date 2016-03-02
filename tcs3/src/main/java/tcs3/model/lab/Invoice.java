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

	
}

