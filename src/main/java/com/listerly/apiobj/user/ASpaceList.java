package com.listerly.apiobj.user;

import java.util.List;

public class ASpaceList {
	private List<ASpaceMetadata> spaces;
	
	public ASpaceList(List<ASpaceMetadata> spaces) {
		this.spaces = spaces;
	}
	
	public List<ASpaceMetadata> getSpaces() {
		return this.spaces;
	}
}
