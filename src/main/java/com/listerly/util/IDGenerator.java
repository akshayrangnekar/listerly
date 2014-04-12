package com.listerly.util;

import java.util.UUID;

public class IDGenerator {
	private IDGenerator() {
		
	}
	
	public static String str() {
		return UUID.randomUUID().toString();
	}
}
