package com.syf.poc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.syf.poc.persistence.model.CnarmMappingConfigVo;

public abstract class BaseCnarmMapper {

    private static final Logger LOGGER = Logger.getLogger(BaseCnarmMapper.class);
    
    protected static final String CNARM_XPATH_DELIMETER = ".";
    protected static final char CNARM_XPATH_DELIMETER_CHAR = '.';
    
    /**
     * This method maps IBL attribute to the specified CNARM object.
     * 
     * @param 
     *     cnarmRootObject - root object of CNARM which is parent of XPATH start string.
     * @param 
     *     bondInfoVo - any VO that contains OLTP attribute
     * @param 
     *     document - The XML Document Object
     * @param 
     *     cnarmMappingConfigVo: CnarmMappingConfigVo - contains IBL attribute,
     *     CNARM XPATH, IBL attribute type etc.
     * @return
     *     bondlineAttributeValue - IBL attribute value that is set in CNARM object.  
     * @throws ServiceException
     * 
     * @see also {@link CnarmMappingConfigVo}
     */
    /*
     * The method logic is as follows
     * 1. Determine IBL attribute type (OLTP, OLTP(XML), Default)
     * 2. Retrieve the IBL attribute value from corresponding source (BondInfoVo,
     *                           XML, cnarmMappingConfigVo for constants)
     * 3. Check if IBL attribute val == null or blank (""). If so, skip
     *                           this cnarm mapping, otherwise perform below steps
     * 4. Retrieve CNARM XPath and determine if it's a complex/ simple type.
     *     a. For Complex type, iterate CNARM XPATH wrt '.' and instantiate each object
     *                 in the path, only if it is null.
     *     b. For Simple type, check for customizations, data/ reference type conversions
     *                and set ibl attribute value in simple type.
     * 
     */
    @SuppressWarnings({"rawtypes"})
    protected <CNARM, Vo> Object performCnarmMapping(CNARM cnarmRootObject, Vo bondInfoVo, Document document, CnarmMappingConfigVo cnarmMappingConfigVo) throws ServiceException {
	final String methodName = "performCnarmMapping";
	
	//Retrieve the IBL attribute value from corresponding source (BondInfoVo, XML, cnarmMappingConfigVo for constants)
	Object bondlineAttributeValue  = getSourceValue(cnarmMappingConfigVo, document, bondInfoVo);
	
	//Check if IBL attribute val == null or blank (""). If so, skip this cnarm mapping,
	if (bondlineAttributeValue == null || "".equals(bondlineAttributeValue)) return null;
	
	Class cnarmClass = cnarmRootObject.getClass();
	Object cnarmObject = cnarmRootObject;
	String cnarmAttribute = cnarmMappingConfigVo.getCnarmAttribute();
	StringBuffer cnarmXpath = new StringBuffer(cnarmAttribute);
	
	boolean complexType = false;
	try {
	    for (; cnarmXpath != null && !"".equals(cnarmXpath.toString());) {
		complexType = cnarmXpath.indexOf(CNARM_XPATH_DELIMETER) != -1;
		if (complexType) {
		    
		    String level = cnarmXpath.substring(0, cnarmXpath.indexOf(CNARM_XPATH_DELIMETER));
		    String getterMethod = getMethodName("get", level);
		    Object object = invokeMethod(getterMethod, new Class[] {}, cnarmObject, new Object[] {});
		    if (object == null) {
			
			cnarmObject = instantiateFieldInObject(getDeclaredField(cnarmClass, level), cnarmObject);
			cnarmClass = cnarmObject.getClass();
		    } else if (object instanceof Collection) {
			if (object instanceof List) {

			    cnarmXpath.delete(0, cnarmXpath.indexOf(CNARM_XPATH_DELIMETER) + 1);
			    cnarmObject = getInstanceFromList(bondlineAttributeValue, (List<?>)object, cnarmClass, level, cnarmXpath.toString());
			    cnarmClass = cnarmObject.getClass();
			} 
		    } else {
			cnarmObject = object;
			cnarmClass = object.getClass();
		    }
		    LOGGER.debug("Iterating levels..." + level + " getter Method " + getterMethod);
		    cnarmXpath.delete(0, cnarmXpath.indexOf(CNARM_XPATH_DELIMETER) + 1);
		} else {
		    
		    if (cnarmMappingConfigVo.getConstraintName() != null) {
			bondlineAttributeValue = handleConstraintName(bondlineAttributeValue, bondInfoVo, cnarmObject, cnarmMappingConfigVo, cnarmXpath.toString());
		    }
		    bondlineAttributeValue = setSimpleTypeValue(bondlineAttributeValue, cnarmObject, cnarmXpath.toString(), cnarmMappingConfigVo);
		    cnarmXpath = null;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception in executing Src : " + cnarmMappingConfigVo.getBondlineAttribute() + " DEST : " + cnarmMappingConfigVo.getCnarmAttribute()
		    + " GRP_NM : " + cnarmMappingConfigVo.getGroupName() + " SEQ No : " + cnarmMappingConfigVo.getGroupSeqNumber() + " Msg : " + e.getMessage(), e);
	    //e.printStackTrace();
	} finally {
	    cnarmClass = null;
	    cnarmObject = null;
	}
	return bondlineAttributeValue;
    }
    
    /**
     * This method identifies bondline attribute type (OLTP, OLTP(XML), DEFAULT)
     * and fetch the value from corresponding source (VO object, XML, cnarmMappingConfigVo)
     * and return the value.
     * 
     * @param cnarmMappingConfigVo - CnarmMappingConfigVo
     * @param document - Document
     * @param infoVo - Vo
     * @return bondlineAttributeValue - Object
     * @throws ServiceException
     */
    protected <Vo> Object getSourceValue(CnarmMappingConfigVo cnarmMappingConfigVo, Document document, Vo infoVo) throws ServiceException {
	Object bondlineAttributeValue = null;
	if ("OLTP".equalsIgnoreCase(cnarmMappingConfigVo.getBondlineAttributeType()) || cnarmMappingConfigVo.getBondlineAttributeType().endsWith("-OLTP")) {
	    bondlineAttributeValue = getOLTPValue(infoVo, cnarmMappingConfigVo);
	} else if ("OLTP(XML)".equalsIgnoreCase(cnarmMappingConfigVo.getBondlineAttributeType()) || cnarmMappingConfigVo.getBondlineAttributeType().endsWith("-OLTP(XML)")){
	    bondlineAttributeValue = getNodeValue(document, cnarmMappingConfigVo.getBondlineAttribute());
	} else {
	    bondlineAttributeValue = cnarmMappingConfigVo.getBondlineAttribute();
	}
	return bondlineAttributeValue;
    }
    
    /**
     * This method will set ibl attribute value to the corresponding CNARM object
     * as specified in the CNARM XPATH.
     * 
     * @param bondlineAttributeValue - bondline attribute value
     * @param cnarmObject - CNARM Object
     * @param cnarmXpath - CNARM XPath
     * @param cnarmConfigVo - CnarmMappingConfigVo
     * @return Object - bondline attribute value compatible with CNARM object
     * @throws Exception
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    protected Object setSimpleTypeValue(Object bondlineAttributeValue, Object cnarmObject, String cnarmXpath, CnarmMappingConfigVo cnarmConfigVo) throws Exception {
	System.out.println("Simple Type name = " + cnarmXpath + " value = " + bondlineAttributeValue);
	Class cnarmXpathDatatypeClass = getDeclaredField(cnarmObject.getClass(), cnarmXpath).getType();
	
	if (!cnarmXpathDatatypeClass.isAssignableFrom(bondlineAttributeValue.getClass())) {
	    
	    bondlineAttributeValue = applyDataTypeConversion(bondlineAttributeValue, cnarmXpathDatatypeClass);
	}
	
	setValueForField(cnarmObject, cnarmXpath, bondlineAttributeValue);
	
	/*Field cnarmInstanceField = getDeclaredField(cnarmClass, cnarmXpath);
	cnarmInstanceField.setAccessible(true);
	cnarmInstanceField.set(cnarmObject, bondlineAttributeValue);*/
	/*String setterMethod = getMethodName("set", destinationString);
	invokeMethod(setterMethod, new Class[] { destDatatypeClass }, targetObject, new Object[] { sourceValue });*/
	
	return bondlineAttributeValue;
    }
    
