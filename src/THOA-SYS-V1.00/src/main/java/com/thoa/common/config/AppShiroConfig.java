package com.thoa.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AppShiroConfig {

	//测试该配置文件是否被加载
	public AppShiroConfig() {
		System.out.println("AppShiroConfig()");
	}
	
	@Bean("securityManager")
	public DefaultWebSecurityManager newDefaultWebSecurityManager(
//			AuthorizingRealm userRealm, 
			EhCacheManager enCacheManager) {
		DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
//		sManager.setRealm(userRealm);
		sManager.setCacheManager(enCacheManager);
		return sManager;
	}
	
	//配置缓存
	@Bean("ehCacheManager")
	public EhCacheManager newEhCacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}
	
	//配置shiro缓存
	@Bean("shiroFilterFactoryBean")
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(
			SecurityManager securityManager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		//此用户是一个非认证用户，需要验证
		bean.setLoginUrl("/doLoginUI.do");
		LinkedHashMap<String, String> fcMap = new LinkedHashMap<String, String>();
		fcMap.put("/bower_components/**","anon");//anon表示允许匿名访问
		fcMap.put("/build/**", "anon");
		fcMap.put("/dist/**","anon");
		fcMap.put("/plugins/**","anon");
		fcMap.put("/doLogin.do","anon");
		fcMap.put("/doLogout.do ","logout");
		fcMap.put("/**", "authc");
		bean.setFilterChainDefinitionMap(fcMap);
		return bean;
	}
	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor newLifecycleBeanPostProcessor(){
			return new LifecycleBeanPostProcessor();
	}
	
	@DependsOn(value="lifecycleBeanPostProcessor")
	@Bean
	public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator(){
			return new DefaultAdvisorAutoProxyCreator();
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor newAuthorizationAttributeSourceAdvisor(
				SecurityManager securityManager){
			    AuthorizationAttributeSourceAdvisor bean=
				new AuthorizationAttributeSourceAdvisor();
			    bean.setSecurityManager(securityManager);
			return bean;
	}
}
