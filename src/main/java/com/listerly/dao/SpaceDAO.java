package com.listerly.dao;

import java.util.List;

import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;

public interface SpaceDAO extends DAO<ISpace>{
	public IItem createItemInSpace(ISpace space);
	public List<? extends IItem> findAllCardsInSpace(ISpace space);
	public IItem saveItemInSpace(ISpace space, IItem item);
}
