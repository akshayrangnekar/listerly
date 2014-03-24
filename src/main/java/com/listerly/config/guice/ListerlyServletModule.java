package com.listerly.config.guice;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.listerly.config.jersey.JerseyFilter;
import com.listerly.dao.UserDAO;
import com.listerly.dao.objectify.UserDAOImpl;
import com.listerly.services.AuthenticationServiceProvider;

public class ListerlyServletModule extends ServletModule {

	@Override
	protected void configureServlets() {

		bindClasses();

	    Map<String, String> jerseyParams = new HashMap<String, String>();
		jerseyParams.put("javax.ws.rs.Application", "com.listerly.config.jersey.JerseyConfiguration");
		jerseyParams.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature");

		Map<String, String> appstatsParams = new HashMap<>();
		appstatsParams.put("calculateRpcCosts", "true");
		appstatsParams.put("requireAdminAuthentication", "false");
		
		filter("/*").through(AppstatsFilter.class);
		filter("/*").through(ObjectifyFilter.class);
		filter("/*").through(JerseyFilter.class, jerseyParams);

	    serve("/appstats/*").with(AppstatsServlet.class);
	}
	
	protected void bindClasses() {
	    bind(AuthenticationServiceProvider.class);
	    bind(ObjectifyFilter.class).in(Singleton.class);
	    bind(AppstatsFilter.class).in(Singleton.class);
	    bind(AppstatsServlet.class).in(Singleton.class);
		bind(UserDAO.class).to(UserDAOImpl.class);
	}
}
