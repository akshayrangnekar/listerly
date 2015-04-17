package com.listerly.filter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import com.listerly.dao.UserDAO;
import com.listerly.entities.IUser;
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
				IUser user = (IUser) obj;
				if (user.getId() == null) {
					log.fine("User not logged in. Throwing exception.");
					throw new ForbiddenException();
				}
			}
		}
    	
    }

    public static class UserOptionalRequestFilter implements ContainerRequestFilter {
        private final Logger log = getLogger(getClass().getName());

    	@Context HttpServletRequest req;
    	@Inject UserDAO userDAO;
    	
		@Override
		public void filter(ContainerRequestContext crc) throws IOException {
			log.finest("Inside filter");
			log.fine("Do I have a request session? " + req.getSession(false));
			SessionStore store = new SessionStore(req);
			Object userToken = store.get("token");
			if (userToken != null) {
				log.fine("Token in session: " + userToken);
				//IUser user = (IUser) userToken;
				IUser fromToken = userDAO.getUserFromToken(userToken.toString()); // TODO Maybe check the token na?
				if (fromToken != null) {
					log.fine("Got a user from Token: " + fromToken.getId());
					req.setAttribute("user", fromToken);
				} else {
					log.fine("No user matching token.");
				}
			}
			else {
				log.info("Don't seem to have a user.");
			}
		}
    	
    }
}
