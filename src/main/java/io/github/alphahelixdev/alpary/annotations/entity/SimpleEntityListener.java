package io.github.alphahelixdev.alpary.annotations.entity;

import io.github.alphahelixdev.alpary.Alpary;

public class SimpleEntityListener {
	
	public SimpleEntityListener() {
		Alpary.getInstance().annotationHandler().setEntityFields(this);
	}
}
