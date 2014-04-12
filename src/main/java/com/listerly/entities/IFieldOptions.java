package com.listerly.entities;

import java.util.List;

public interface IFieldOptions extends List<IFieldOption>{
	public IFieldOption createAtEnd();
	public IFieldOption createAtIndex(int index);
}
