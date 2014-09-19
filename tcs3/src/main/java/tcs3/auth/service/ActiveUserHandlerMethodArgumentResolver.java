package tcs3.auth.service;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.SecurityUser;
import tcs3.controller.HomeController;

public class ActiveUserHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	public static Logger logger = LoggerFactory.getLogger(HomeController.class);

	
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		if(parameter.hasParameterAnnotation(Activeuser.class)) {
			
			Principal principal = webRequest.getUserPrincipal();
			
			logger.debug("principal: " + principal.toString() + " :" + principal.getName());
			
			return (SecurityUser) ((UsernamePasswordAuthenticationToken) (principal)).getPrincipal();
			
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Activeuser.class);
	}

}