    /**
     * This method will convert IBL attribute value compatible to set in CNARM object.
     * 
     * @param bondlineAttributeValue - bondline attribute value
     * @param destDatatypeClass - CNARM simple type object class instance
     * @return returnValue - converted IBL attribute value.
     * @throws Exception
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    protected <T> T applyDataTypeConversion(Object bondlineAttributeValue, Class<T> destDatatypeClass) throws Exception {
	T returnValue = null;
	if (destDatatypeClass.isEnum()) {
	    returnValue = (T) Enum.valueOf((Class<? extends Enum>) destDatatypeClass, bondlineAttributeValue.toString().toUpperCase());
	} else {
	    Class utilClass = getUtilityClassInstance();
	    String utilityMethodName = getUtilityMethodName(bondlineAttributeValue.getClass().getSimpleName(), destDatatypeClass.getSimpleName());
	    Method m = utilClass.getDeclaredMethod(utilityMethodName, new Class[] { bondlineAttributeValue.getClass() });
	    returnValue = (T) m.invoke(null, new Object[] { bondlineAttributeValue });
	}
	return returnValue;
    }
    
    /**
     * This method will set the bondlineAttributeValue to the instanceField of
     * cnarmObject specified by fieldName
     * 
     * @param cnarmObject - the CNARM object
     * @param fieldName - indicated an instanceField in cnarmObject arg
     * @param fieldValue - value to be set
     * @throws Exception
     */
    private void setValueForField(Object cnarmObject, String fieldName, Object fieldValue) throws Exception {
	Field cnarmInstanceField = getDeclaredField(cnarmObject.getClass(), fieldName);
	//boolean isAccessible = cnarmInstanceField.isAccessible();
	cnarmInstanceField.setAccessible(true);
	cnarmInstanceField.set(cnarmObject, fieldValue);
	//cnarmInstanceField.setAccessible(isAccessible);
    }
    /**
     * This method will iterate the class instance upto, not including, Object class instance
     * to get the fieldName.
     * 
     * @param targetClass - the class or its one of the superclass contains the fieldName
     * @param fieldName - the instanceVariable in targetClass argument
     * @return the field in the Class as indicated by fieldName argument
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private Field getDeclaredField(Class targetClass, String fieldName) throws Exception {
	Field instanceVariable = null;
	for (Class tempClass = targetClass; tempClass != null
		&& !tempClass.isAssignableFrom(Object.class); tempClass = tempClass.getSuperclass()) {
	    try {
		instanceVariable = tempClass.getDeclaredField(fieldName);
		break;
	    } catch (NoSuchFieldException nsfe) {
		//instanceVariable = targetClass.getSuperclass().getDeclaredField(fieldName);
	    }
	}
	if (instanceVariable == null) {
	    throw new NoSuchFieldException("Cannot find field " + targetClass.getSimpleName() + "." + fieldName);
	}
	return instanceVariable;
    }
    
    /**
     * This method will iterate the class instance upto, not including, Object class instance
     * to get the declared method.
     * 
     * @param targetClass - the class or one of it superclass contains the method
     * @param methodName - the name of the method
     * @param argsList - parameter list that method has
     * @return the method in the class as indicated by methodName argument
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    protected Method getDeclaredMethod(Class targetClass, String methodName, Class[] argsList) throws Exception {
	Method returnVal = null;
	for (Class tempClass = targetClass; tempClass != null
		&& !tempClass.isAssignableFrom(Object.class); tempClass = tempClass.getSuperclass()) {
	    try {
		returnVal = tempClass.getDeclaredMethod(methodName, argsList);
		break;
	    } catch (NoSuchMethodException nsme) {
	    }
	}
	if (returnVal == null) throw new NoSuchMethodException("Could not find method " + targetClass + "." + methodName);
	return returnVal;
    }
    
    /**
     * This method will do one of the following based on cnarmXPath.
     * If indexString in cnarmXpath is
     * 	<ol>
     * <li> x - creates a new instance of the list type and adds it to list.</li>
     * <li> y - gets the last added instance from the list (list.get(list.size() -1)</li>
     * <li> [condition=value] - iterates the list, executes condition on each item returns the item
     *   that matches condition. Otherwise creates an instance and adds condition=value and adds it to the list
     *   </li></ol>
     * If list of simple type like List&ltString> then sourceValue or converted to compatible data as list type and added to list .
     *  
     * @param bondlineAttributeValue - bondlineAttributeValue
     * @param list  List object 
     * @param targetClass cnarm Class instance
     * @param level - the instancefield name of the list in cnarm class 
     * @param cnarmXpath - the cnarmXpath
     * @return an instance in the list
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private <T> T getInstanceFromList(Object bondlineAttributeValue, List<T> list, Class targetClass, String level, String cnarmXpath) throws Exception {
	final String methodName = "getInstanceFromList";
	T instance = null;
	Class<T> listType = getGenericTypeForList(targetClass, level);
	String indexString = cnarmXpath.substring(0, cnarmXpath.indexOf(CNARM_XPATH_DELIMETER_CHAR));
	int index;
	try {
	    if (indexString != null && indexString.startsWith("[")) {
		index = getMatchingIndex(list, indexString, listType);
	    } else if ("x".equalsIgnoreCase(indexString)) {
		index = list.size() + 1;
	    } else if ("y".equalsIgnoreCase(indexString)) {
		index = list.size() - 1;
		if (index < 0) {
		    index = 0;
		}
	    } else {
		index = Integer.parseInt(indexString);
	    }
	} catch (NumberFormatException nfe) {
	    throw new ServiceException("You must specify a valid index for List");
	}
	if (list.size() <= index) {
	    LOGGER.debug("Generic Type " + listType);
	    boolean simpleType = "".equals(cnarmXpath.substring(cnarmXpath.indexOf(CNARM_XPATH_DELIMETER_CHAR) + 1));
	    if (simpleType) {
		if (!listType.isAssignableFrom(bondlineAttributeValue.getClass())) {
		    instance = applyDataTypeConversion(bondlineAttributeValue, listType);
		} else {
		    instance = (T) bondlineAttributeValue;
		}
	    } else {
		instance = listType.newInstance();
	    }
	    list.add(instance);
	} else {
	    instance = list.get(index);
	}
	return instance;
    }
    
    /**
     * This method retrieves the list type and returns the same.
     * 
     * @param cnarmClass - the CNARM class instance
     * @param fieldName - the list name as instance field in the cnarnClass
     * @return Class instance of the list type
     * @throws Exception
     */
    //eg. for List<String> this method returns String.class instance
    @SuppressWarnings("rawtypes")
    private Class getGenericTypeForList(Class cnarmClass, String fieldName) throws Exception {
	return (Class<?>) ((ParameterizedType) getDeclaredField(cnarmClass, fieldName).getGenericType()).getActualTypeArguments()[0];
    }
    
