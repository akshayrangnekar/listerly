package com.listerly.dao.objectify;

import java.util.List;
import java.util.UUID;

import com.listerly.dao.SpaceDAO;
import com.listerly.entities.IFieldSetting;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.entities.impl.objectify.FieldSetting;
import com.listerly.entities.impl.objectify.Item;
import com.listerly.entities.impl.objectify.Space;

public class SpaceDAOImpl extends AbstractDAO<ISpace> implements SpaceDAO {
	public SpaceDAOImpl() {
		super(Space.class);
	}

	@Override
	public IFieldSetting createField() {
		FieldSetting setting = new FieldSetting();
		setting.setUuid(UUID.randomUUID().toString());
		return setting;
	}

	@Override
	public IItem createItemInSpace(ISpace space) {
		Item item = new Item();
		return null;
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
