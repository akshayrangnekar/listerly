package com.listerly.config.guice;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.listerly.SecondTest;
import com.listerly.TestClass;
import com.listerly.config.jersey.JerseyFilter;
import com.listerly.services.AuthenticationServiceProvider;

public class ListerlyServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
//	    serve("/hi").with(HelloWorldServlet.class);
		
	    bind(SecondTest.class);
	    bind(TestClass.class);
	    bind(AuthenticationServiceProvider.class);
	    bind(ObjectifyFilter.class).in(Singleton.class);

	    Map<String, String> params = new HashMap<String, String>();
		params.put("javax.ws.rs.Application", "com.listerly.config.jersey.JerseyConfiguration");
		params.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature");
		
		filter("/*").through(ObjectifyFilter.class);
		filter("/*").through(JerseyFilter.class, params);
	}
}
