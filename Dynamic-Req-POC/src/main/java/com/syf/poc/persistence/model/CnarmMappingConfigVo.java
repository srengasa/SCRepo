package com.syf.poc.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This class is created to hold CNARM_MAPPING_CONFIG table record data.
 *
 */
@Entity
@Table(name="CNARM_MAPPING_CONFIG")
public class CnarmMappingConfigVo implements Serializable, Cloneable {

    private static final long serialVersionUID = -1779731224382503738L;
    @Id
    @GeneratedValue
    @Column(name="CNARM_MAPPING_CONFIG_KEY")
    private int cnarmKey;
    
    @Column(name="BONDLINE_ATTRIBUTE")
    private String bondlineAttribute;
    @Column(name="GROUP_NM")
    private String groupName;
    @Column(name="CNARM_ATTRIBUTE")
    private String cnarmAttribute;
    
    //private String cnarmQuestionType;
    @Column(name="BONDLINE_ATTRIBUTE_TYPE")
    private String bondlineAttributeType;
    @Column(name="GROUP_SEQ_NO")
    private String groupSeqNumber;
    @Column(name="CONSTRAINT_NM")
    private String constraintName;
    @Column(name="CONSTRAINT_ATTRIBUTE")
    private String constraintAttribute;
    @Column(name="CONSTRAINT_TARGET")
    private String constraintTarget;
    @Column(name="CONSTRAINT_ATTRIBUTE_TYPE")
    private String constraintAttributeType;
    
    public CnarmMappingConfigVo() {}
    
    public CnarmMappingConfigVo(String source, String destination, String attributeType) {
	this.bondlineAttribute = source;
	this.cnarmAttribute = destination;
	this.bondlineAttributeType = attributeType;
    }
    
    public CnarmMappingConfigVo(CnarmMappingConfigVo cnarmMappingConfigVo) {
	this.bondlineAttribute = cnarmMappingConfigVo.bondlineAttribute;
	this.cnarmAttribute = cnarmMappingConfigVo.cnarmAttribute;
	this.bondlineAttributeType = cnarmMappingConfigVo.bondlineAttributeType;
	this.groupName = cnarmMappingConfigVo.groupName;
	this.groupSeqNumber = cnarmMappingConfigVo.groupSeqNumber;
	this.constraintAttribute = cnarmMappingConfigVo.constraintAttribute;
	this.constraintTarget = cnarmMappingConfigVo.constraintTarget;
	this.constraintAttributeType = cnarmMappingConfigVo.constraintAttributeType;
	this.constraintName = cnarmMappingConfigVo.constraintName;
    }
    
    public String getBondlineAttribute() {
        return bondlineAttribute;
    }
    public void setBondlineAttribute(String bondlineAttribute) {
        this.bondlineAttribute = bondlineAttribute;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getCnarmAttribute() {
        return cnarmAttribute;
    }
    public void setCnarmAttribute(String cnarmAttribute) {
        this.cnarmAttribute = cnarmAttribute;
    }
    /*public String getCnarmQuestionType() {
        return cnarmQuestionType;
    }
    public void setCnarmQuestionType(String cnarmQuestionType) {
        this.cnarmQuestionType = cnarmQuestionType;
    }*/
    public String getBondlineAttributeType() {
        return bondlineAttributeType;
    }
    public void setBondlineAttributeType(String bondlineAttributeType) {
        this.bondlineAttributeType = bondlineAttributeType;
    }
    public String getGroupSeqNumber() {
        return groupSeqNumber;
    }
    public void setGroupSeqNumber(String groupSeqNumber) {
        this.groupSeqNumber = groupSeqNumber;
    }
    public String getConstraintName() {
        return constraintName;
    }
    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }
    public String getConstraintAttribute() {
        return constraintAttribute;
    }
    public void setConstraintAttribute(String constraintAttribute) {
        this.constraintAttribute = constraintAttribute;
    }
    public String getConstraintTarget() {
        return constraintTarget;
    }
    public void setConstraintTarget(String constraintTarget) {
        this.constraintTarget = constraintTarget;
    }
    public String getConstraintAttributeType() {
        return constraintAttributeType;
    }
    public void setConstraintAttributeType(String constraintAttributeType) {
        this.constraintAttributeType = constraintAttributeType;
    }

    public CnarmMappingConfigVo cloneObject()  {
	try {
	   return (CnarmMappingConfigVo) super.clone();
	} catch (CloneNotSupportedException cnse) {
	    return new CnarmMappingConfigVo(this.bondlineAttribute, this.cnarmAttribute, this.bondlineAttributeType);
	}
    }

	@Override
	public String toString() {
		return "CnarmMappingConfigVo [bondlineAttribute=" + bondlineAttribute + ", groupName=" + groupName
				+ ", cnarmAttribute=" + cnarmAttribute + ", bondlineAttributeType=" + bondlineAttributeType
				+ ", groupSeqNumber=" + groupSeqNumber + ", constraintName=" + constraintName + ", constraintAttribute="
				+ constraintAttribute + ", constraintTarget=" + constraintTarget + ", constraintAttributeType="
				+ constraintAttributeType + "]";
	}
    
}
