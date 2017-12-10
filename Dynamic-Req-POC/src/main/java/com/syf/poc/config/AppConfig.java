package com.syf.poc.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class AppConfig {

	@Autowired
	DataSource dataSource;
	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
		emf.setPersistenceUnitName("myPU");
		emf.setPackagesToScan("com.syf.poc.persistence");
		emf.setJpaProperties(getAdditionalProps());
		return emf;
	}
	
	private Properties getAdditionalProps() {
		Properties props = new Properties();
		props.put("eclipselink.weaving", "false");
		return props;
	}
}
