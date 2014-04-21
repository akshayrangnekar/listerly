package com.listerly.config.guice;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Provider;
import com.listerly.apiobj.user.AUser;

public class UserProvider implements Provider<AUser>{
	static Logger log = Logger.getLogger(UserProvider.class.getName());	

	@Inject HttpServletRequest request;
	
	@Override
	public AUser get() {
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

}
