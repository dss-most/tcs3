package tcs3.auth.model;

import java.util.Set;

public interface User {
	public String getUsername();
	public Set<Role> getRoles();
}
