package com.listerly.entities;

import java.io.Serializable;
import java.util.List;

public interface IField extends Serializable {
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
	
	public List<? extends IFieldOption> getFieldOptions();
	public IFieldOption createOption();
}