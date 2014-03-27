package com.listerly.entities.impl.objectify;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Unindex;
import com.listerly.entities.IFieldSetting;
import com.listerly.entities.ISpace;

@Entity
@Cache
public class Space implements ISpace {
	private static Logger log = Logger.getLogger(Space.class.getName());	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7842136513064948649L;
	
	@Id private Long id;
	private String name;
	@Unindex private List<FieldSetting> fields = new ArrayList<>();
	@Index @Load List<Ref<Item>> items;

	public Space() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<? extends IFieldSetting> getFields() {
		return fields;
	}
	
	@SuppressWarnings("unchecked")
	public void setFields(List<? extends IFieldSetting> fields) {
		this.fields = (List<FieldSetting>) fields;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addFieldSetting(IFieldSetting field) {
		List<FieldSetting> fields = (List<FieldSetting>) getFields();		
		fields.add((FieldSetting) field);
	}
	
	@OnLoad void onLoadCallback() {
		log.fine("Calling onLoadCallback()");
		for (FieldSetting setting : fields) {
			setting.deserializeSettingsMap();
		}
	}
	
	@OnSave void onSaveCallback() {
		log.fine("Calling onSaveCallback()");
		for (FieldSetting setting : fields) {
			setting.serializeSettingsMap();
		}
	}
}
