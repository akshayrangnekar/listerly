package com.listerly.entities;

public interface ISpaceView {

	public String getUuid();

	public void setUuid(String uuid);

	public String getName();

	public void setName(String name);

	public String getPrimaryFieldUuid();

	public void setPrimaryFieldUuid(String primaryFieldUuid);

	public String getCheckboxFieldUuid();

	public void setCheckboxFieldUuid(String checkboxFieldUuid);

	public String getLayoutType();

	public void setLayoutType(String layoutType);

}