package com.listerly.entities;

public interface IUser extends BaseEntity {
	public String getFacebookId();
	public void setFacebookId(String facebookId);

	public String getGoogleId();
	public void setGoogleId(String googleId);
	
	public String getTwitterId();
	public void setTwitterId(String twitterId);

	public String getName();
	public void setName(String name);

	public String getEmail();
	public void setEmail(String email);
}
