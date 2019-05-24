package io.github.alphahelixdev.alpary.utilities.item.data;

public class WrongDataException extends Exception {
	WrongDataException(ItemData data) {
        super("Exception while trying to apply the DataType: " + data.getClass().getName());
    }
}