    /**
     * This method searches for a given condition and returns 
     *  an instance in the list that satisfied the condition (or)
     *  create a new instance fills up the condition values and adds it to list and
     *  returns the instance. 
     *  
     * @param list - List Object
     * @param indexString - String that contains [condition=value]
     * @param listType - the type of the list instance
     * @return the index of the list
     * @throws Exception
     * 
     *
     */
    private <T> int getMatchingIndex(List<T> list, String indexString, Class<T> listType) throws Exception {
	String constraints = indexString.substring(indexString.indexOf('[') + 1, indexString.indexOf(']'));
	String fieldName = constraints.split("=")[0].trim();//condition
	String fieldValue = constraints.split("=")[1].trim();//value
	CnarmMappingConfigVo tempVo = new CnarmMappingConfigVo(fieldName, null, null);
	
	for (int i = 0; i < list.size(); i++) {
	    Object o = list.get(i);
	    Object aValue = getOLTPValue(o, tempVo);
	    //match condition value with every instance in the list
	    //and return the index of that matched instance.
	    if (fieldValue.equals(aValue)) return i;//this index
	}
	return createListInstance(list, fieldName, fieldValue, listType);
    }

    /**
     * This method creates an instance of List type and 
     * adds the condition and value and adds it to list and returns last index of list
     * 
     * @param list - the list instance
     * @param fieldName - condition; the instance Field in list type class
     * @param fieldValue - the value to be set
     * @param listType - list type Class instance
     * @return the last index of the list
     * @throws Exception
     */
    private <T> int createListInstance(List<T> list, String fieldName, String fieldValue, Class<T> listType) throws Exception {
	//1. get parameterized type of list, 
	//2. get field name 
	//3. set field value in it
	T o = listType.newInstance();
	list.add(o);
	setValueForField(o, fieldName, fieldValue);
	/*Field f = getDeclaredField(listType, fieldName);
	f.setAccessible(true);
	f.set(o, fieldValue);*/
	
	return list.size() - 1;
    }
    
