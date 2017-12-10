package com.syf.poc.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.util.StringUtils;

public class Customizer {

    
    /* Methods with more than 1 argument block start */
    
    /*public static String customizeRoleTypeCodeForPartyRoleType(String roleTypeCode, SaveSubmissionVo saveSubmissionVo) throws ServiceException {
	return customizeRoleTypeCodeForPartyRoleName(roleTypeCode, saveSubmissionVo);
    }
    
    public static String customizeRoleTypeCodeForPartyRoleName(String roleTypeCode, SaveSubmissionVo saveSubmissionVo) throws ServiceException {
	if (relationPartyMap.containsKey(roleTypeCode)) {
	    //determine deceased or ward party
	    if ("ATT".equals(roleTypeCode) && saveSubmissionVo.getPartyInfoVo().isAttorney()) {
		return "CNAPolicyAttorney";
	    }
	    String probateProcessType = CentralBondUtilities.getNodeValue(saveSubmissionVo.getXmlDocument(), "probateProcessType");
	    
	    if (probateProcessType != null && "D".equals(probateProcessType)) {
		return "CNAPolicyDeceased";
	    } else if (probateProcessType != null && !"W".equals(probateProcessType)) {
		return "CNAPolicyWard";
	    } else if (probateProcessType != null && !"M".equals(probateProcessType)) {
		return "CNAPolicyMinor";
	    }
	}
	return partyRoleTypeCodeMap.get(roleTypeCode);
    }*/
    
    /* Methods with more than 1 argument block end */
    
    public static String customizeCompanyCodeForWritingCompanyCode(String companyCode) {
	return companyCode.substring(1);
    }
    public static String customizeSuffixAbbreviation_ForPartyNameValue(String suffixAbbreviation) {
	if (suffixAbbreviation.endsWith(".")) {
	    return suffixAbbreviation.replace(".", "").trim();
	}
	return suffixAbbreviation;
    }
    
   /* public static String customizePersonIndicator_ForPartyType(String personIndicator) {
	if ("P".equalsIgnoreCase(personIndicator)
		|| "Y".equalsIgnoreCase(personIndicator)) {
	    return  PartyTypeEnum.PERSON.value();
	}
	return PartyTypeEnum.ORGANIZATION.value();
    }*/
    
    public static String customizeForBooleanQuestionType(String answer) {
	if ("Y,Yes".toUpperCase().contains(answer.toUpperCase())) {
	    return "true";
	} else {
	    return "false";
	}
    }
    public static String customizeForDateQuestionType(String instrumentDate) {
	
   	try {
	    return SpcUtility.convertStringToXMLGregorianCalendar(instrumentDate).toString();
	} catch (DatatypeConfigurationException e) {
	   
	}
   	return null;
       }
    
    private static Map<String, String> partyRoleTypeCodeMap = new HashMap<String, String>() {
	private static final long serialVersionUID = -3801036427248973588L;

	{
	    put("PRN", "Principal");
	    put("IND", "CNAPolicyOwnerIndemnitor");
	    put("ATT", "CNAPolicyAttorney");
	    put("UNK", "PolicyPriNamedInsured");
	    put("PPP", "CNAPolicyDeceased");
	    put("EMR", "CNAPolicyEmployer");
	    put("WRD", "CNAPolicyWard");
	}
    };
    
    private static Map<String, String> relationPartyMap = new HashMap<String, String>() {
	{
	    put("260", "Mother");
	    put("256", "Father");
	    put("264", "Sister");
	    put("246", "Brother");
	    put("252", "Daughter");
	    put("266", "Son");
	    put("268", "Wife");
	    put("259", "Husband");
	    put("245", "Aunt");
	    put("267", "Uncle");
	    put("265", "Sister-In-Law");
	    put("247", "Brother-In-Law");
	    put("279", "Daughter-In-Law");
	    put("278", "Son-In-Law");
	    put("258", "Grandchild");
	    put("250", "Cousin");
	    put("262", "Niece");
	    put("261", "Nephew");
	    put("255", "Ex-Wife");
	    put("254", "Ex-Husband");
	    put("253", "Distant Relative");
	    put("257", "Friend");
	    //put("ATT", "Attorney");
	    put("251", "Co-Worker");
	    put("249", "Clergy");
	    put("248", "Case/Social Worker");
	    put("263", "None");
	}
    };
    
    private static Map<String, String> businessTpCodeMap = new HashMap<String, String>() {
	private static final long serialVersionUID = 5105642027900730426L;

	{
	  put("C", "CORP");
	  put("L","LLC");
	}
    };
    /*public static ObligeeTypeEnum customizeObligeeTypeCodeForObligeeType(String obligeeTypeCode) {
	String obligeeTypeDesc=null;
	  try {
	      if (obligeeTypeCode != null) {
		  String tableName="FORM_OBLIGEE_TYPE";
		  String indicator="VALID_IND";
		  String codeField="OBLIGEE_TP_DESC";
		  String nameField="OBLIGEE_TP_CD";
		    CodesDelegate codesDelegate = new CodesDelegate();
		    Collection<NameValueVo> codesCollection = codesDelegate.getTypes(tableName, codeField, nameField, indicator);
		    for (Iterator<NameValueVo> iterator = codesCollection.iterator(); iterator.hasNext();) {
			NameValueVo nameValueVo = (NameValueVo) iterator.next();
			if (StringUtils.equals(nameValueVo.getName(), obligeeTypeCode)) {

			    obligeeTypeDesc = nameValueVo.getValue();
			    break;
			}
		    }
		    if (StringUtils.isEmpty(obligeeTypeDesc)) {
			throw new CodesException(null, "Unable to find Obligee Type", obligeeTypeDesc, 0);
		    }else{
			if(obligeeTypeDesc.equalsIgnoreCase(ObligeeTypeEnum.CITY_AND_COUNTY.value())){
			    return ObligeeTypeEnum.CITY_AND_COUNTY;
			}
			return ObligeeTypeEnum.valueOf(obligeeTypeDesc.toUpperCase());
		    }
		}
	} catch (CodesException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;

    }*/
    
    public static String customizeBusinessTypeCodeForLegalEntityType(String businessTypeCode) {
	return businessTpCodeMap.get(businessTypeCode);
    }

}
