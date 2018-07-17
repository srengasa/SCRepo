package com.syf.gemfire.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.syf.gemfire.client.config.ConfigProperties;
import com.syf.gemfire.client.util.CommonUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonUtil.class)
public class PowerMockTest {

	@Mock
	ConfigProperties configProps;
	
	@Test
	public void commonUtilTest() {
		mockStatic(CommonUtil.class);
		when(CommonUtil.getPropertyValue(configProps, "server.host")).thenReturn("Hello!");
		when(CommonUtil.getPropertyValue(configProps, "server.port")).thenReturn("Port!");
		String s = (String) CommonUtil.getPropertyValue(configProps, "server.port");
		System.out.println("String *****" + s);
		assertThat(s).isNotNull();
	}
}

@RunWith(MockitoJUnitRunner.class)
class SampleTest {
	
}