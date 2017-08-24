package de.alphahelix.alphalibary.netty.handler;

import de.alphahelix.alphalibary.netty.channel.ChannelWrapper;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class PacketAbstract {

    private Player player;
    private ChannelWrapper channelWrapper;

    private Object packet;
    private Cancellable cancellable;

    public PacketAbstract(Object packet, Cancellable cancellable, Player player) {
        this.player = player;

        this.packet = packet;
        this.cancellable = cancellable;
    }

    public PacketAbstract(Object packet, Cancellable cancellable, ChannelWrapper channelWrapper) {
        this.channelWrapper = channelWrapper;

        this.packet = packet;
        this.cancellable = cancellable;
    }

    /**
     * Modify a value of the packet
     *
     * @param field Name of the field to modify
     * @param value Value to be assigned to the field
     */
    public void setPacketValue(String field, Object value) {
        try {
            new ReflectionUtil.SaveField(packet.getClass().getDeclaredField(field)).set(getPacket(), value, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify a value of the packet (without throwing an exceptions)
     *
     * @param field Name of the field to modify
     * @param value Value to be assigned to the field
     */
    public void setPacketValueSilent(String field, Object value) {
        try {
            new ReflectionUtil.SaveField(packet.getClass().getDeclaredField(field)).set(getPacket(), value, false);
        } catch (NoSuchFieldException e) {

        }
    }

    /**
     * Modify a value of the packet
     *
     * @param index field-index in the packet class
     * @param value value to be assigned to the field
     */
    public void setPacketValue(int index, Object value) {
        try {
            new ReflectionUtil.SaveField(packet.getClass().getDeclaredFields()[index]).set(getPacket(), value, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modify a value of the packet (without throwing an exceptions)
     *
     * @param index field-index in the packet class
     * @param value value to be assigned to the field
     */
    public void setPacketValueSilent(int index, Object value) {
        try {
            new ReflectionUtil.SaveField(packet.getClass().getDeclaredFields()[index]).set(getPacket(), value, false);
        } catch (Exception e) {
        }
    }

    /**
     * Get a value of the packet
     *
     * @param field Name of the field
     * @return current value of the field
     */
    public Object getPacketValue(String field) {
        try {
            return new ReflectionUtil.SaveField(packet.getClass().getDeclaredField(field)).get(getPacket());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a value of the packet (without throwing an exceptions)
     *
     * @param field Name of the field
     * @return current value of the field
     */
    public Object getPacketValueSilent(String field) {
        try {
            return new ReflectionUtil.SaveField(packet.getClass().getDeclaredField(field)).get(getPacket());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Get a value of the packet
     *
     * @param index field-index in the packet class
     * @return value of the field
     */
    public Object getPacketValue(int index) {
        try {
            return new ReflectionUtil.SaveField(packet.getClass().getDeclaredFields()[index]).get(getPacket());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a value of the packet (without throwing an exceptions)
     *
     * @param index field-index in the packet class
     * @return value of the field
     */
    public Object getPacketValueSilent(int index) {
        try {
            return new ReflectionUtil.SaveField(packet.getClass().getDeclaredFields()[index]).get(getPacket());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return <code>true</code> if the packet has been cancelled
     */
    public boolean isCancelled() {
        return this.cancellable.isCancelled();
    }

    /**
     * @param b if set to <code>true</code> the packet will be cancelled
     */
    public void setCancelled(boolean b) {
        this.cancellable.setCancelled(b);
    }

    /**
     * @return The receiving or sending player of the packet
     * @see #hasPlayer()
     * @see #getChannel()
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return <code>true</code> if the packet has a player
     */
    public boolean hasPlayer() {
        return this.player != null;
    }

    /**
     * @return The receiving or sending channel (wrapped in a {@link ChannelWrapper})
     * @see #hasChannel()
     * @see #getPlayer()
     */
    public ChannelWrapper<?> getChannel() {
        return this.channelWrapper;
    }

    /**
     * @return <code>true</code> if the packet has a channel
     */
    public boolean hasChannel() {
        return this.channelWrapper != null;
    }

    /**
     * @return The name of the receiving or sending player
     * @see #hasPlayer()
     * @see #getPlayer()
     */
    public String getPlayername() {
        if (!this.hasPlayer()) {
            return null;
        }
        return this.player.getName();
    }

    /**
     * @return the sent or received packet as an Object
     */
    public Object getPacket() {
        return this.packet;
    }

    /**
     * Change the packet that is sent
     *
     * @param packet new packet
     */
    public void setPacket(Object packet) {
        this.packet = packet;
    }

    /**
     * @return the class name of the sent or received packet
     */
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Packet{ " + (this.getClass().equals(SentPacket.class) ? "[> OUT >]" : "[< IN <]") + " " + this.getPacketName() + " " + (this.hasPlayer() ? this.getPlayername() : this.hasChannel() ? this.getChannel().channel() : "#server#") + " }";
    }

}
