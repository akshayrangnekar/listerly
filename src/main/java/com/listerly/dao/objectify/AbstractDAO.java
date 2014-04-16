package com.listerly.dao.objectify;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
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
		ofy().save().entity(t).now();
		return t;
	}
	
	@Transact(TxnType.REQUIRED)
	public List<? extends T> findAll() {
		List<? extends T> all= ofy().consistency(Consistency.STRONG).load().type(cls).list();
		return all;
	}
	
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
	
	public T findByField(String field, String value) {
		T user = ofy().consistency(Consistency.STRONG).load().type(cls).filter(field, value).first().now();
		return user;
	}

	public T findById(Long id) {
		Key<T> key = Key.create(cls, id);
		Result<T> t = ofy().load().key(key);
		return t.now();
	}
}
