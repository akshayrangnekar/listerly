package com.listerly.entities;

import java.io.Serializable;
import java.util.Map;

public interface IFieldSetting extends Serializable {
	public String getUuid();
	public void setUuid(String inUid);
	
	public String getName();
	public void setName(String name);

	public Boolean getListable();
	public void setListable(Boolean showable);

	public String getType();
	public void setType(String type);

	public Object getSetting(String inSetting);
	public void setSetting(String setting, Object value);
	
	public Map<String, Object> getAdditionalSettings();
}