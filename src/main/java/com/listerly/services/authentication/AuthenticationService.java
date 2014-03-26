package com.listerly.services.authentication;

import java.net.URI;

import com.listerly.entities.IUser;

public interface AuthenticationService {
	public URI getAuthenticationRedirectUrl();
	public IUser getAuthenticatedUser(String validationCode);
}
