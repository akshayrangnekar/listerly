package com.listerly.dao.objectify;

import static com.listerly.config.objectify.OfyService.ofy;

import java.util.List;

import com.google.appengine.api.datastore.ReadPolicy.Consistency;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.listerly.dao.SpaceDAO;
import com.listerly.entities.IAccessRule;
import com.listerly.entities.IAccessRule.AccessLevel;
import com.listerly.entities.IItem;
import com.listerly.entities.ISpace;
import com.listerly.entities.IUser;
import com.listerly.entities.impl.objectify.AccessRule;
import com.listerly.entities.impl.objectify.Item;
import com.listerly.entities.impl.objectify.Space;
import com.listerly.entities.impl.objectify.User;

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

	@Override
	public List<? extends IAccessRule> findAllRulesForSpace(ISpace space) {
		Ref<Space> spaceRef = Ref.create((Space)space);
		Query<AccessRule> filter = ofy().consistency(Consistency.STRONG).load().type(AccessRule.class).filter("space", spaceRef);
		List<AccessRule> list = filter.list();
		return list;
	}

	@Override
	public void createAccessRule(ISpace space, IUser user, AccessLevel level) {
		AccessRule rule = new AccessRule((Space)space, (User) user, level);
		ofy().save().entity(rule).now();
	} 
	
	
}
