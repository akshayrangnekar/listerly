package com.listerly.apiobj.user;

public interface IAUser {

	public Long getId();

	public void setId(Long id);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getEmail();

	public void setEmail(String email);

	public String getProfileImageUrl();

	public void setProfileImageUrl(String profileImageUrl);

	public boolean isProfileComplete();

	public void setProfileComplete(boolean profileComplete);

	public boolean isLoggedIn();
}