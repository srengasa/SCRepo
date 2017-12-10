package com.syf.poc.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.syf.poc.persistence.model.CnarmMappingConfigVo;

/*import com.cnasurety.services.bondline.factory.PCConsumerUtility;
import com.cnasurety.services.centralbond.entity.CnarmMappingConfigSearcher;
import com.cnasurety.services.centralbond.valueobjects.BondInfoVo;
import com.cnasurety.services.centralbond.valueobjects.PartyInfoVo;
import com.cnasurety.services.centralbond.valueobjects.SaveSubmissionVo;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.BondSubmission;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.Coverage;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.InvolvedParty;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.SaveSubmission;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.SuretyAccount;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.SuretyInvolvedParty;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.SuretyMarketableProduct;
import com.cnasurety.services.surety.managebondsubmission.contract.v1.SuretyProduct;*/

public class SaveSubmissionCnarmMapper extends BaseCnarmMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveSubmissionCnarmMapper.class);
    private static java.util.Properties props = null;

    /**
     * This method will construct saveSubmission object and populate it
     * with IBL attribute values.
     * 
     * @param bondInfoVo - BondInfoVo
     * @param d - XML Document Object
     * @return the constructed saveSubmission object
     * @throws Exception
     */
    /*public Object buildCnarmSaveSubmission(BondInfoVo bondInfoVo, Document d) throws Exception {
	final String methodName = "buildCnarmSaveSubmission";

	Collection<CnarmMappingConfigVo> cnarmMappingConfigVos = getCnarmMappingConfigValues(bondInfoVo.getFormType());
	Collection<CnarmMappingConfigVo> formTypeConfigVos = getCnarmConfigVoByGroup(cnarmMappingConfigVos, bondInfoVo.getFormType());
	
	Collection<CnarmMappingConfigVo> commonCnarmConfigVos = getCnarmConfigVoByGroup(cnarmMappingConfigVos, "ALL");
	commonCnarmConfigVos.addAll(formTypeConfigVos);

	Object saveSubmission = new SaveSubmission();
	BondSubmission bs = new BondSubmission();
	saveSubmission.setBondSubmission(bs);

	SaveSubmissionVo saveSubmissionVo = new SaveSubmissionVo();
	saveSubmissionVo.setBondInfoVo(bondInfoVo);
	saveSubmissionVo.setXmlDocument(d);
	
	processAttributeTypes(saveSubmission, bondInfoVo, d, commonCnarmConfigVos);

	processRecurringElements(saveSubmission, bondInfoVo, d, formTypeConfigVos);
	
	Collection<CnarmMappingConfigVo> partyCnarmMappingConfigVos = getCnarmConfigVoByGroup(cnarmMappingConfigVos, "All-Party");
	setParties(bs, saveSubmissionVo, partyCnarmMappingConfigVos);

	// workaround for coveragecode & product code
	if (bs.getSuretyMarketableProduct() == null)
	    bs.setSuretyMarketableProduct(new SuretyMarketableProduct());
	if (bs.getSuretyMarketableProduct().getSuretyProduct() == null)
	    bs.getSuretyMarketableProduct().setSuretyProduct(new SuretyProduct());
	if (bs.getSuretyMarketableProduct().getSuretyProduct().getCoverage() == null
		|| bs.getSuretyMarketableProduct().getSuretyProduct().getCoverage().getCoverageCode() == null) {
	    bs.getSuretyMarketableProduct().getSuretyProduct().setCoverage(new Coverage());
	    bs.getSuretyMarketableProduct().getSuretyProduct().getCoverage().setCoverageCode(getCoverageCode(bondInfoVo));
	}
	bs.getSuretyMarketableProduct().getSuretyProduct().setProductCode(getProductCode(bondInfoVo));
	PCConsumerUtility.marshalPOJOToXML(saveSubmission);

	return saveSubmission;
    }*/

    /*private void processAttributeTypes(SaveSubmission saveSubmission, BondInfoVo bondInfoVo, Document d,
	    Collection<CnarmMappingConfigVo> commonCnarmConfigVos) throws ServiceException {
	final String methodName = "processAttributeTypes";

	Object sourceValue = null;
	BondSubmission bs = saveSubmission.getBondSubmission();
	Map<String, List<CnarmMappingConfigVo>> arrElementsMap = new HashMap<String, List<CnarmMappingConfigVo>>();

	for (CnarmMappingConfigVo questionConfigVo : commonCnarmConfigVos) {
	    sourceValue = null;
	    LOGGER.info(methodName, "Executing SOURCE: " + questionConfigVo.getBondlineAttribute() + " DEST : " + questionConfigVo.getCnarmAttribute()
		    + " GROUP_NM :" + questionConfigVo.getGroupName() + " SEQ : " + questionConfigVo.getGroupSeqNumber());
	    if ("OLTP(XML)".equalsIgnoreCase(questionConfigVo.getBondlineAttributeType())
		    || "OLTP".equalsIgnoreCase(questionConfigVo.getBondlineAttributeType())
		    || "DEFAULT".equalsIgnoreCase(questionConfigVo.getBondlineAttributeType())) {
		sourceValue = performCnarmMapping(saveSubmission, bondInfoVo, d, questionConfigVo);

	    }
	    
	    if (sourceValue != null && questionConfigVo.getConstraintAttribute() != null
		    && !"".equals(questionConfigVo.getConstraintAttribute())) {
		handleConstraints(saveSubmission, bondInfoVo, d, questionConfigVo);
	    }
	}
    }
    
    
    private void processRecurringElements(SaveSubmission saveSubmission, BondInfoVo bondInfoVo, Document d,
	    Collection<CnarmMappingConfigVo> formTypeConfigVos) throws ServiceException {
	
	Map<String, List<CnarmMappingConfigVo>> recurringElementsMap = new HashMap<String, List<CnarmMappingConfigVo>>();
	for (CnarmMappingConfigVo formTypeConfigVo : formTypeConfigVos) {
	    if (formTypeConfigVo.getBondlineAttributeType().endsWith("RecurringElement-OLTP(XML)")) {
		List<CnarmMappingConfigVo> element = null;
		if (recurringElementsMap.get(formTypeConfigVo.getBondlineAttributeType()) != null) {
		    element = recurringElementsMap.get(formTypeConfigVo.getBondlineAttributeType());
		} else {
		    element = new ArrayList<CnarmMappingConfigVo>();
		    recurringElementsMap.put(formTypeConfigVo.getBondlineAttributeType(), element);
		    recurringElementsMap.put(formTypeConfigVo.getBondlineAttributeType().replace("OLTP(XML)", "Specifier"),
			    getSpecifier(formTypeConfigVo, formTypeConfigVos));
		}
		element.add(formTypeConfigVo);
	    }
	}
	
	if (!recurringElementsMap.isEmpty()) {
	    handleRecurringElements(recurringElementsMap, bondInfoVo, d, saveSubmission);
	}
    }

    public Collection<CnarmMappingConfigVo> getCnarmMappingConfigValues(String groupName) throws ServiceException {
	CnarmMappingConfigSearcher searcher = new CnarmMappingConfigSearcher(groupName);
	Collection<CnarmMappingConfigVo> questionConfigVos = searcher.execute();
	return questionConfigVos;
    }

    private void setParties(BondSubmission bs, SaveSubmissionVo saveSubmissionVo, Collection<CnarmMappingConfigVo> partyCnarmConfigVos)
	    throws ServiceException {

	Collection<PartyInfoVo> partyInfoVos = saveSubmissionVo.getBondInfoVo().getPartyInformationCollection();
	Document doc = saveSubmissionVo.getXmlDocument();

	if (partyInfoVos == null || partyInfoVos.isEmpty())
	    return;

	Collection<CnarmMappingConfigVo> accountPartyNameCnarmVo = getCnarmConfigVoByAttribute(partyCnarmConfigVos, "Account-Party-OLTP");
	Collection<CnarmMappingConfigVo> accountPartylocCnarmVos = getCnarmConfigVoByAttribute(partyCnarmConfigVos,
		"Account-Party-Location-OLTP");
	Collection<CnarmMappingConfigVo> bondPartyNameCnarmVo = getCnarmConfigVoByAttribute(partyCnarmConfigVos, "Bond-Party-OLTP");
	Collection<CnarmMappingConfigVo> bondPartylocCnarmVos = getCnarmConfigVoByAttribute(partyCnarmConfigVos, "Bond-Party-Location-OLTP");

	Object sourceValue = null;
	String accountPartyRoleTypeCodes = "PRN,";
	SuretyAccount suretyAccountHeader = bs.getSuretyAccount();
	Map<String, Integer> partyCount = new HashMap<String, Integer>();

	if (suretyAccountHeader == null) {
	    suretyAccountHeader = new SuretyAccount();
	    bs.setSuretyAccount(suretyAccountHeader);
	}

	for (PartyInfoVo partyInfoVo : partyInfoVos) {
	    saveSubmissionVo.setPartyInfoVo(partyInfoVo);
	    if (partyInfoVo != null && partyInfoVo.getPartyBondVo() != null && partyInfoVo.getPartyBondVo().getRoleTypeCode() != null) {
		InvolvedParty invParty = null;
		if (accountPartyRoleTypeCodes.contains(partyInfoVo.getPartyBondVo().getRoleTypeCode())) {
		    invParty = new InvolvedParty();
		    
	            setPartyDetail(invParty, saveSubmissionVo, doc, accountPartyNameCnarmVo,null,null);
		    sourceValue = null;
		    if (partyInfoVo.getLocationVo() != null) {
			setPartyDetail(invParty, saveSubmissionVo, doc, accountPartylocCnarmVos, null,null);
		    }
		    
		    suretyAccountHeader.getAccountParty().add(invParty);
		} else {
		    invParty = new SuretyInvolvedParty();
		    setPartyDetail(invParty, saveSubmissionVo, doc, bondPartyNameCnarmVo,null,null);
		    if (partyInfoVo.getLocationVo() != null) {
			setPartyDetail(invParty, saveSubmissionVo, doc, bondPartylocCnarmVos, null,null);
		    }

		    //invParty.setPartyRoleName("CNAPolicyAdditionalPrincipal");
		    //invParty.setPartyRoleType("CNAPolicyAdditionalPrincipal");
		    
		    bs.getBondParty().add((SuretyInvolvedParty) invParty);
		}
		setBondFamilySpecificDetails(partyCount, saveSubmissionVo, partyCnarmConfigVos, invParty);
	    }
	}
    }
    
    private void setBondFamilySpecificDetails(Map<String, Integer> partyCount, SaveSubmissionVo saveSubmissionVo,
	    Collection<CnarmMappingConfigVo> partyCnarmConfigVos, InvolvedParty invParty) throws ServiceException {
	int count = 1;
	PartyInfoVo partyInfoVo = saveSubmissionVo.getPartyInfoVo();
	
        if (!partyCount.containsKey(partyInfoVo.getPartyBondVo().getRoleTypeCode())) {
    	    partyCount.put(partyInfoVo.getPartyBondVo().getRoleTypeCode(), count);
	} else {
	    count = partyCount.get(partyInfoVo.getPartyBondVo().getRoleTypeCode());
	    partyCount.put(partyInfoVo.getPartyBondVo().getRoleTypeCode(), ++count);
	}
        String partyType = getSpcPartyType(partyInfoVo.getPartyBondVo().getRoleTypeCode(), saveSubmissionVo);
	Collection<CnarmMappingConfigVo> c = getCnarmConfigVoByAttribute(partyCnarmConfigVos,
		partyType + "-" + saveSubmissionVo.getBondInfoVo().getFormType() + "-OLTP(XML)");
	setPartyDetail(invParty, saveSubmissionVo, saveSubmissionVo.getXmlDocument(), c, "[*]", String.valueOf(count));//bondinfovo instead??
    }
    
    private void setPartyDetail(Object invParty, SaveSubmissionVo saveSubmissionVo, Document doc, Collection<CnarmMappingConfigVo> accountPartyNameCnarmVo,
	    String delimeter, String replaceVariable) throws ServiceException {
	Object sourceValue;
	for (CnarmMappingConfigVo principalCnarmVo : accountPartyNameCnarmVo) {
		sourceValue = null;
		if (delimeter!=null && principalCnarmVo.getBondlineAttribute().contains(delimeter)) {
		    principalCnarmVo = principalCnarmVo.cloneObject();
		    principalCnarmVo.setBondlineAttribute(principalCnarmVo.getBondlineAttribute().replace(delimeter, replaceVariable));
		}
		sourceValue = performCnarmMapping(invParty, saveSubmissionVo, doc, principalCnarmVo);
		if (principalCnarmVo.getConstraintAttribute() != null && sourceValue != null) {
		    handleConstraints(invParty, saveSubmissionVo, doc, principalCnarmVo);
		}
	    }
    }
    
    private String getSpcPartyType(String roleTypeCode, SaveSubmissionVo saveSubmissionVo) throws ServiceException {
	return Customizer.customizeRoleTypeCodeForPartyRoleName(roleTypeCode, saveSubmissionVo);
    }*/

    protected Object handleConstraints(Object ss, Object infoVo, Document d, CnarmMappingConfigVo cnarmMappingConfigVo)
	    throws ServiceException {
	String constraintSource = cnarmMappingConfigVo.getConstraintAttribute();
	String constraintTarget = cnarmMappingConfigVo.getConstraintTarget();
	String constraintType = cnarmMappingConfigVo.getConstraintAttributeType();

	if (constraintSource != null && !"".equals(constraintSource)) {
	    if (constraintSource.contains(",")) {
		String[] constraintSrcArr = constraintSource.split(",");
		String[] constraintTgtArr = constraintTarget.split(",");
		String[] constraintSrcType = constraintType.split(",");

		for (int i = 0; i < constraintSrcArr.length; i++) {
		    CnarmMappingConfigVo tempConfigVo = new CnarmMappingConfigVo(constraintSrcArr[i], constraintTgtArr[i],
			    constraintSrcType[i]);
		    tempConfigVo.setGroupName(cnarmMappingConfigVo.getGroupName());
		    tempConfigVo.setGroupSeqNumber(cnarmMappingConfigVo.getGroupSeqNumber());
		    performCnarmMapping(ss, infoVo, d, tempConfigVo);
		}
	    } else {
		CnarmMappingConfigVo tempConfigVo = new CnarmMappingConfigVo(cnarmMappingConfigVo.getConstraintAttribute(),
			cnarmMappingConfigVo.getConstraintTarget(), cnarmMappingConfigVo.getConstraintAttributeType());
		tempConfigVo.setGroupName(cnarmMappingConfigVo.getGroupName());
		tempConfigVo.setGroupSeqNumber(cnarmMappingConfigVo.getGroupSeqNumber());
		performCnarmMapping(ss, infoVo, d, tempConfigVo);
	    }
	}
	return ss;
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
	}
	else if ("customizeDateQuestion".equalsIgnoreCase(methodType)) {
	    methodName.append("customizeForDateQuestionType");
	}
	return methodName.toString();

    }

    private Class customizer = Customizer.class;
    private Class spcUtility = SpcUtility.class;
    
    @Override
    protected Class getCustomizerClassInstance() {
	return customizer;
    }

    @Override
    protected Class getUtilityClassInstance() {
	return spcUtility;
    }
    
    //coveredPerson.coveredPerson[*]Position = bondSubmission.bondTerm.bondScheduledPosition.[*].positionTypeCode
    /*private void handleRecurringElements(Map<String, List<CnarmMappingConfigVo>> arrElementsMap, BondInfoVo bondInfoVo, Document d,
	    SaveSubmission ss) throws ServiceException {
	for (Map.Entry<String, List<CnarmMappingConfigVo>> entry : arrElementsMap.entrySet()) {
	    
	    int tagCount = getIterationCount(entry.getValue().get(0), d, arrElementsMap);
	    boolean createListIndex;
	    for (int i = 1; i <= tagCount; i++) {
		createListIndex = true;
		for (CnarmMappingConfigVo configVo : entry.getValue()) {

		    if (configVo.getBondlineAttributeType().contains("Specifier")) {
		    	continue;
		    }
		    StringBuffer source = new StringBuffer(configVo.getBondlineAttribute());
		    if (source.indexOf(".") != -1) {
			source.delete(0, source.indexOf(".") + 1);
		    }
		    source.replace(source.indexOf("["), source.indexOf("]") + 1, String.valueOf(i));
		    StringBuffer cnarmPath = new StringBuffer(configVo.getCnarmAttribute());
		    if (cnarmPath.indexOf("[") != -1) {
			if (!createListIndex) {
			    cnarmPath.replace(cnarmPath.indexOf("["), cnarmPath.indexOf("]") + 1, "y");
			} else {
			    cnarmPath.replace(cnarmPath.indexOf("["), cnarmPath.indexOf("]") + 1, "x");
			}
		    }
		    CnarmMappingConfigVo tempVo = new CnarmMappingConfigVo(source.toString(), cnarmPath.toString(), configVo.getBondlineAttributeType());
		    tempVo.setGroupName(configVo.getGroupName());
		    tempVo.setGroupSeqNumber(configVo.getGroupSeqNumber());
		    Object srcValue = performCnarmMapping(ss, bondInfoVo, d, tempVo);
		    if (srcValue != null) {
			createListIndex = false;
			if (configVo.getConstraintAttribute() != null) {
			    String constraintSrc = configVo.getConstraintAttribute();
			    if (constraintSrc.indexOf("[*]") != -1) {
				tempVo.setConstraintAttribute(constraintSrc.replace("[*]", String.valueOf(i)));
			    }
			    handleConstraints(ss, bondInfoVo, d, tempVo);
		        }
		    }
		}
	    }
	}
    }*/
    
    private int getIterationCount(CnarmMappingConfigVo cnarmMappingConfigVo, Document d, Map<String, List<CnarmMappingConfigVo>> recurringElements) throws ServiceException {
	List<CnarmMappingConfigVo> specifierAttributes = recurringElements.get(cnarmMappingConfigVo.getBondlineAttributeType().replace("OLTP(XML)", "Specifier"));
	if (specifierAttributes != null && !specifierAttributes.isEmpty()) {
	    CnarmMappingConfigVo specifier = specifierAttributes.get(0);
	    String count = getNodeValue(d, specifier.getBondlineAttribute());
	    if (count != null && !"".equals(count)) {
		return Integer.parseInt(count);
	    } else {
		return 0;
	    }
	} else {
	    String src = cnarmMappingConfigVo.getBondlineAttribute();
	    String tagName = null;
	    if(src.indexOf('.')!=-1){
		tagName= src.substring(0, src.indexOf('.'));
	    }else{
		tagName= src.substring(0);
	    }
	    return d.getElementsByTagName(tagName).getLength();
	}
    }

    private List<CnarmMappingConfigVo> getSpecifier(CnarmMappingConfigVo recurringElement, Collection<CnarmMappingConfigVo> formTypeConfigVos) {
	
	String attributeName = recurringElement.getBondlineAttributeType().replace("OLTP(XML)", "Specifier");
	List<CnarmMappingConfigVo> specifierAttributes = (List<CnarmMappingConfigVo>) getCnarmConfigVoByAttribute(formTypeConfigVos, attributeName);
	return specifierAttributes;
    }
    
    /*public String getProductCode(BondInfoVo bondInfoVo) {
	return productCodeMap.get(bondInfoVo.getFormType());
    }

    public String getCoverageCode(BondInfoVo bondInfoVo) {
	LOGGER.info("getCoverageCode", bondInfoVo.getClassCode() + "*****" + bondInfoVo.getSubClassCode());
	return coverageCodeMap.get(bondInfoVo.getFormType());
    }*/

    private static Map<String, String> productCodeMap = new HashMap<String, String>() {

	private static final long serialVersionUID = 1L;

	{
	    put("FID", "CNASuretyDJ");
	    put("LI", "CNASuretyLI");
	    put("CT", "CNASuretyCO");
	    put("NOT", "CNASuretyNO");
	    put("LNP", "CNASuretyLP");
	    put("PO", "CNASuretyPOI");
	    put("CON", "CNASuretyCT");
	    put("FED", "CNASuretyFD");
	    put("PRB", "CNASuretyPB");
	    put("O", "CNASuretyMI");
	}
    };

    private static Map<String, String> coverageCodeMap = new HashMap<String, String>() {
	{
	    put("LI", "CNASULILSNDT");
	    put("CT", "CNASUCOIWofP");
	    put("FID", "CNASUFCBCBCRH");
	    put("NOT", "CNASUNONPB");
	    put("LNP", "CNASULPCSCS");
	    put("PO", "CNASUPOBCBPSD");
	    put("CON", "CNASUCTSPPPACCRC1");
	    put("FED", "CNASUFDPSA");
	    put("PRB", "CNASUPBAdminCoPR");
	    put("O", "CNASUMIWCSIDFS");
	}
    };
}