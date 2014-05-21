package com.listerly.entities;

import java.util.List;

public interface IItem extends BaseEntity{

	public Long getSpaceId();
	public void setSpaceId(Long spaceId);

	public Long getParentId();
	public void setParentId(Long parentId);

	public List<? extends IFieldValue> getFields();
//	public void addField(IFieldValue in);
	public IFieldValue createField();
}