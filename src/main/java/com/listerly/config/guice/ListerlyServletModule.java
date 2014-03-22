package com.listerly.config.guice;

import com.google.inject.servlet.ServletModule;
import com.listerly.SecondTest;
import com.listerly.TestClass;
import com.listerly.services.AuthenticationServiceProvider;

public class ListerlyServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
//	    serve("/hi").with(HelloWorldServlet.class);
	    bind(SecondTest.class);
	    bind(TestClass.class);
	    bind(AuthenticationServiceProvider.class);
	}
}
