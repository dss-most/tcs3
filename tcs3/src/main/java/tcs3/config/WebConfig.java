package tcs3.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

import tcs3.auth.service.ActiveUserHandlerMethodArgumentResolver;

@Configuration
public class WebConfig {
	
    @Bean
    Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		final Hibernate5Module hm = new Hibernate5Module();
		hm.enable(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);	

		return builder -> builder
			//.featuresToEnable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			//.failOnUnknownProperties(false)
			.dateFormat(sdf)
			.modulesToInstall(hm);
	}
	
	// @Bean
	// MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {

	// 	MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

	// 	return converter;
	// }
	//  public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
	//         MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

	//         ObjectMapper mapper = new ObjectMapper();
	        
	//         Hibernate5Module hm = new Hibernate5Module();
	//         hm.enable(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
	        
	//         // Registering Hibernate4Module to support lazy objects
	//         mapper.registerModule(hm);

	//         // Register default dateformat
	//         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	// 		mapper.setDateFormat(sdf);

	// 		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	// 		System.err.println("xxxxx");
	// 		messageConverter.setObjectMapper(mapper);
	//         return messageConverter;

	//     }
	
  
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     //Here we add our custom-configured HttpMessageConverter
    //     converters.add(jacksonMessageConverter());
    //     super.configureMessageConverters(converters);
    // }
    
    // @Bean
    // public StringHttpMessageConverter stringConverter() {
    // 	return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    // }
}
