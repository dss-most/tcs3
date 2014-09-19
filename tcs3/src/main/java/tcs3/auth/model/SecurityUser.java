package tcs3.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6024145586906819420L;
	
	private String userName;
	private String password;
	private DssUser dssUser;
	private Boolean enabled = false;
	private Boolean accountNonExpired = false;
	private Boolean accountNonLocked = false;
	private Boolean credentialsNonExpired = false;
	private Set<Role> roles; 
	
	
	public SecurityUser() {
		this.userName = null;
		this.password = null;
		this.roles = null;
		this.dssUser = null;
	}
	
	public SecurityUser(UsernamePasswordAuthenticationToken token) {
		this.userName = token.getName();
		this.password = null;
		
		Set<Role> roles = new HashSet<Role>();
		for(GrantedAuthority authority: token.getAuthorities()) {
			DssRole role = new DssRole(authority.getAuthority());
			
			roles.add(role);
		}
		
		this.roles = roles;
		
		this.enabled = true;
		this.accountNonExpired = true;
		this.credentialsNonExpired=true;
		this.accountNonLocked = true;
	}

	public SecurityUser(DssUser dssUser) {
		if(dssUser != null) {
			if(dssUser.getEnabled() != null && dssUser.getEnabled().equals(1)) {
				this.enabled = true;
			}
		}
		this.userName = dssUser.getUsername();
		this.password = dssUser.getPassword();
		this.roles = dssUser.getRoles();
		
		this.dssUser = dssUser;
				
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		
		for(Role role : this.roles) {
			
			grantedAuths.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return grantedAuths;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public DssUser getDssUser() {
		return dssUser;
	}
}
