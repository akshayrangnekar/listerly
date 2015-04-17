package com.listerly.entities;

import java.util.Date;

public interface IUser extends BaseEntity {
	public String getFacebookId();
	public void setFacebookId(String facebookId);

	public String getGoogleId();
	public void setGoogleId(String googleId);
	
	public String getTwitterId();
	public void setTwitterId(String twitterId);

	public String getFirstName();
	public void setFirstName(String name);

	public String getLastName();
	public void setLastName(String name);

	public String getEmail();
	public void setEmail(String email);
	
	public String getProfileImageUrl();
	public void setProfileImageUrl(String in);
	
	public Date getCreated();
}
