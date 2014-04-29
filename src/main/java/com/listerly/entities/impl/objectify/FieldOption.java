package com.listerly.entities.impl.objectify;

import com.listerly.entities.IFieldOption;
import com.listerly.util.IDGenerator;

public class FieldOption implements IFieldOption {
	private String uuid;
	private String display;
	private String colorCode;
	
	public FieldOption() {
		setUuid(IDGenerator.str());
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String id) {
		this.uuid = id;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getColorCode() {
		return colorCode;
	}
	
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	
}
