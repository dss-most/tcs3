package tcs3.config;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

import tcs3.auth.service.ActiveUserHandlerMethodArgumentResolver;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
		
		 registry.addResourceHandler("/webjars/**")
		  	.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	     argumentResolvers.add(new ActiveUserHandlerMethodArgumentResolver());
	  }

	 public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
	        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

	        ObjectMapper mapper = new ObjectMapper();
	        
	        Hibernate5Module hm = new Hibernate5Module();
	        hm.enable(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
	        
	        // Registering Hibernate4Module to support lazy objects
	        mapper.registerModule(hm);

	        // Register default dateformat
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			mapper.setDateFormat(sdf);
			
			messageConverter.setObjectMapper(mapper);
	        return messageConverter;

	    }
	
  
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //Here we add our custom-configured HttpMessageConverter
        converters.add(jacksonMessageConverter());
        super.configureMessageConverters(converters);
    }
    
    @Bean
    public StringHttpMessageConverter stringConverter() {
    	return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
}
