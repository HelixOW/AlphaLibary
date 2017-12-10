package de.alphahelix.alphalibary.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class ServerPinger {

    private final String server;
    private final int port;

    public ServerPinger(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void ping(Consumer<ServerResult> callback) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

        exec.schedule(() -> {
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
                callback.accept(new ServerResult(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[0]));
            } catch (IOException e) {
                e.printStackTrace();
                callback.accept(null);
            }
        }, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerPinger that = (ServerPinger) o;
        return port == that.port &&
                Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, port);
    }

    @Override
    public String toString() {
        return "ServerPinger{" +
                "server='" + server + '\'' +
                ", port=" + port +
                '}';
    }
}
