package com.listerly.dao.objectify;

import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.entities.impl.objectify.User;

public class UserDAOImpl extends AbstractDAO<IUser> implements UserDAO {
	
	public UserDAOImpl() {
		super(User.class);
	}
}
