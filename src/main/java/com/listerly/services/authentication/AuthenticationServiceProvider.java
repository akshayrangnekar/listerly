package com.listerly.services.authentication;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.listerly.entities.IUser;

public class AuthenticationServiceProvider {
		
	private Map<String, AuthenticationService> services;
	
	@Inject
	public AuthenticationServiceProvider(@Named("Facebook") AuthenticationService fbService,
			@Named("Twitter") AuthenticationService twitService,
			@Named("Google") AuthenticationService googleService, @Named("GooglePlus") AuthenticationService googlePlusService) {
		services = new HashMap<String, AuthenticationService>();
		services.put("Facebook", fbService);
		services.put("Twitter", twitService);
		services.put("GooglePlus", googlePlusService);
		services.put("Google", googleService);
	}
	
	private AuthenticationService getService(String name) {
		return services.get(name);
	}
	
	public URI getAuthenticationUrl(String type) {
		AuthenticationService service = getService(type);
		URI uri = service.getAuthenticationRedirectUrl();
		return uri;
	}
	
	public IUser getAuthenticatedUser(String type, String code) {
		AuthenticationService service = getService(type);
		IUser authenticatedUser = service.getAuthenticatedUser(code);
		return authenticatedUser;
	}
	
}
