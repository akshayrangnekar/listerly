package com.listerly.apiobj.space;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.listerly.apiobj.AbstractApiObject;
import com.listerly.entities.IField;
import com.listerly.entities.IFieldOption;

public class AField extends AbstractApiObject<IField> {
	private static Logger log = Logger.getLogger(AField.class.getName());	
	protected String uuid;
	protected String name;
	protected String type;
	protected List<AFieldOption> options;

	public AField(IField in) {
		super(in);
		
		List<? extends IFieldOption> options = in.getFieldOptions();
		if (options != null) {
			this.options = new ArrayList<>();
			for (int i = 0; i < options.size(); i++) {
				IFieldOption option = options.get(i);
				log.finer("Creating new option for " + option.getDisplay());
				this.options.add(new AFieldOption(option));
			}
		}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<AFieldOption> getOptions() {
		return options;
	}

	public void setOptions(List<AFieldOption> options) {
		this.options = options;
	}

	public static class AFieldOption extends AbstractApiObject<IFieldOption> {
		private String uuid;
		private String display;
		private String colorCode;

		public AFieldOption(IFieldOption option) {
			super(option);
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getDisplay() {
			return display;
		}

		public void setDisplay(String display) {
			this.display = display;
		}

		public String getColorCode() {
			return colorCode;
		}

		public void setColorCode(String colorCode) {
			this.colorCode = colorCode;
		}
		
		
	}
}
