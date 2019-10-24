package com.thoa.common.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	//负责加载service dao
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {AppRootConfig.class, AppShiroConfig.class};
	}

	//加载servlet
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {AppServletConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"*.do"};
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		registerContextLoaderListener(servletContext);
		registerFilter(servletContext);
		registerDispatcherServlet(servletContext);
	}

	private void registerFilter(ServletContext servletContext) {
		//注册filter对象
		FilterRegistration.Dynamic dy = servletContext.addFilter("filterProxy", 
				DelegatingFilterProxy.class);
		dy.setInitParameter("targetBeanName", "shiroFilterFactoryBean");
		dy.addMappingForUrlPatterns(null, false, "/*");
	}
	
}
