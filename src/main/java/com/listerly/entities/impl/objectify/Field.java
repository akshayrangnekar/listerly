package com.listerly.entities.impl.objectify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.annotation.Ignore;
import com.listerly.entities.IField;
import com.listerly.entities.IFieldOptions;
import com.listerly.util.IDGenerator;

public class Field implements IField {
	private static Logger log = Logger.getLogger(Field.class.getName());	
	/**
	 * 
	 */
	private static final long serialVersionUID = 29021641246155143L;
	private static final String kSettingsField = "__FIELD_SETTINGS";

	protected String uuid;
	protected String name;
	protected Boolean listable;
	protected String type;
	@Ignore protected Map<String, Object> additionalSettings = new HashMap<String, Object>();
	protected String serializedSettings;
	
	public Field() {
		setUuid(IDGenerator.str());
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getListable() {
		return listable;
	}

	public void setListable(Boolean listable) {
		this.listable = listable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public IFieldOptions getFieldOptions() {
		FieldOptions fo = null;
		Object object = this.additionalSettings.get(kSettingsField);
		if (object instanceof FieldOptions) {
			fo = (FieldOptions) object;
		}
		if (fo == null) {
			fo = new FieldOptions();
			this.additionalSettings.put(kSettingsField, fo);
		}
		return fo;
	}

	protected Map<String, Object> getAdditionalSettings() {
		return additionalSettings;
	}

	protected void setAdditionalSettings(Map<String, Object> additionalSettings) {
		this.additionalSettings = additionalSettings;
	}
	
	public Object getSetting(String inSetting) {
		return getAdditionalSettings().get(inSetting);
	}
	
	public void setSetting(String setting, Object value) {
		getAdditionalSettings().put(setting, value);
	}
	
	public int getSettingCount() {
		return getAdditionalSettings().size();
	}
	
	protected void deserializeSettingsMap() {
		if (this.serializedSettings != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = mapper.readValue(this.serializedSettings, HashMap.class);
				Object fieldSettingsStr = map.get(kSettingsField);
				log.fine("Field Options String:" + fieldSettingsStr);
				if (fieldSettingsStr != null) {
					@SuppressWarnings("unchecked")
					ArrayList<FieldOption> afo = mapper.readValue((String)fieldSettingsStr, ArrayList.class);
					FieldOptions fo = new FieldOptions(afo);
					map.put(kSettingsField, fo);
				}
				setAdditionalSettings(map);
			} catch (JsonParseException e) {
				log.warning("Unable to parse string for additional settings.");
			} catch (IOException e) {
				log.warning("Unable to parse string for additional settings.");
			}
		}
	}
	
	protected void serializeSettingsMap() {
		if (getAdditionalSettings().size() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				IFieldOptions fieldOptions = getFieldOptions();
				log.fine("Field Options Size: " + fieldOptions.size());
				String fieldOptionsStr = mapper.writeValueAsString(fieldOptions);
				log.fine("Field Options String: " + fieldOptionsStr);
				Map<String, Object> additionalSettings = getAdditionalSettings();
				additionalSettings.put(kSettingsField, fieldOptionsStr);
				String valueAsString = mapper.writeValueAsString(additionalSettings);
				this.serializedSettings = valueAsString;
			} catch (JsonProcessingException e) {
				log.warning("Unable to write string for additional settings.");
			}
		}
	}
}
