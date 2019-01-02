package io.github.alphahelixdev.alpary.annotations.item;

import io.github.alphahelixdev.alpary.Alpary;

public class SimpleItemListener {
    public SimpleItemListener() {
	    Alpary.getInstance().annotationHandler().setItemFields(this);
    }
}
