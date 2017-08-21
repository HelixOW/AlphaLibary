package de.alphahelix.alphalibary.server;

public class ServerResult {
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
    public String toString() {
        return "ServerResult{" +
                "playercount=" + playercount +
                ", maximumPlayers=" + maximumPlayers +
                ", motd='" + motd + '\'' +
                '}';
    }
}
