package com.listerly.entities.impl.objectify;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.listerly.util.IDGenerator;

@Entity
@Cache
public class LoginToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3096608751047729456L;

	@Id private Long id;
	@Index private Long userId;
	@Index private String uuid;
	@Index private Date created;
	@Index private Date expires;
	
	public LoginToken() {
		created = new Date();
		uuid = IDGenerator.str();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	
}
