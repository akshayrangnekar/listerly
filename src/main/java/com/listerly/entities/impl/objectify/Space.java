package com.listerly.entities.impl.objectify;

import java.util.ArrayList;
import java.util.Date;
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
import com.listerly.entities.IField;
import com.listerly.entities.ISpace;
import com.listerly.entities.ISpaceView;

@Entity
@Cache
public class Space implements ISpace {
	private static Logger log = Logger.getLogger(Space.class.getName());	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7842136513064948649L;
	
	@Id private Long id;
	@Unindex private String name;
	@Unindex private List<Field> fields = new ArrayList<>();
	@Index @Load List<Ref<Item>> items;
	@Index private Long parentId;
	@Unindex private Date created = new Date();
	@Unindex private List<SpaceView> views = new ArrayList<>();

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
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getCreated() {
		return created;
	}

	public List<? extends IField> getFields() {
		return fields;
	}
	
	@SuppressWarnings("unchecked")
	protected void setFields(List<? extends IField> fields) {
		this.fields = (List<Field>) fields;
	}

	@SuppressWarnings("unchecked")
	protected void addFieldSetting(IField field) {
		List<Field> fields = (List<Field>) getFields();		
		fields.add((Field) field);
	}
	@SuppressWarnings("unchecked")
	protected void addFieldSetting(int index, IField field) {
		List<Field> fields = (List<Field>) getFields();		
		fields.add(index, (Field) field);
	}
	
	@OnLoad void onLoadCallback() {
		log.fine("Calling onLoadCallback()");
		for (Field setting : fields) {
			log.fine("Deserializing a field setting.");
			setting.deserializeSettingsMap();
		}
	}
	
	@OnSave void onSaveCallback() {
		log.fine("Calling onSaveCallback()");
		for (Field setting : fields) {
			log.fine("Serializing a field setting.");
			setting.serializeSettingsMap();
		}
	}

	@Override
	public IField createField() {
		log.fine("Creating a new field setting");
		IField fs = new Field();
		addFieldSetting(fs);
		log.fine("Field settings now: " + getFields().size());
		return fs;
	}

	@Override
	public IField createField(int index) {
		log.fine("Creating a new field setting");
		IField fs = new Field();
		addFieldSetting(index, fs);
		log.fine("Field settings now: " + getFields().size());
		return fs;
	}

	@Override
	public List<? extends ISpaceView> getViews() {
		return views;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISpaceView createView() {
		SpaceView spaceView = new SpaceView();
		List<SpaceView> list = (List<SpaceView>) getViews();
		list.add(spaceView);
		return spaceView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISpaceView createView(int index) {
		SpaceView spaceView = new SpaceView();
		List<SpaceView> list = (List<SpaceView>) getViews();
		list.add(index, spaceView);;
		return spaceView;
	}
}
