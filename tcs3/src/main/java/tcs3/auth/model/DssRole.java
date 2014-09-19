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
	
	@Override
	public String getName() {
		return this.name;
	}

	public DssRole(String name) {
		this.name = name;
	}
	
	public DssRole() {
		this.name = null;
	}

}
