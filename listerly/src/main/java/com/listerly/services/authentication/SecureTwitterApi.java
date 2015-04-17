package com.listerly.services.authentication;

import java.util.logging.Logger;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class SecureTwitterApi extends DefaultApi10a {
	private static final Logger log = Logger.getLogger(SecureTwitterApi.class.getName());	
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";
	private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
	private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";
	  
	@Override
	public String getAccessTokenEndpoint() {
		log.info("Getting access token endpoint.");
		return "https://" + ACCESS_TOKEN_RESOURCE;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		log.info("Getting authorization URL.");
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

	@Override
	public String getRequestTokenEndpoint() {
		log.info("Getting Request Token endpoint.");
		return "https://" + REQUEST_TOKEN_RESOURCE;
	}
}
