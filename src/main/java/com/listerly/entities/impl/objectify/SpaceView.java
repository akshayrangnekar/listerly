package com.listerly.entities.impl.objectify;

import com.listerly.entities.ISpaceView;
import com.listerly.util.IDGenerator;

public class SpaceView implements ISpaceView {
	private String uuid;
	private String name;
	private String primaryFieldUuid;
	private String checkboxFieldUuid;
	private String layoutType;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrimaryFieldUuid() {
		return primaryFieldUuid;
	}

	public void setPrimaryFieldUuid(String primaryFieldUuid) {
		this.primaryFieldUuid = primaryFieldUuid;
	}

	public String getCheckboxFieldUuid() {
		return checkboxFieldUuid;
	}

	public void setCheckboxFieldUuid(String checkboxFieldUuid) {
		this.checkboxFieldUuid = checkboxFieldUuid;
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	public SpaceView() {
		this.uuid = IDGenerator.str();
	}
}
