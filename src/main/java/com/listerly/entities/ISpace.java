package com.listerly.entities;

import java.util.List;

public interface ISpace extends BaseEntity {
	public String getName();
	public void setName(String name);

	public List<? extends IFieldSetting> getFields();
	public void setFields(List<? extends IFieldSetting> fields);
	
	public void addFieldSetting(IFieldSetting field);
}
