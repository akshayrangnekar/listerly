package com.listerly.dao;

import java.util.List;

public interface DAO<T> {
	public T save(T in);
	public List<? extends T> findAll();
	public T create();
	public T findById(Long id);
}
