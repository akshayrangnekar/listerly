package com.listerly.apiobj.space;

import java.util.List;

public class ASpaceMetadataList {
	private List<ASpaceMetadata> spaces;
	
	public ASpaceMetadataList(List<ASpaceMetadata> spaces) {
		this.spaces = spaces;
	}
	
	public List<ASpaceMetadata> getSpaces() {
		return this.spaces;
	}
}
