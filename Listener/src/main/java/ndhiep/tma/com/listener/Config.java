package ndhiep.tma.com.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan("ndhiep.tma.com.listener")
@PropertySource("classpath:database.properties")
public class Config {
	
	  @Bean
	    public UrlBasedViewResolver setupViewResolver() {
	        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	        resolver.setPrefix("/WEB-INF/views/");
	        resolver.setSuffix(".jsp");
	        resolver.setViewClass(JstlView.class);
	        return resolver;
	    }

}
