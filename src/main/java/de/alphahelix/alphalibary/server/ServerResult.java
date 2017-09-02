package de.alphahelix.alphalibary.server;

import com.google.common.base.Objects;

import java.io.Serializable;

public class ServerResult implements Serializable {
    private int playercount, maximumPlayers;
    private String motd;

    public ServerResult(int playercount, int maximumPlayers, String motd) {
        this.playercount = playercount;
        this.maximumPlayers = maximumPlayers;
        this.motd = motd;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerResult that = (ServerResult) o;
        return getPlayercount() == that.getPlayercount() &&
                getMaximumPlayers() == that.getMaximumPlayers() &&
                Objects.equal(getMotd(), that.getMotd());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPlayercount(), getMaximumPlayers(), getMotd());
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
