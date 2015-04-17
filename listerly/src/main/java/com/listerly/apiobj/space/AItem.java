package com.listerly.apiobj.space;

import java.util.ArrayList;
import java.util.List;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.IFieldValue;
import com.listerly.entities.IItem;

public class AItem extends AbstractApiObject<IItem>{
	private Long id;
	private List<AFieldValue> values = new ArrayList<>();
	private Long spaceId;
	private Long parentId;
	
	public AItem(IItem in) {
		super(in);
		
		for (IFieldValue field : in.getFields()) {
			values.add(new AFieldValue(field));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<AFieldValue> getValues() {
		return values;
	}

	public void setValues(List<AFieldValue> values) {
		this.values = values;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	
}
