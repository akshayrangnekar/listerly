package com.listerly.entities;

import java.io.Serializable;

public interface IFieldValue extends Serializable {
	public String getFieldId();
	public void setFieldId(String in);

	public String getFieldValue();
	public void setFieldValue(String in);
	
//	public Long getFieldPriority();
//	public void setFieldPriority(Long in);
}
