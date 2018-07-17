package com.syf.gemfire.client;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syf.gemfire.client.config.ConfigProperties;
import com.syf.gemfire.client.controller.GemfireController;
import com.syf.gemfire.client.helper.GemfireService;
import com.syf.gemfire.client.util.CommonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataGemFireApplicationTests {

	@Autowired
	private GemfireController classUnderTest;
	
	@InjectMocks
	private GemfireController classMock;
	
	@Mock
	private GemfireService service;
	
	@Autowired
	ConfigProperties config;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void testController() {
		CommonUtil cc = Mockito.mock(CommonUtil.class, "Hello");
		//Mockito.when(cc.getPropertyValue(anyObject(), anyString())).thenReturn("Hello");
		String s = (String) cc.getPropertyValue(config, "url");
		System.out.println("SSSSSS " + s);
	}
}
