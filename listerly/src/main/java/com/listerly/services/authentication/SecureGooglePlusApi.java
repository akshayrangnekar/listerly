package com.listerly.services.authentication;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;

public class SecureGooglePlusApi extends DefaultApi20 {

	@Override
	public String getAccessTokenEndpoint() {
		return null;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig arg) {
		return null;
	}

}
