package com.listerly.config.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.listerly.entities.SimpleTestEntity;

public class OfyService {
	static {
		factory().register(SimpleTestEntity.class);
	}
	
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory()  {
		return ObjectifyService.factory();
	}
}
