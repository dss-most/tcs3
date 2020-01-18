package tcs3.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;

/**
 * 
 * @author dbuak
 *
 * some officer hase user_id that has no corresponing with users table
 * update organization_persons set user_id = null where person_id in (select person_id from organization_persons where user_id not in (select user_id from users));
 */


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
