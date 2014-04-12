package com.listerly.dao.objectify;

import java.util.List;

import com.listerly.dao.SpaceDAO;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.entities.impl.objectify.Item;
import com.listerly.entities.impl.objectify.Space;

public class SpaceDAOImpl extends AbstractDAO<ISpace> implements SpaceDAO {
	public SpaceDAOImpl() {
		super(Space.class);
	}

	@Override
	public IItem createItemInSpace(ISpace space) {
		Item item = new Item();
		return item;
	}

	@Override
	public List<? extends IItem> findAllCardsInSpace(ISpace space) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IItem saveItemInSpace(ISpace space, IItem item) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	
}
