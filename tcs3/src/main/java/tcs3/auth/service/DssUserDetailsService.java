package tcs3.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;


public class DssUserDetailsService implements UserDetailsService{

	@Autowired
	public DssUserRepository dssUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		
		Officer officer = dssUserRepository.findOfficerByUserName(userName);
		
		if(officer != null && officer.getDssUser() != null) {
			SecurityUser secUser = new SecurityUser(officer.getDssUser());
			return secUser;
		}
		 
		throw new UsernameNotFoundException("UserName : "+userName+" not found");
	}
	
}
