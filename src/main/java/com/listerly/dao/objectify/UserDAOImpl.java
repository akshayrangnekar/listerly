package com.listerly.dao.objectify;

import java.util.List;

import com.listerly.config.guice.MethodWrapperExampleImpl.ExampleAOPAnnotation;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.entities.impl.objectify.LoginToken;
import com.listerly.entities.impl.objectify.User;

public class UserDAOImpl extends AbstractDAO<IUser> implements UserDAO {
	
	public UserDAOImpl() {
		super(User.class);
	}
	
	protected static class TokenDAO extends AbstractDAO<LoginToken> {
		public TokenDAO() {
			super(LoginToken.class);
		}
		
		public LoginToken findByUuid(String inId) {
			return super.findByField("uuid", inId);
		}

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

	@Override
	public String createLoginToken(IUser user) {
		TokenDAO tokenDAO = new TokenDAO();
		LoginToken token = tokenDAO.create();
		token.setUserId(user.getId());
		tokenDAO.save(token);
		return token.getUuid();
	}

	@Override
	public IUser getUserFromToken(String loginToken) {
		TokenDAO tokenDAO = new TokenDAO();
		LoginToken token = tokenDAO.findByUuid(loginToken);
		if (token != null) {
			return this.findById(token.getId());
		}
		return null;
	}

	@ExampleAOPAnnotation
	@Override
	public List<? extends IUser> findAll() {
		return super.findAll();
	}
}
