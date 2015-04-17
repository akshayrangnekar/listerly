package com.listerly.config.guice;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.logging.Logger.getLogger;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodWrapperExampleImpl implements MethodInterceptor {
    private final Logger log = getLogger(getClass().getName());

//	@Override
//	public void configure(final ResourceInfo resourceInfo, final FeatureContext configuration) {
//        AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
//
//        if (am.isAnnotationPresent(UserRequired.class) ||
//                resourceInfo.getResourceClass().isAnnotationPresent(UserRequired.class)) {
//            configuration.register(UserRequiredRequestFilter.class);
//        }
//	}
	
//	/** Work around java's annoying checked exceptions */
//	private static class ExceptionWrapper extends RuntimeException {
//		private static final long serialVersionUID = 1L;
//
//		public ExceptionWrapper(Throwable cause) {
//			super(cause);
//		}
//
//		/** This makes the cost of using the ExceptionWrapper negligible */
//		@Override
//		public synchronized Throwable fillInStackTrace() {
//			return this;
//		}
//	}
//
	

    @Target({TYPE, METHOD})
    @Retention(RUNTIME)
    public static @interface ExampleAOPAnnotation {
    }
    
//	public static class UserRequiredRequestFilter implements ContainerRequestFilter {
//		private static Logger log = Logger.getLogger(UserRequiredRequestFilter.class.getName());	
//
//		@Override
//		public void filter(ContainerRequestContext ctx) throws IOException {
//			log.fine("Filtering request for UserRequired");
//			Map<String, Cookie> cookies = ctx.getCookies();
//			for (String cookie : cookies.keySet()) {
//				log.fine("Cookie: " + cookie + " = " + cookies.get(cookie).getValue());
//			}
//			//log.fine("User is logged in: " + user.isLoggedIn());
//		}
//    }
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable {
		log.info("Yeah, in my filter.");
    	return inv.proceed();
	}

}
