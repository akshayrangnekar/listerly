package com.listerly.config.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.listerly.entities.impl.objectify.AccessRule;
import com.listerly.entities.impl.objectify.Item;
import com.listerly.entities.impl.objectify.LoginToken;
import com.listerly.entities.impl.objectify.Space;
import com.listerly.entities.impl.objectify.User;

public class OfyService {
	static {
		factory().register(User.class);
		factory().register(LoginToken.class);
		factory().register(Space.class);
		factory().register(Item.class);
		factory().register(AccessRule.class);
	}
	
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory()  {
		return ObjectifyService.factory();
	}
}
