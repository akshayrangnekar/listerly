package com.listerly.entities;

import java.util.List;

import com.listerly.entities.impl.objectify.FieldValue;

public interface IItem extends BaseEntity{

	public Long getSpaceId();

	public void setSpaceId(Long spaceId);

	public List<? extends FieldValue> getFields();

	public void addField(FieldValue in);
	//public void setFields(List<? extends FieldValue> fields);

}