package com.listerly.entities;

public interface IUser extends BaseEntity {
	public String getFacebookId();
	public void setFacebookId(String facebookId);
	public String getEmail();
	public void setEmail(String email);
}
