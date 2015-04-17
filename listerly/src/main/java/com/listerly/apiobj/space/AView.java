package com.listerly.apiobj.space;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.ISpaceView;

public class AView extends AbstractApiObject<ISpaceView>{
	private String name;
	private String uuid;
	private String primaryFieldUuid;
	private String checkboxFieldUuid;
	private String layoutType;

	
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

	public AView(ISpaceView spaceView){
		super(spaceView);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}