package tcs3.model.lab;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="LAB_KEYWORD_CAT")
@SequenceGenerator(name="LAB_KEYWORD_CAT_SEQ", sequenceName="LAB_KEYWORD_CAT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=TestProductCategory.class)
public class TestProductCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276981545436343493L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_KEYWORD_CAT_SEQ")
	@Column(name="ID")
	private Long id;

	@Column(name="CODE")
	private String code;

	
	@Column(name="NAME_TH")
	private String nameTh;
	
	@Column(name="NAME_EN")
	private String nameEn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameTh() {
		return nameTh;
	}

	public void setNameTh(String nameTh) {
		this.nameTh = nameTh;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
}
