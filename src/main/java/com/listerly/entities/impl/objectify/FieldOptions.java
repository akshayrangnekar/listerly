package com.listerly.entities.impl.objectify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import com.listerly.entities.IFieldOption;
import com.listerly.entities.IFieldOptions;

public class FieldOptions extends ArrayList<IFieldOption> implements IFieldOptions {
	private static Logger log = Logger.getLogger(FieldOptions.class.getName());	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8065242071452156291L;

	public FieldOptions() {
		super();
	}
	
	public FieldOptions(Collection<? extends IFieldOption> in) {
		super(in);
	}
	
	@Override
	public IFieldOption createAtEnd() {
		FieldOption option = new FieldOption();
		this.add(option);
		log.fine("Number of field options now: " + this.size());
		return option;
	}

	@Override
	public IFieldOption createAtIndex(int index) {
		FieldOption option = new FieldOption();
		this.add(index, option);
		log.fine("Number of field options now: " + this.size());
		return option;
	}

}
