package tcs3.auth.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import tcs3.model.hrx.Officer;

@Entity
@Table(name="USERS")
public class DssUser implements User, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 291788593559734097L;

	@Id
	@Column(name="USER_ID")
	private Long id;
	
	@Column(name="USERNAME")
	private String userName;
	
	@Column(name="USER_PASSWORD")
	private String password;

	@Column(name="GID")
	private Integer gid;
	
	@Column(name="ENABLED")
	private Integer enabled;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable ( 
		name="ROLE_MEMBERSHIP",
		joinColumns={ @JoinColumn(name="USER_ID", referencedColumnName="USER_ID") },
	    inverseJoinColumns={ @JoinColumn(name="GID", referencedColumnName="GID", unique=true) } 
	)
	private Set<DssRole> dssRoles;
	
	@OneToOne(fetch=FetchType.EAGER, mappedBy="dssUser")
	private Officer officer;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(Set<DssRole> roles) {
		this.dssRoles = roles;
	}
	
	public Set<DssRole> getDssRoles() {
		return dssRoles;
	}

	public void setDssRoles(Set<DssRole> dssRoles) {
		this.dssRoles = dssRoles;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public Set<Role> getRoles() {
		Set<Role> returnSet = new HashSet<Role>();
		for(DssRole role : this.dssRoles) {
			returnSet.add(role);
		}
		return returnSet;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Officer getOfficer() {
		return officer;
	}

	public void setOfficer(Officer officer) {
		this.officer = officer;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}
	
	

	
}