    /**
     * This method frames getter/setter method name and returns it.
     *   
     * @param methodType - get/ set
     * @param instanceField - instanceField name
     * @return the getter or setter method name for level
     */
    private String getMethodName(String methodType, String instanceField) {
	StringBuffer sb = new StringBuffer(instanceField);
	sb.setCharAt(0, Character.toUpperCase(instanceField.charAt(0)));
	String getterMethod = methodType + sb.toString();
	return getterMethod;
    }
    
    /**
     * This method frames the utility method name in the below format:
     * convert + ibl Attribute type + To + cnarm attribute type
     * 
     * @param iblAttributeType - the type of ibl attribute
     * @param cnarmAttributeType - the type of cnarm attribute
     * @return the method name
     */
    //eg. if ibl attribute type is String and cnarm attr type is int/ Integer then
    // methodname = convertStringToInteger
    // method signature should be - public static Integer convertStringToInteger(String data) {}
    private String getUtilityMethodName(String iblAttributeType, String cnarmAttributeType) {
	StringBuffer sb = new StringBuffer();
	sb.append("convert").append(iblAttributeType).append("To").append(cnarmAttributeType);
	return sb.toString();
    }

    /**
     * This method invokes the method specified by methodName in targetObject class. 
     * 
     * @param methodName - the name of the method
     * @param parameterList - method arguments.
     * @param targetObject - the object that contains the method
     * @param parameterValues - values for the method call
     * @return the return value of the invoked method
     * @throws Exception
     */
    private Object invokeMethod(String methodName, Class[] parameterList, Object targetObject, Object[] parameterValues) throws Exception {
	Method method = getDeclaredMethod(targetObject.getClass(), methodName, parameterList);
	Object object = method.invoke(targetObject, parameterValues);
	return object;
    }

