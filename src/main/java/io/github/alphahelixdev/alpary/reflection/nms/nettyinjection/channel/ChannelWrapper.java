package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel;

import java.net.SocketAddress;

public class ChannelWrapper {

	private Object channel;

	public ChannelWrapper(Object channel) {
		this.channel = channel;
	}

	public Object channel() {
		return this.channel;
	}

	public SocketAddress getRemoteAddress() {
		return null;
	}

	public SocketAddress getLocalAdress() {
		return null;
	}
}
