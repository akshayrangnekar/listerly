package com.listerly.apiobj.user;

import java.util.ArrayList;
import java.util.List;

import com.listerly.entities.ISpace;
import com.listerly.entities.ISpaceView;

public class ASpaceMetadata extends AbstractApiObject<ISpace> {
	//private static Logger log = Logger.getLogger(ASpaceMetadata.class.getName());	

	private Long id;
	private String name;
	private List<AViewMetadata> views;
	
	public ASpaceMetadata(ISpace space) {
		super(space);
		
		this.views = new ArrayList<>();
		for (ISpaceView view : space.getViews()) {
			this.views.add(new AViewMetadata(view));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<AViewMetadata> getViews() {
		return views;
	}

	public void setViews(List<AViewMetadata> views) {
		this.views = views;
	}

	public static class AViewMetadata extends AbstractApiObject<ISpaceView>{
		private String name;
		private String uuid;
		
		public AViewMetadata(ISpaceView spaceView){
			super(spaceView);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}
}
