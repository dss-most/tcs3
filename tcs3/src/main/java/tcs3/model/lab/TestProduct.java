package tcs3.model.lab;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="LAB_KEYWORD")
@SequenceGenerator(name="LAB_KEYWORD_SEQ", sequenceName="LAB_KEYWORD_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=TestProduct.class)
public class TestProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276981545436343493L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="LAB_KEYWORD_SEQ")
	@Column(name="KEYWORD_ID")
	private Long id;

	@Column(name="KEYWORD")
	private String keyword;

	@OneToMany
	@JoinTable(
            name="test_method_lab_keyword",
            joinColumns=@JoinColumn(name="keyword_id"),
            inverseJoinColumns=@JoinColumn(name="test_method_id")
    )
	public List<TestMethod> methods;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public List<TestMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<TestMethod> methods) {
		this.methods = methods;
	}
	
	
	
}
