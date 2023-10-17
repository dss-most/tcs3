package tcs3.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import tcs3.auth.service.ActiveUserHandlerMethodArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
		
		 registry.addResourceHandler("/webjars/**")
		  	.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	     argumentResolvers.add(new ActiveUserHandlerMethodArgumentResolver());
	  }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

}
