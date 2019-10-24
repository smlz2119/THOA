package com.thoa.common.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@ComponentScan(value = "com.thoa", includeFilters = {@Filter({Controller.class, 
	ControllerAdvice.class})}, useDefaultFilters = false)
@EnableWebMvc
public class AppServletConfig extends WebMvcConfigurerAdapter{

	//配置视图解析器
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/pages/", ".html");
	}

	//整合fastson
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		//禁用静止循环(bug)
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializeConfig(SerializeConfig.globalInstance);
		config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
		converter.setFastJsonConfig(config);
		//配置messageConverter对象
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(new MediaType("text", "html", Charset.forName("UTF-8")));
		list.add(new MediaType("application", "json", Charset.forName("UTF-8")));
		converter.setSupportedMediaTypes(list);
		converters.add(converter);
	}
	
}
