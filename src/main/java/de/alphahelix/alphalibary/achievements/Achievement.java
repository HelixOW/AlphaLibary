package de.alphahelix.alphalibary.achievements;

import de.alphahelix.alphalibary.item.InventoryItem;

import java.io.Serializable;
import java.util.List;

public interface Achievement extends Serializable {

    String getName();

    InventoryItem getIcon();

    List<String> getDescription();
}
