package com.listerly.dao.objectify;

import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.entities.impl.objectify.User;

public class UserDAOImpl extends AbstractDAO<IUser> implements UserDAO {
	
	public UserDAOImpl() {
		super(User.class);
	}

	@Override
	public IUser findByFacebookId(String inId) {
		return super.findByField("facebookId", inId);
	}
	
	@Override
	public IUser findByGoogleId(String inId) {
		return super.findByField("googleId", inId);
	}
	
	@Override
	public IUser findByTwitterId(String inId) {
		return super.findByField("twitterId", inId);
	}

}