    public static String getNodeValue(Document objectDocument, String attributeName) throws ServiceException {
	return "";//CentralBondUtilities.getNodeValue(objectDocument, attributeName);
    }
    
    /**
     * This method gets the bondline attribute value specified in cnarmMappingConfigVo
     * from infoVo object.
     * 
     * @param infoVo any Vo containing the bondline attribute
     * @param cnarmMappingConfigVo CnarmMappingConfigVo
     * @return the bondline attribute value
     * @throws ServiceException
     */
    protected Object getOLTPValue(Object infoVo, CnarmMappingConfigVo cnarmMappingConfigVo) throws ServiceException {
	Object returnVal = null;
	try {
	    Class targetClass = infoVo.getClass();
	    Object targetObject = infoVo;
	    StringBuffer source = new StringBuffer(cnarmMappingConfigVo.getBondlineAttribute());
	    boolean complexType = false;
	    for (;source != null && !"".equals(source);) {
		complexType = source.indexOf(CNARM_XPATH_DELIMETER) != -1;
		if (complexType) {
		    String level = source.substring(0, source.indexOf(CNARM_XPATH_DELIMETER));
		    Object object = getValueFromField(targetObject, level);
		    if (object != null && object instanceof Collection) {
			if (object instanceof List) {
			    source.delete(0, source.indexOf(CNARM_XPATH_DELIMETER) + 1);
			    List l = (List) object;
			    String indexString = source.substring(0, source.indexOf("."));
			    int index;
			    try {
				index = Integer.parseInt(indexString);
			    } catch (NumberFormatException nfe) {
				throw new ServiceException("You must specify a valid index for List");
			    }
			    targetObject = l.get(index);
			    targetClass = targetObject.getClass();
			    source.delete(0, source.indexOf(CNARM_XPATH_DELIMETER) + 1);
			}
		    } else if (object != null) {
			targetObject = object;
			targetClass = object.getClass();
		    } else if (object == null) {
			LOGGER.warn("getOLTPValue" + source.substring(0, source.indexOf(".")) + " is null so cannot continue. Skipping this attribute : " + source);
			source = null;
			break;
		    }
		    source.delete(0, source.indexOf(".") + 1);
		} else {
		    returnVal = getValueFromField(targetObject, source.toString());
		    source = null;
		}
	    }
	} catch(Exception e) {
	    LOGGER.error("getOLTPValue " + " Exception occured in retrieving OLTP value : " +  e.getClass() + " Msg: " + e.getMessage() + " Cause: " + e.getCause());
	    //e.printStackTrace();
	}
	return returnVal;
    }

