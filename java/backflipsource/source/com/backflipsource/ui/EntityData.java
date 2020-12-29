package com.backflipsource.ui;

import java.util.List;

public interface EntityData<T> {

	List<T> list();

	void save(T t);
}
