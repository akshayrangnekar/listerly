package com.listerly.services;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.listerly.apiobj.user.AUser;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.session.SessionStore;

public class UserService {
	static Logger log = Logger.getLogger(UserService.class.getName());	

	@Inject HttpServletRequest request;
	@Inject UserDAO userDAO;
	@Inject SessionStore session;
	
	public AUser getRequestUser() {
		//log.info("Request: " + request);
		if (request != null) {
			Object object = request.getAttribute("user");
			if (object != null) {
				log.info("Returning an object: " + object);
				return (AUser) object;
			} else {
				log.fine("Unable to find a user in the request.");
			}
		} else {
			log.fine("Unable to find the request.");
		}
		return new AUser();
	}
	
	public AUser setLoggedInUser(IUser pUser) {
		session.put("message", "Log in successful");
		AUser aUser = new AUser(pUser);
		session.put("user", aUser);
		session.put("userid", pUser.getId());
		log.fine("Put user: " + aUser.getId());
		String loginToken = userDAO.createLoginToken(pUser);
		session.put("token", loginToken);
		return aUser;
	}

}
