package com.listerly.config.objectify;

import static com.listerly.config.objectify.OfyService.ofy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;

public class OfyTransactionInterceptor implements MethodInterceptor {

	private static class ExceptionWrapper extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ExceptionWrapper(Throwable cause) {
			super(cause);
		}

		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}
	
	@Override
	public Object invoke(final MethodInvocation inv) throws Throwable {
		Transact attr = inv.getStaticPart().getAnnotation(Transact.class);
		TxnType type = attr.value();

		try {
			return ofy().execute(type, new Work<Object>() {
				@Override
				public Object run() {
					try {
						return inv.proceed();
					} 
					catch (RuntimeException re) { throw re; }
					catch (Throwable e) { throw new ExceptionWrapper(e); }
					
				}
			});
		} catch (ExceptionWrapper exw) {
			throw exw.getCause();
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface Transact {
		TxnType value();
	}
}
