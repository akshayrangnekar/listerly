package com.listerly.entities.impl.objectify;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.listerly.entities.IUser;

@Entity
@Cache
public class User implements IUser {
	private static final long serialVersionUID = -6904583655281747352L;

	@Id
    private Long id;
	@Index private String facebookId;
	@Index private String googleId;
	@Index private String twitterId;
	@Index private String email;
	@Unindex private String firstName;
	@Unindex private String lastName;
	@Unindex private Date created = new Date();
	@Unindex private String profileImageUrl;
	
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

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getCreated() {
		return created;
	}

	@Override
	public String getProfileImageUrl() {
		return "/assets/avatars/profile-picture-default.png";
	}

	@Override
	public void setProfileImageUrl(String in) {
		profileImageUrl = in;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
}
