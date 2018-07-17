package com.syf.gemfire.client.util;

import java.util.Map;

import com.syf.gemfire.client.config.ConfigProperties;

public class CommonUtil {

	public static Object getPropertyValue(ConfigProperties configProps, String key) {
		if (key == null) { return null; }
		String[] keys = null;
		if (key.contains(".")) {
		    keys = key.split("\\.");
		} else {
			keys = new String[] { key };
		}
		Object map = configProps.getCaching();
		for (String s : keys) {
			map = ((Map<String, Object>) map).get(s);
		}
		return map;
	}
	
	private CommonUtil() {}
}
