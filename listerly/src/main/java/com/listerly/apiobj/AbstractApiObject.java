package com.listerly.apiobj;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;

public abstract class AbstractApiObject<T> {
	private static Logger log = Logger.getLogger(AbstractApiObject.class.getName());	

	public AbstractApiObject() {
	}
	
	public AbstractApiObject(T in) {
		try {
			BeanUtils.copyProperties(this, in);
		} catch (IllegalAccessException e) { 
			log.log(Level.WARNING, "Failure creating obj", e);
		} catch (InvocationTargetException e) {
			log.log(Level.WARNING, "Failure creating obj", e);
		}
	}
}
