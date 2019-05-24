package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.*;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ColorData implements ItemData {
	
	private int red = 0;
	private int blue = 0;
	private int green = 0;
	@NonNull
	private DyeColor dyeColor;
	
	/**
	 * Creates a new {@link ColorData} with RGB
	 *
	 * @param red   the amount of red
	 * @param blue  the amount of blue
	 * @param green the amount of green
	 */
	public ColorData(int red, int blue, int green) {
		this.setRed(red);
		this.setBlue(blue);
		this.setGreen(green);
		this.setDyeColor(DyeColor.getByColor(Color.fromRGB(red, green, blue)));
	}
	
	@Deprecated
	@Override
	public void applyOn(ItemStack applyOn) throws WrongDataException {
		
		ItemMeta meta = applyOn.getItemMeta();
		
		if (meta instanceof LeatherArmorMeta) {
			try {
				LeatherArmorMeta armor = (LeatherArmorMeta) meta;
				
				armor.setColor(Color.fromRGB(this.getRed(), this.getGreen(), this.getBlue()));
				
				applyOn.setItemMeta(armor);
			} catch (IllegalArgumentException e) {
				throw new WrongDataException(this);
			}
			
		} else {
			throw new WrongDataException(this);
		}
	}
}