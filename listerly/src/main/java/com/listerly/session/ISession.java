package com.listerly.session;


public interface ISession {
	public void put(String key, Object obj);
	public Object get(String key);
}