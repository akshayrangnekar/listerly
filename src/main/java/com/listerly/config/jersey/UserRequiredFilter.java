package com.listerly.config.jersey;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import com.listerly.apiobj.user.AUser;
import com.listerly.session.SessionStore;

public class UserRequiredFilter implements DynamicFeature {

	@Override
	public void configure(final ResourceInfo resourceInfo, final FeatureContext configuration) {
        AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());

        if (am.isAnnotationPresent(UserRequired.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(UserRequired.class)) {
            configuration.register(UserRequiredRequestFilter.class);
        }
        if (am.isAnnotationPresent(UserOptional.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(UserOptional.class)) {
            configuration.register(UserOptionalRequestFilter.class);
        }
	}
	

    @Target({TYPE, METHOD})
    @Retention(RUNTIME)
    public static @interface UserRequired {
    }
    
    @Target({TYPE, METHOD})
    @Retention(RUNTIME)
    public static @interface UserOptional {
    }
    
    public static class UserRequiredRequestFilter extends UserOptionalRequestFilter implements ContainerRequestFilter {
        private final Logger log = getLogger(getClass().getName());

    	@Context HttpServletRequest req;
    	
		@Override
		public void filter(ContainerRequestContext crc) throws IOException {
			super.filter(crc);
			Object obj = req.getAttribute("user");
			if (obj == null) {
				log.fine("No user found. Throwing exception.");
				throw new ForbiddenException();
			} else {
				AUser user = (AUser) obj;
				if (!user.isLoggedIn()) {
					log.fine("User not logged in. Throwing exception.");
					throw new ForbiddenException();
				}
			}
		}
    	
    }

    public static class UserOptionalRequestFilter implements ContainerRequestFilter {
        private final Logger log = getLogger(getClass().getName());

    	@Context HttpServletRequest req;
    	
		@Override
		public void filter(ContainerRequestContext crc) throws IOException {
			log.finest("Inside filter");
			log.fine("Do I have a request session? " + req.getSession(false));
			SessionStore store = new SessionStore(req);
			Object userObj = store.get("user");
			if (userObj != null) {
				AUser user = (AUser) userObj;
				String token = (String) store.get("token");
				log.fine("Do I have a user? " + user.isLoggedIn());
				log.fine("Do I have a token? " + token); // Maybe check the token na?
				req.setAttribute("user", user);
			}
			else {
				log.info("Don't seem to have a user.");
			}
		}
    	
    }
}
