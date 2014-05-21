package com.listerly.entities;

import java.util.List;

public interface IFieldOption {
	public String getUuid();
	public void setUuid(String id);

	public String getDisplay();
	public void setDisplay(String display);

	public String getColorCode();
	public void setColorCode(String colorCode);

	public List<String> getUuidOrder();
	public void setUuidOrder(List<String> uuidOrder);
}