package com.syf.gemfire.client.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import com.syf.caching.domain.Customer;
import com.syf.caching.serializer.GemfireSerializer;
import com.syf.gemfire.client.util.CommonUtil;

@Configuration
public class GemfireConfig {

	@Autowired
	private ConfigProperties configProps;
	
	@Autowired
	private GemfireSerializer pdxSerializer;
	
	@Bean("gemfireProps")
	public Properties setGemfireProps() {
		Properties gemfireProps = new Properties();
		gemfireProps.setProperty("log-level", (String) CommonUtil.getPropertyValue(configProps, "log-level"));
		return gemfireProps;
	}
	
	@Bean("poolFactoryBean")
	public PoolFactoryBean poolFactoryBean() {
		PoolFactoryBean pfb = new PoolFactoryBean();
		String host = (String) CommonUtil.getPropertyValue(configProps, "server.host");
		int port = (Integer) CommonUtil.getPropertyValue(configProps, "server.port");
		pfb.addServers(new ConnectionEndpoint[] { new ConnectionEndpoint(host, port)});
		return pfb;
	}
	
	/*@Bean
	public ClientCacheFactory cacheFactoryBean(@Qualifier("gemfireProps") Properties properties, GemFireCache cache) {
		ClientCacheFactory cfb = new ClientCacheFactory();
		String host = (String) CommonUtil.getPropertyValue(configProps, "locators.host");
		int port = (Integer) CommonUtil.getPropertyValue(configProps, "locators.port");
		
		String shost = (String) CommonUtil.getPropertyValue(configProps, "server.host");
		int sport = (Integer) CommonUtil.getPropertyValue(configProps, "server.port");
		cfb.addPoolLocator(host, port);
		cfb.addPoolServer(shost, sport);
		return cfb;
	}*/
	
	/*@Bean
	public CacheFactoryBean cacheFactoryBean(@Qualifier("gemfireProps") Properties properties) {
		CacheFactoryBean cfb = new CacheFactoryBean();
		cfb.setProperties(properties);
		return cfb;
	}*/
	
	@Bean
	public ClientCacheFactoryBean clientCacheFactoryBean(/*Pool poolFactoryBean*/)  {
		ClientCacheFactoryBean ccfb = new ClientCacheFactoryBean();
		String host = (String) CommonUtil.getPropertyValue(configProps, "locators.host");
		int port = (Integer) CommonUtil.getPropertyValue(configProps, "locators.port");
		ccfb.setLocators(new ConnectionEndpoint[] { new ConnectionEndpoint(host, port) });
		ccfb.setPdxSerializer(pdxSerializer);
		//ccfb.setPool(poolFactoryBean);
		return ccfb;
	}
	
	@Bean
	public ClientRegionFactoryBean<Long, Customer> customerRegion(GemFireCache clientCahe, Pool poolFactoryBean) {
		ClientRegionFactoryBean<Long, Customer> crf = new ClientRegionFactoryBean<Long, Customer>();
		crf.setCache(clientCahe);
		crf.setPool(poolFactoryBean);
		crf.setRegionName("customer");
		crf.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		return crf;
	}
	
	
}
