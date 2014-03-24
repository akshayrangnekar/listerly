package com.listerly.entities.impl.objectify;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.listerly.entities.IUser;

@Entity
@Cache
public class User implements IUser {

	@Id
    private Long id;
	@Index private String facebookId;
	@Index private String googleId;
	@Index private String email;
	
	public User() {
	}

	public Long getId() {
		return id;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
