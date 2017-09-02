package de.alphahelix.alphalibary.server;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.utils.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerPinger {

    private String server;
    private int port;

    public ServerPinger(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void ping(ServerCallback callback) {
        Util.runLater(1, true, () -> {
            try {
                Socket s = new Socket(server, port);

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                out.write(0xFE);

                int b;
                StringBuilder str = new StringBuilder();

                while ((b = in.read()) != -1)
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24)
                        str.append((char) b);

                String[] data = str.toString().split("§");
                callback.done(new ServerResult(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[0]));
            } catch (IOException e) {
                e.printStackTrace();
                callback.done(null);
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerPinger that = (ServerPinger) o;
        return port == that.port &&
                Objects.equal(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(server, port);
    }

    @Override
    public String toString() {
        return "ServerPinger{" +
                "server='" + server + '\'' +
                ", port=" + port +
                '}';
    }
}
