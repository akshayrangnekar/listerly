package com.listerly.dao;

import java.util.List;

import com.listerly.entities.IAccessRule;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;
import com.listerly.entities.IAccessRule.AccessLevel;

public interface SpaceDAO extends DAO<ISpace>{
	public IItem createItemInSpace(ISpace space);
	public List<? extends IItem> findAllCardsInSpace(ISpace space);
	public IItem saveItemInSpace(ISpace space, IItem item);
	
	public List<? extends IAccessRule> findAllRulesForSpace(ISpace space);
	public void createAccessRule(ISpace space, IUser user, AccessLevel level);
}
