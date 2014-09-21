package tcs3.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import tcs3.auth.model.DssUser;
import tcs3.auth.model.SecurityUser;

@Service
public class DssAuthenticationProvider implements AuthenticationProvider {

	public static Logger logger = LoggerFactory.getLogger(DssAuthenticationProvider.class);
	
	@Autowired
	private DssUserRepository dssUserRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		DssUser dssUser = dssUserRepository.findByUserNameAndPassword(userName, password);
		logger.debug("found dssUser: " + dssUser);
		if(dssUser != null) {
			
			SecurityUser secUser = new SecurityUser(dssUser);
			
			Authentication auth = new UsernamePasswordAuthenticationToken(secUser, password, secUser.getAuthorities());
			
			return auth;
		
		} 

		throw new BadCredentialsException("UserName and Password cannot be found!");
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		logger.debug("supports? : " + authentication.getCanonicalName());
		return (authentication.equals(UsernamePasswordAuthenticationToken.class));
	}

}
