package com.listerly.entities;

public interface IAccessRule {
	public static enum AccessLevel {
		READONLY, READWRITE, PARTIAL
	}
	
	public ISpace getSpace();
	public IUser getUser();
	public AccessLevel getLevel();
}
