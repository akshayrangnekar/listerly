package com.listerly.apiobj.user;

import java.util.ArrayList;
import java.util.List;

import com.listerly.entities.IField;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;

public class ASpace extends ASpaceMetadata {
	private List<AField> fields;
	
	public List<AField> getFields() {
		return fields;
	}

	public void setFields(List<AField> fields) {
		this.fields = fields;
	}

	public ASpace(ISpace space, List<? extends IItem> items) {
		super(space);
		
		this.fields = new ArrayList<>();
		for (IField field : space.getFields()) {
			this.fields.add(new AField(field));
		}
	}

}
