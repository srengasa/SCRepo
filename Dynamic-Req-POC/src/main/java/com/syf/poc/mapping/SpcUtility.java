package com.syf.poc.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class SpcUtility {

    public static BigInteger convertStringToBigInteger(String data) {
	return new BigInteger(data);
    }

    /*public static Amount convertStringToAmount(String amount) {
	return convertBigDecimalToAmount(new BigDecimal(amount));
    }

    public static Amount convertBigDecimalToAmount(BigDecimal amount) {
	Amount theAmount = new Amount();
	theAmount.setTheAmount(amount);
	theAmount.setTheUnit("USD");
	return theAmount;
    }*/

    public static BigInteger convertBigDecimalToBigInteger(BigDecimal b) {
	return new BigInteger(b.toString());
    }

    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(date);
	
	XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
	return xmlCalendar;
    }
    public static XMLGregorianCalendar convertStringToXMLGregorianCalendar(String date) throws DatatypeConfigurationException {
	XMLGregorianCalendar xmlCalendar = null;
	if(date!=null && date.contains("/"))
	    
	try {
	    GregorianCalendar cal = new GregorianCalendar();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    java.util.Date startDate = (java.util.Date)formatter.parse(date);
	    cal.setTime(startDate);
	    xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	    xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
	    xmlCalendar.setTime(DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	} catch (ParseException e) {
	} 
	
	return xmlCalendar;
    }
    public static  java.util.Date formatDate(String date) throws ParseException{
	 SimpleDateFormat originalFormat = new SimpleDateFormat("DD/MM/YYYY");
	   SimpleDateFormat targetFormat = new SimpleDateFormat("DD/MM/YYYY");
	   java.util.Date originalDate=null;
	   java.util.Date targetDate=null;
	  
	if(date!=null && date.contains("/")){
	
	     originalDate = originalFormat.parse(date);
	    String newDate= targetFormat.format(originalDate);
	     targetDate=targetFormat.parse(newDate);
	    
	   return targetDate;
	}else{
	      targetDate=(java.util.Date)targetFormat.parse(date);
	    return targetDate;
	}
	   
   }
    
    public static BigDecimal convertIntegerToBigDecimal(Integer i) {
	return new BigDecimal(i);
    }
    
    public static Boolean convertStringToBoolean(String s) {
	System.out.println("convertStringToBoolean : " + s);
	if (s != null && !"".equals(s)) {
	    return Boolean.TRUE;
	}
	return Boolean.FALSE;
    }
}
