package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection;

import org.bukkit.event.Cancellable;

public interface INettyInjector {

	Object onPacketSend(Object receiver, Object packet, Cancellable cancellable);

	Object onPacketReceive(Object sender, Object packet, Cancellable cancellable);

}
