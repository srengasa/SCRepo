package com.syf.poc.mapping;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.syf.poc.model.MyRequest;
import com.syf.poc.model.WtxRequest;
import com.syf.poc.persistence.CnarmMappingConfigSearcher;
import com.syf.poc.persistence.model.CnarmMappingConfigVo;

@Component
public class MyMapper extends BaseCnarmMapper {

	private static final Logger logger = Logger.getLogger(MyMapper.class);
	
	@Autowired
	private CnarmMappingConfigSearcher searcher;
	
	public WtxRequest buildWtxRequest(MyRequest request) {
		Collection<CnarmMappingConfigVo> cnarmMappingConfigVos = getCnarmMappingConfigValues("PRB");
		WtxRequest wtxRequest = new WtxRequest();
		try {
			for (CnarmMappingConfigVo cnarmMappingConfigVo : cnarmMappingConfigVos) {
				performCnarmMapping(wtxRequest, request, null, cnarmMappingConfigVo);
			}
		}catch (Exception e) {
			logger.error("Exception in mapping cnarm objects");
		}
		return wtxRequest;
	}
	
	public Collection<CnarmMappingConfigVo> getCnarmMappingConfigValues(String groupName) {
		Collection<CnarmMappingConfigVo> questionConfigVos = searcher.getAllConfigVo(groupName);
		return questionConfigVos;
	    }
	
	@Override
	protected String getMethodNameForConstraintName(CnarmMappingConfigVo cnarmMappingConfigVo) {
		String customizerName = cnarmMappingConfigVo.getConstraintName();
		String customizeMethodName = null;
		if ("customize".equalsIgnoreCase(customizerName)) {
		    customizeMethodName = getCustomizeMethodName("customize", cnarmMappingConfigVo.getBondlineAttribute(),
			    cnarmMappingConfigVo.getCnarmAttribute());
		} else if ("customizeBooleanQuestion".equalsIgnoreCase(customizerName)) {
		    customizeMethodName = getCustomizeMethodName("customizeBooleanQtype", null, null);
		}
		else if ("customizeDateQuestion".equalsIgnoreCase(customizerName)) {
		    customizeMethodName = getCustomizeMethodName("customizeDateQtype", null, null);
		}
		return customizeMethodName;
	}

	private String getCustomizeMethodName(String methodType, String source, String destination) {
		StringBuffer methodName = new StringBuffer();
		if ("customize".equalsIgnoreCase(methodType)) {
			StringBuffer sourceAttr = new StringBuffer(source.substring(source.lastIndexOf('.') + 1));
			StringBuffer destAttr = new StringBuffer(destination.substring(destination.lastIndexOf('.') + 1));

			sourceAttr.setCharAt(0, Character.toUpperCase(sourceAttr.charAt(0)));
			destAttr.setCharAt(0, Character.toUpperCase(destAttr.charAt(0)));

			methodName.append(methodType).append(sourceAttr).append("For").append(destAttr);
			System.out.println("Customizer method name : Customizer." + methodName);
		} else if ("customizeBooleanQuestion".equalsIgnoreCase(methodType)) {
			methodName.append("customizeForBooleanQuestionType");
		} else if ("customizeDateQuestion".equalsIgnoreCase(methodType)) {
			methodName.append("customizeForDateQuestionType");
		}
		return methodName.toString();

	}
	
	@Override
	protected Class<?> getCustomizerClassInstance() {
		return Customizer.class;
	}

	@Override
	protected Class<?> getUtilityClassInstance() {
		return SpcUtility.class;
	}

}
