package com.listerly.dao;

import com.listerly.entities.IUser;

public interface UserDAO extends DAO<IUser> {
	public IUser findByFacebookId(String inID);
	public IUser findByGoogleId(String inId);
	public IUser findByTwitterId(String inId);
	
	public String createLoginToken(IUser user);
	public IUser getUserFromToken(String loginToken);
}
