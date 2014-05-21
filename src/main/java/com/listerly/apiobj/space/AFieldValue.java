package com.listerly.apiobj.space;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.IFieldValue;

public class AFieldValue extends AbstractApiObject<IFieldValue>{
	private String fieldId;
	private String fieldValue;
	
	public AFieldValue(IFieldValue in) {
		super(in);
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}
