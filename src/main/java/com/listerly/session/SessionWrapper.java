package com.listerly.session;

import java.util.logging.Logger;

import com.google.inject.Inject;

public class SessionWrapper {
	private static Logger log = Logger.getLogger(SessionWrapper.class.getName());	

	private ISession session;
	
	@Inject
	public SessionWrapper(ISession store) {
		log.info("Creating a new session wrapper for: " + store);
		session = store;
	}
	
	public ISession getStorex() {
		return session;
	}
}
