package com.listerly.services.authentication;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;

public class GoogleAuthenticationService implements AuthenticationService {
	private static Logger log = Logger.getLogger(GoogleAuthenticationService.class.getName());	
	@Inject UserDAO userDAO;

	@Override
	public URI getAuthenticationRedirectUrl() {
		String loginURL = UserServiceFactory.getUserService().createLoginURL("/authenticate/google/callback/");
		URI uri = UriBuilder.fromUri(loginURL).build();
		return uri;
	}

	@Override
	public IUser getAuthenticatedUser(String email) {
	    log.info(String.format("Got back a response with email: %s", email));
	    if (email == null) return null;
	    
	    IUser userc = userDAO.findByGoogleId(email);
	    if (userc == null) {
	    	log.info("Creating a new user.");
	    	IUser created = userDAO.create();
	    	created.setEmail(email);
	    	created.setName(email);
	    	created.setGoogleId(email);
	    	userc = userDAO.save(created);
	    } else {
	    	log.info("Found existing user.");
	    }
	    
	    return userc;
	}

}
