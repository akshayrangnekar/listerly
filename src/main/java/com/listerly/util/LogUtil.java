package com.listerly.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class LogUtil {
	private static Logger log = Logger.getLogger(LogUtil.class.getName());	
	
	private LogUtil() {
		
	}

	public static void debugPrint(String type, Object space) {
		if (!log.isLoggable(Level.FINE)) return;
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, space);
			log.info("Sending back " + type + ": " + sw.toString());
		} catch (IOException e) {
			log.log(Level.WARNING, "Unable to serialize " + type, e);
		}
	}

}
