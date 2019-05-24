package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumEquipSlot;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.*;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityEquipmentPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(
                    Utils.nms().getNMSClass("PacketPlayOutEntityEquipment"), int.class,
                    Utils.nms().getNMSClass("EnumItemSlot"), Utils.nms().getNMSClass("ItemStack"));

    private int entityID;
    private ItemStack itemStack;
    private REnumEquipSlot equipSlot;

    private static SaveConstructor getPacket() {
        return EntityEquipmentPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityEquipmentPacket.getPacket().newInstance(stackTrace, this.getEntityID(),
                Utils.nms().getNMSItemStack(this.getItemStack()), this.getEquipSlot().getNmsSlot());
    }
}
