package com.syf.gemfire.client.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="gemfire")
@Component
public class ConfigProperties implements InitializingBean {

	private Map<String, Object> caching = new HashMap<>();

	public Map<String, Object> getCaching() {
		return caching;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("config props " + caching);
	}

}
