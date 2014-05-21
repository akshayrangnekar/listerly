package com.listerly.apiobj.space;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.IField;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.entities.ISpaceView;

public class ASpace extends AbstractApiObject<ISpace> {
	private static Logger log = Logger.getLogger(ASpace.class.getName());	
	
	private List<AField> fields;
	private Long id;
	private String name;
	private List<AView> views;
	private List<AItem> items;

	public ASpace(ISpace space, List<? extends IItem> items) {
		super(space);
		
		this.fields = new ArrayList<>();
		for (IField field : space.getFields()) {
			this.fields.add(new AField(field));
		}
		
		this.views = new ArrayList<>();
		for (ISpaceView view : space.getViews()) {
			this.views.add(new AView(view));
		}

		this.items = new ArrayList<>();
		for (IItem item : items) {
			log.fine("Creating new item for " + item.getId());
			this.items.add(new AItem(item));
		}
		
		log.fine("Number of items in ASpace " + this.items.size());
	}

	public List<AItem> getItems() {
		return items;
	}

	public void setItems(List<AItem> items) {
		this.items = items;
	}

	public List<AField> getFields() {
		return fields;
	}

	public void setFields(List<AField> fields) {
		this.fields = fields;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<AView> getViews() {
		return views;
	}

	public void setViews(List<AView> views) {
		this.views = views;
	}

}