    /**
     * This method gets value from the field in targetObject.
     * 
     * @param targetObject 
     * @param fieldName - the instanceField name in targetObject
     * @return the instance field value
     * @throws Exception
     */
    protected Object getValueFromField(Object targetObject, String fieldName)  throws Exception {
	Field f = getDeclaredField(targetObject.getClass(), fieldName);
	boolean isAccessible = f.isAccessible();
	try {
	    f.setAccessible(true);
	    return f.get(targetObject);
	} finally {
	    f.setAccessible(isAccessible);
	}
    }
    
    /**
     * This method will instantiate the instanceField (specified by Field) in fieldObject.
     * 
     * @param field - the instance field in fieldObject
     * @param fieldObject
     * @return the instanceField object
     * @throws Exception
     */
    protected Object instantiateFieldInObject(Field field, Object fieldObject) throws Exception {
	Object returnValue = field.getType().newInstance();
	boolean isAccessible = field.isAccessible();
	try {
	    field.setAccessible(true);
	    field.set(fieldObject, returnValue);
	} finally {
	    field.setAccessible(isAccessible);
	}
	return returnValue;
    }
    
    /**
     * This utility method takes a collection of CnarmConfigVo instances and
     * returns a sub list that matches the group name.
     * 
     * @param cnarmMappingConfigVos - Collection of CnarmMappingConfigVos
     * @param group - group name
     * @return sub list of cnarmMappingConfigVos that satisfies cnarmMappingConfigVo.groupName = group
     */
    public static Collection<CnarmMappingConfigVo> getCnarmConfigVoByGroup(Collection<CnarmMappingConfigVo> cnarmMappingConfigVos, String group) {
	List<CnarmMappingConfigVo> cnarmVos = new ArrayList<CnarmMappingConfigVo>();
	for (CnarmMappingConfigVo questVo : cnarmMappingConfigVos) {
	    if (group.equalsIgnoreCase(questVo.getGroupName())) {
		cnarmVos.add(questVo);
	    }
	}
	return cnarmVos;
    }
    
    /**
     * This utility method takes a collection of CnarmMappingConfigVo instances and
     * returns a sub-list that matches the attribute name.
     * 
     * @param cnarmMappingConfigVos - collection of CnarmMappingConfigVo instances
     * @param attribute - attribute type
     * @return sub list of cnarmMappingConfigVo instances that satisfies cnarmMapingConfigVo.attributeType = attribute
     */
    public static Collection<CnarmMappingConfigVo> getCnarmConfigVoByAttribute(Collection<CnarmMappingConfigVo> cnarmMappingConfigVos, String attribute) {
	List<CnarmMappingConfigVo> cnarmVos = new ArrayList<CnarmMappingConfigVo>();
	for (CnarmMappingConfigVo questVo : cnarmMappingConfigVos) {
	    if (attribute.equalsIgnoreCase(questVo.getBondlineAttributeType())) {
		cnarmVos.add(questVo);
	    }
	}
	return cnarmVos;
    }
    
