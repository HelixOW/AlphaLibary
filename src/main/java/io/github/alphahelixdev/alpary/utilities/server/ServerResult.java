package io.github.alphahelixdev.alpary.utilities.server;

import java.io.Serializable;
import java.util.Objects;


public class ServerResult implements Serializable {
	
	private final int playercount, maximumPlayers;
	private final String motd;
	
	public ServerResult(int playercount, int maximumPlayers, String motd) {
		this.playercount = playercount;
		this.maximumPlayers = maximumPlayers;
		this.motd = motd;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getPlayercount(), getMaximumPlayers(), getMotd());
	}
	
	public int getPlayercount() {
		return playercount;
	}
	
	public int getMaximumPlayers() {
		return maximumPlayers;
	}
	
	public String getMotd() {
		return motd;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ServerResult that = (ServerResult) o;
		return getPlayercount() == that.getPlayercount() &&
				getMaximumPlayers() == that.getMaximumPlayers() &&
				Objects.equals(getMotd(), that.getMotd());
	}
	
	@Override
	public String toString() {
		return "ServerResult{" +
				"playercount=" + playercount +
				", maximumPlayers=" + maximumPlayers +
				", motd='" + motd + '\'' +
				'}';
	}
}
