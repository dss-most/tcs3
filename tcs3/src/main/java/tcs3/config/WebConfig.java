package tcs3.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import tcs3.auth.service.ActiveUserHandlerMethodArgumentResolver;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
	}
	
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	     argumentResolvers.add(new ActiveUserHandlerMethodArgumentResolver());
	  }

	 @Bean
	 public ObjectMapper jacksonObjectMapper() {
		Hibernate4Module hibernate4Module = new Hibernate4Module();
		hibernate4Module.enable(
				Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
		 
		 ObjectMapper mapper = new ObjectMapper();
		 mapper.findAndRegisterModules();
		 mapper.registerModule(hibernate4Module);
		 
		 return mapper;
	 }
	
	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setObjectMapper(objectMapper);
        return converter;

    }


	 @Override
	 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		 converters.add(jacksonConverter(jacksonObjectMapper()));
		 converters.add(stringConverter());
	 }
    
    @Bean
    public StringHttpMessageConverter stringConverter() {
    return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
}