    /**
     * This method will handle constraint on bondline attribute value.
     * 
     * @param bondlineAttributeValue - IBL attribute value
     * @param infoVo - any Vo object
     * @param cnarmObject - the CNARM Object
     * @param cnarmMappingConfigVo - CnarmMappingConfigVo
     * @param cnarmXpath - the CNARM XPath
     * @return converted bondlineAttributeValue according to constraint
     * @throws ServiceException
     */
    private Object handleConstraintName(Object bondlineAttributeValue, Object infoVo, Object cnarmObject, CnarmMappingConfigVo cnarmMappingConfigVo, String cnarmXpath) throws ServiceException {
	if (cnarmMappingConfigVo.getConstraintName().contains("customize")) {
	    bondlineAttributeValue = handleCustomizationForBondlineAttribute(bondlineAttributeValue, cnarmMappingConfigVo, new Object[] {bondlineAttributeValue, infoVo} );
	}
	return bondlineAttributeValue;
    }
    
    /**
     * This method will handle customization constraint on bondlineAttributeValue.
     * 
     * @param bondlineAttributeValue - IBL attribute value
     * @param cnarmMappingConfigVo - CnarmMappingConfigVo
     * @param args - method parameter values
     * @return the customized bondlineAttributeValue
     * @throws ServiceException
     */
    protected Object handleCustomizationForBondlineAttribute(Object bondlineAttributeValue, CnarmMappingConfigVo cnarmMappingConfigVo, Object[] args) throws ServiceException {
	String methodName = getMethodNameForConstraintName(cnarmMappingConfigVo);
	return invokeCustomizerMethod(methodName, bondlineAttributeValue, args);
    }
    
    /**
     * This method invokes the customizer method.
     * 
     * @param methodName - method name obtained from {@link #getMethodNameForConstraintName(CnarmMappingConfigVo)}
     * @param bondlineAttributeValue - IBL attribute value.
     * @param args - array of possible method argument values for customizer method.
     * @return the customized bondlineAttributeValue
     */
    private Object invokeCustomizerMethod(String methodName, Object bondlineAttributeValue, Object[] args) {
	Object instance = null;
	Class clazz = getCustomizerClassInstance();
	try {
	    Method m = getDeclaredMethod(clazz, methodName, new Class[] { bondlineAttributeValue.getClass() });
	    instance = m.invoke(null, new Object[] { bondlineAttributeValue });
	} catch (NoSuchMethodException nme) {
	    try {
	     instance = handleVarArgsMethod(methodName, args);
	    } catch(Exception e) {
		e.printStackTrace();
		LOGGER.error("Could not find method : " + clazz.getSimpleName() + "." + methodName);
	    }
	} catch (InvocationTargetException ite) {
	    System.out.println("Could not invoke customised method : Customizer." + methodName);
	    // ite.printStackTrace();
	} catch (Exception e) {
	    System.out.println("Other Exception : " + e.getClass());
	    // e.printStackTrace();
	}
	return instance;
    }
    
    /**
     * This method will be called if there is more than one argument in
     * the customizer methods
     *  
     * @param methodName - the method to be invoked
     * @param args - method parameter values
     * @return the customized bondlineAttributeValue
     * @throws Exception
     */
    private Object handleVarArgsMethod(String methodName, Object[] args) throws Exception {
	Class clazz = getCustomizerClassInstance();
	Object returnVal = null;
	Method[] methods = clazz.getDeclaredMethods();
	    for (Method method : methods) {
		if (methodName.equals(method.getName())) {
		    List argsList = new ArrayList();
		    Class<?>[] params = method.getParameterTypes();
		    for (Class<?> param : params) {
			for (Object o : args) {
			if (param.isInstance(o)) {
			    argsList.add(o);
			    break;
			}
			}
		    }
		    returnVal = method.invoke(null, argsList.toArray());
		    break;
		}
	    }
	return returnVal;
    }

    protected abstract String getMethodNameForConstraintName(CnarmMappingConfigVo cnarmMappingConfigVo);
    
    protected abstract Class getCustomizerClassInstance();
    
    protected abstract Class getUtilityClassInstance();
    
}
