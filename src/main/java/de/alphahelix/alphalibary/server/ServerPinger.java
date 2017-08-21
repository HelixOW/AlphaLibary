package de.alphahelix.alphalibary.server;

import de.alphahelix.alphalibary.utils.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

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
                System.out.println(Arrays.toString(data));
                callback.done(new ServerResult(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[0]));
            } catch (IOException e) {
                e.printStackTrace();
                callback.done(null);
            }
        });
    }
}
