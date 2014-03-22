package com.listerly.services;

import java.util.HashMap;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.oauth.OAuthService;

import com.google.inject.Singleton;

@Singleton
public class AuthenticationServiceProvider {
	
	private static final String kFACEBOOK_API_KEY = "1413462062246104";
	private static final String kFACEBOOK_API_SECRET = "f57327ea04814261007164d654125e2b";
	private static final String kFACEBOOK_SCOPES = "basic_info,email";
	
	private Map<String, OAuthService> services;
	
	public AuthenticationServiceProvider() {
		services = new HashMap<String, OAuthService>();
		
		OAuthService service = new ServiceBuilder()
			.provider(FacebookApi.class)
			.apiKey(kFACEBOOK_API_KEY)
			.apiSecret(kFACEBOOK_API_SECRET)
			.scope(kFACEBOOK_SCOPES)
			.callback("http://annkh-source.appspot.com/")
			.build();

		services.put("facebook", service);
	}
	
	public OAuthService getService(String name) {
		return services.get(name);
	}
}
