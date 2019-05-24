package io.github.alphahelixdev.alpary.reflection.nms.nettyinjection.channel;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.net.SocketAddress;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ChannelWrapper {
	
	@NonNull
	private Object channel;
	
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
