package com.listerly.entities.impl.objectify;

import com.listerly.entities.IFieldValue;

public class FieldValue implements IFieldValue {
	private static final long serialVersionUID = 1L;
	
	String fieldId;
	String fieldValue;
	Long fieldPriority;
	
	@Override
	public String getFieldId() {
		return fieldId;
	}
	@Override
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	@Override
	public String getFieldValue() {
		return fieldValue;
	}
	@Override
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	@Override
	public Long getFieldPriority() {
		return fieldPriority;
	}
	@Override
	public void setFieldPriority(Long fieldPriority) {
		this.fieldPriority = fieldPriority;
	}

}
