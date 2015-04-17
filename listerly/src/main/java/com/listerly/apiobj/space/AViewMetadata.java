package com.listerly.apiobj.space;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.ISpaceView;

public class AViewMetadata extends AbstractApiObject<ISpaceView>{
	private String name;
	private String uuid;
	
	public AViewMetadata(ISpaceView spaceView){
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