package com.backflipsource;

public interface Converter<M, P> {

	void convertModel(M source, P target);

	void convertPresentation(P source, M target);
}
