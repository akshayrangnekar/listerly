package com.listerly.dao.objectify;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.listerly.config.guice.MethodWrapperExampleImpl.ExampleAOPAnnotation;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IAccessRule;
import com.listerly.entities.IUser;
import com.listerly.entities.impl.objectify.AccessRule;
import com.listerly.entities.impl.objectify.LoginToken;
import com.listerly.entities.impl.objectify.User;

public class UserDAOImpl extends AbstractDAO<IUser> implements UserDAO {
	static Logger log = Logger.getLogger(UserDAOImpl.class.getName());	
	
	public UserDAOImpl() {
		super(User.class);
	}
	
	protected static class TokenDAO extends AbstractDAO<LoginToken> {
		static Logger log = Logger.getLogger(TokenDAO.class.getName());	
		public TokenDAO() {
			super(LoginToken.class);
		}
		
		public LoginToken findByUuid(String inId) {
			LoginToken token = findByField("uuid", inId);
			
			if (token == null) {
				log.fine("Trying ok implementation.");
				token = okFindByUuid(inId);
			}
			if (token == null) {
				log.fine("Trying bad implementation.");
				token = badFindByUuid(inId);
			}
			return token;
		}
		
		public LoginToken okFindByUuid(String value) {
			List<LoginToken> list = ofy().consistency(Consistency.STRONG).load().type(LoginToken.class).filter("uuid", value).list();
			log.fine("Got back a list of " + list.size());
			LoginToken user = null;
			for (LoginToken token : list) {
				if (token.getUuid().equals(value)) {
					log.fine("Matched token: " + value);
					user = token;
				} else {
					log.fine("Didn't match token: " + token.getUuid());
				}
			}
			return user;
		}
		
		public LoginToken badFindByUuid(String value) {
			List<LoginToken> list = ofy().consistency(Consistency.STRONG).load().type(LoginToken.class).list();
			LoginToken user = null;
			for (LoginToken token : list) {
				if (token.getUuid().equals(value)) {
					log.fine("Matched token: " + value);
					user = token;
				} else {
					log.fine("Didn't match token: " + token.getUuid());
				}
			}
			return user;
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
		log.fine("I have a token to check: ***" + loginToken + "***");
		LoginToken token = tokenDAO.findByUuid(loginToken);
		if (token != null) {
			log.fine("Found a token");
			return this.findById(token.getUserId());
		}
		log.fine("Not found token");
		return null;
	}

	@ExampleAOPAnnotation
	@Override
	public List<? extends IUser> findAll() {
		return super.findAll();
	}

	@Override
	public List<? extends IAccessRule> findAllRulesForUser(IUser user) {
		User ruser = (User) user;
		Ref<User> userRef = Ref.create(ruser);
		Query<AccessRule> filter = ofy().consistency(Consistency.STRONG).load().type(AccessRule.class).filter("user", userRef);
		List<AccessRule> list = filter.list();
		return list;
	}
}
