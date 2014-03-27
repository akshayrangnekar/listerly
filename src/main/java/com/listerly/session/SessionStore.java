package com.listerly.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import com.google.inject.Inject;

public class SessionStore implements ISession {

	private HttpSession session;

	@Inject
	public SessionStore(@Context HttpServletRequest req) {
		this.session = req.getSession();
	}
	
	public void put(String key, Object obj) {
		session.setAttribute(key, obj);
	}
	
	public Object get(String key) {
		return session.getAttribute(key);
	}
}
