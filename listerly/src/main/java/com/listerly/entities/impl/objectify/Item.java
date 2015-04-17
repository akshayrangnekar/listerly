package com.listerly.entities.impl.objectify;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.listerly.entities.IFieldValue;
import com.listerly.entities.IItem;

@Entity
@Cache
public class Item implements IItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3096608751047729456L;

	@Id private Long id;
	private List<FieldValue> fields = new ArrayList<>();
	@Index private Long spaceId;
	private Long parentId;

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

	public Long getId() {
		return id;
	}

	public List<? extends FieldValue> getFields() {
		return fields;
	}

	protected void addField(IFieldValue in) {
		this.fields.add((FieldValue)in);
	}

	@Override
	public IFieldValue createField() {
		FieldValue fieldValue = new FieldValue();
		addField(fieldValue);
		return fieldValue;
	}
	
	

}
