package com.listerly.entities;

import java.util.Date;
import java.util.List;


public interface ISpace extends BaseEntity {
	public String getName();
	public void setName(String name);

	public List<? extends IField> getFields();
	public IField createField();
	public IField createField(int index);

	public List<? extends ISpaceView> getViews();
	public ISpaceView createView();
	public ISpaceView createView(int index);
	
	public Date getCreated();
}
