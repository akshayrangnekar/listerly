package com.listerly.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SimpleTestEntity {

	@Id
    private Long id;
    private String name;
    private Date creationDate = new Date();

    public SimpleTestEntity() {
    }

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}
}
