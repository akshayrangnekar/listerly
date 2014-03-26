package com.listerly.session;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import com.google.inject.Inject;

public class SessionStore implements ISession {
	private static Logger log = Logger.getLogger(SessionStore.class.getName());	

	private HttpSession session;

	@Inject
	public SessionStore(@Context HttpServletRequest req) {
		this.session = req.getSession();
		log.info("Creating a new SessionStore.");
	}
	
	public void put(String key, Object obj) {
		session.setAttribute(key, obj);
	}
	
	public Object get(String key) {
		return session.getAttribute(key);
	}
}
