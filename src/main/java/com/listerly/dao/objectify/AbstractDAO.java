package com.listerly.dao.objectify;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.objectify.TxnType;
import com.listerly.config.objectify.OfyTransactionInterceptor.Transact;

abstract class AbstractDAO<T> {
	private static Logger log = Logger.getLogger(AbstractDAO.class.getName());	
	
	private final Class<? extends T> cls;
	
	public AbstractDAO(Class<? extends T> cls) {
		this.cls = cls;
	}
	
	@Transact(TxnType.REQUIRED)
	public T save(T t) {
		ofy().save().entity(t);
		return t;
	}
	
	@Transact(TxnType.REQUIRED)
	public List<? extends T> findAll() {
		List<? extends T> all= ofy().load().type(cls).list();
		return all;
	}
	
//	@Transact(TxnType.REQUIRED) 
//	public List<T> findAllMethod2() {
//		Collection<? extends T> values = ofy().consistency(Consistency.EVENTUAL).load().keys(ofy().load().type(cls).keys()).values();
//		List<T> newVals = new ArrayList<>(values);
//		return newVals;
//	}
	
	public T create() {
		try {
			return this.cls.newInstance();
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "Unable to instantiate new object.", e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Unable to instantiate new object.", e);
		}
		return null;
	}
}
