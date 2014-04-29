package com.listerly.services;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
import com.listerly.session.SessionStore;

public class UserService {
	static Logger log = Logger.getLogger(UserService.class.getName());	

	@Inject HttpServletRequest request;
	@Inject UserDAO userDAO;
	@Inject SessionStore session;
	
	public IUser getRequestUser() {
		//log.info("Request: " + request);
		if (request != null) {
			Object object = request.getAttribute("user");
			if (object != null) {
				log.info("Returning an object: " + object);
				return (IUser) object;
			} else {
				log.fine("Unable to find a user in the request.");
			}
		} else {
			log.fine("Unable to find the request.");
		}
		return null;
	}
	
	public IUser setLoggedInUser(IUser pUser) {
		session.put("message", "Log in successful");
//		session.put("user", pUser);
//		session.put("userid", pUser.getId());
		log.fine("Put user: " + pUser.getId());
		String loginToken = userDAO.createLoginToken(pUser);
		session.put("token", loginToken);
		return pUser;
	}
	
	public void unsetLoggedInUser() {
		session.put("user", null); 
		session.put("token", null); // TODO: Yeah, may want to invalidate token too.
		request.setAttribute("user", null);
	}

}
