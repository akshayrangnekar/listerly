package com.listerly.entities.impl.objectify;

import java.io.Serializable;
import java.util.logging.Logger;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.listerly.entities.IAccessRule;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;

@Entity
public class AccessRule implements IAccessRule, Serializable {
	private static Logger log = Logger.getLogger(AccessRule.class.getName());	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7842136513064948649L;
	
	@Id private Long id;
	@Index @Load private Ref<Space> space;
	@Index @Load private Ref<User> user;
	private AccessLevel level;
	
	public AccessRule() {
		log.finest("Constructing AccessRule");
	}
	
	public AccessRule(Space space, User user, AccessLevel level) {
		this.space = Ref.create(space);
		this.user = Ref.create(user);
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	@Override
	public ISpace getSpace() {
		return space.get();
	}

	@Override
	public IUser getUser() {
		return user.get();
	}

	@Override
	public AccessLevel getLevel() {
		return level;
	}
}
