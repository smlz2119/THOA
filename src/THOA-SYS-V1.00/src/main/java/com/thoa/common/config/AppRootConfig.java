package com.thoa.common.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(value = "com.thoa", excludeFilters = {@Filter({Controller.class, ControllerAdvice.class})})
@PropertySource("classpath:configs.properties")
@MapperScan("com.thoa.**.dao")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AppRootConfig {

	 /**
	  * 让系统支持多个properties文件应用
	  * @return
	  */
	 @Bean
	 public PropertySourcesPlaceholderConfigurer newPropertyPlaceholderConfigurer(){
		 return new PropertySourcesPlaceholderConfigurer();
	 }
	 
	 @Bean(value = "dataSource", initMethod = "init", destroyMethod = "close")
	 @Lazy(false)
	 public DataSource newDataSource(@Value("${driver}")String driver, 
			 @Value("${url}")String url, 
			 @Value("${username}")String username,
			 @Value("${password}")String password) {
		 DruidDataSource ds = new DruidDataSource();
		 ds.setDriverClassName(driver);
		 ds.setUrl(url);
		 ds.setUsername(username);
		 ds.setPassword(password);
		 return ds;
	 }
	 
	 //整合sqlSessionFactory对象
	 @Bean("sqlSessionFactory")
	 public SqlSessionFactoryBean newSqlSessionFactoryBean(@Autowired DataSource dataSource) throws IOException {
		 SqlSessionFactoryBean ssf = new SqlSessionFactoryBean();
		 Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
				 .getResources("classpath*:mapper/*Mapper.xml");
		 ssf.setDataSource(dataSource);
		 ssf.setMapperLocations(mapperLocations);
		 return ssf;
	 }
	 
	 //spring中声明事务的控制
	 @Bean("dataSourceTransactionManager")
	 public DataSourceTransactionManager newDataSourceTransactionManager(DataSource dataSource) {
		 DataSourceTransactionManager tm = new DataSourceTransactionManager();
		 tm.setDataSource(dataSource);
		 return tm;
	 }
}
