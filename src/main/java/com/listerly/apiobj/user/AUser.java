package com.listerly.apiobj.user;

import java.io.Serializable;

import com.listerly.entities.IUser;

public class AUser extends AbstractApiObject<IUser> implements Serializable, IAUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String profileImageUrl;
	private boolean profileComplete;
	private boolean isLoggedIn;
	
	public AUser(IUser persistent) {
		super(persistent);
		
		isLoggedIn = true;
		if (firstName != null && lastName != null && email != null) {
			setProfileComplete(true);
		} else {
			setProfileComplete(false);
		}
	}
	
	public AUser() {
		isLoggedIn = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public boolean isProfileComplete() {
		return profileComplete;
	}

	public void setProfileComplete(boolean profileComplete) {
		this.profileComplete = profileComplete;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}


}
