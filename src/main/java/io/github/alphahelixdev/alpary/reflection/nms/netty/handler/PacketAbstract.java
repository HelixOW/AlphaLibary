package io.github.alphahelixdev.alpary.reflection.nms.netty.handler;

import com.google.common.base.Objects;
import io.github.alphahelixdev.alpary.reflection.nms.netty.channel.ChannelWrapper;
import io.github.alphahelixdev.helius.reflection.SaveField;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class PacketAbstract {

    private final Cancellable cancellable;
    private Player player;
    private ChannelWrapper channelWrapper;
    private Object packet;

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
            new SaveField(this.getPacket().getClass().getDeclaredField(field)).set(this.getPacket(), value,
                    true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
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
     * Modify a value of the packet (without throwing an exceptions)
     *
     * @param field Name of the field to modify
     * @param value Value to be assigned to the field
     */
    public void setPacketValueSilent(String field, Object value) {
        try {
            new SaveField(this.getPacket().getClass().getDeclaredField(field)).set(this.getPacket(), value,
                    false);
        } catch (NoSuchFieldException ignored) {

        }
    }

    /**
     * Modify a value of the packet
     *
     * @param index field-index in the packet class
     * @param value value to be assigned to the field
     */
    public void setPacketValue(int index, Object value) {
        new SaveField(this.getPacket().getClass().getDeclaredFields()[index]).set(this.getPacket(), value,
                true);
    }

    /**
     * Modify a value of the packet (without throwing an exceptions)
     *
     * @param index field-index in the packet class
     * @param value value to be assigned to the field
     */
    public void setPacketValueSilent(int index, Object value) {
        new SaveField(this.getPacket().getClass().getDeclaredFields()[index]).set(this.getPacket(), value,
                false);
    }

    /**
     * Get a value of the packet
     *
     * @param field Name of the field
     * @return current value of the field
     */
    public Object getPacketValue(String field) {
        try {
            return new SaveField(this.getPacket().getClass().getDeclaredField(field)).get(this.getPacket());
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
            return new SaveField(this.getPacket().getClass().getDeclaredField(field)).get(this.getPacket());
        } catch (Exception ignored) {
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
            return new SaveField(this.getPacket().getClass().getDeclaredFields()[index]).get(this.getPacket());
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
            return new SaveField(this.getPacket().getClass().getDeclaredFields()[index]).get(this.getPacket());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return <code>true</code> if the packet has been cancelled
     */
    public boolean isCancelled() {
        return this.getCancellable().isCancelled();
    }

    /**
     * @param b if set to <code>true</code> the packet will be cancelled
     */
    public void setCancelled(boolean b) {
        this.getCancellable().setCancelled(b);
    }

    /**
     * @return the class name of the sent or received packet
     */
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
    }

    /**
     * @return <code>true</code> if the packet has a player
     */
    public boolean hasPlayer() {
        return this.player != null;
    }

    /**
     * @return The name of the receiving or sending player
     * @see #hasPlayer()
     * @see #getPlayer()
     */
    public String getPlayername() {
        if (!this.hasPlayer())
            return null;

        return this.getPlayer().getName();
    }

    /**
     * @return <code>true</code> if the packet has a channel
     */
    public boolean hasChannel() {
        return this.getChannelWrapper() != null;
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
     * @return The receiving or sending player of the packet
     * @see #hasPlayer()
     * @see #getChannel()
     */
    public Player getPlayer() {
        return this.player;
    }

    public Cancellable getCancellable() {
        return this.cancellable;
    }

    public ChannelWrapper getChannelWrapper() {
        return this.channelWrapper;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getPlayer(), this.getChannelWrapper(), this.getPacket(), this.getCancellable());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketAbstract that = (PacketAbstract) o;
        return Objects.equal(getPlayer(), that.getPlayer()) &&
                Objects.equal(channelWrapper, that.channelWrapper) &&
                Objects.equal(getPacket(), that.getPacket()) &&
                Objects.equal(cancellable, that.cancellable);
    }

    @Override
    public String toString() {
        return "Packet{ " + (this.getClass().equals(SentPacket.class) ? "[> OUT >]" : "[< IN <]") + " " +
                this.getPacketName() + " " + (this.hasPlayer() ? this.getPlayername() : this.hasChannel() ?
                this.getChannel().channel() : "#server#") + " }";
    }
}
