package tcs3.auth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AUTHORITIES")
public class DssRole implements Role, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2252783982656498251L;

	@Id
	@Column(name="GID")
	private Long id;
	
	@Column(name="G_NAME")
	private String name;
	
	@Column(name="G_DESCRIPTION")
	private String desc;
	
	@Column(name="AUTH_TYPE")
	private String type;
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DssRole(String name) {
		this.name = name;
	}
	
	public DssRole() {
		this.name = null;
	}

}
