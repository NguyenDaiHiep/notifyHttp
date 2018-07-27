package ndhiep.tma.com.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import ndhiep.tma.com.listener.Config;;

public class SpringInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		 AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
	        appContext.register(Config.class);
	        // servletContext.addListener(new ApplicationContextListener());
	        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher", new DispatcherServlet(appContext));
	        dispatcher.setLoadOnStartup(1);
	        dispatcher.addMapping("/");
		
	}

}
