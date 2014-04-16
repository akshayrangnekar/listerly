package com.listerly.config.guice;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.listerly.config.jersey.JerseyFilter;
import com.listerly.dao.SpaceDAO;
import com.listerly.dao.UserDAO;
import com.listerly.dao.objectify.SpaceDAOImpl;
import com.listerly.dao.objectify.UserDAOImpl;
import com.listerly.services.authentication.AuthenticationService;
import com.listerly.services.authentication.AuthenticationServiceProvider;
import com.listerly.services.authentication.FacebookAuthenticationService;
import com.listerly.services.authentication.GoogleAuthenticationService;
import com.listerly.services.authentication.GooglePlusAuthenticationService;
import com.listerly.services.authentication.TwitterAuthenticationService;
import com.listerly.session.ISession;
import com.listerly.session.SessionStore;

public class ListerlyServletModule extends ServletModule {

	@Override
	protected void configureServlets() {

		bindFiltersAndServlets();
		bindSession();
		bindDAO();
		bindAuthentication();
		
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
	
	private void bindAuthentication() {
	    bind(AuthenticationServiceProvider.class);
	    bind(AuthenticationService.class).annotatedWith(Names.named("Facebook")).to(FacebookAuthenticationService.class);
	    bind(AuthenticationService.class).annotatedWith(Names.named("Twitter")).to(TwitterAuthenticationService.class);
	    bind(AuthenticationService.class).annotatedWith(Names.named("Google")).to(GoogleAuthenticationService.class);
	    bind(AuthenticationService.class).annotatedWith(Names.named("GooglePlus")).to(GooglePlusAuthenticationService.class);
	}

	private void bindDAO() {
		bind(UserDAO.class).to(UserDAOImpl.class);
		bind(SpaceDAO.class).to(SpaceDAOImpl.class);
	}
	
	private void bindSession() {
		bind(ISession.class).to(SessionStore.class);
	}

	protected void bindFiltersAndServlets() {
	    bind(ObjectifyFilter.class).in(Singleton.class);
	    bind(AppstatsFilter.class).in(Singleton.class);
	    bind(AppstatsServlet.class).in(Singleton.class);
	}
}
