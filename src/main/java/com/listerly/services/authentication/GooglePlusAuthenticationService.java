package com.listerly.services.authentication;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import com.listerly.entities.IUser;

public class GooglePlusAuthenticationService implements AuthenticationService {

	@Override
	public URI getAuthenticationRedirectUrl() {
		OAuthService service = new ServiceBuilder()
	        .provider(SecureGooglePlusApi.class)
	        .apiKey("62427970332.apps.googleusercontent.com")
	        .apiSecret("gGfcmpChdgxjidrnSFFU-OUw")
	        .scope("https://www.googleapis.com/auth/plus.login")
	        .build();	
		
		String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
		Token requestToken = service.getRequestToken();
		String authorizationUrl = (AUTHORIZE_URL + requestToken.getToken());
		URI uri = UriBuilder.fromUri(authorizationUrl).build();
		return uri;

	}

	@Override
	public IUser getAuthenticatedUser(String validationCode) {
		return null;
	}

}
