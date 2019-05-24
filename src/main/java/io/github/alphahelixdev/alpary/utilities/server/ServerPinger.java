package io.github.alphahelixdev.alpary.utilities.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ServerPinger {
	
	private final String server;
	private final int port;
	
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
				
				while((b = in.read()) != -1)
					if(b != 0 && b > 16 && b != 255 && b != 23 && b != 24)
						str.append((char) b);
				
				String[] data = str.toString().split("§");
				callback.accept(new ServerResult(Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[0]));
			} catch(IOException e) {
				e.printStackTrace();
				callback.accept(null);
			}
		}, 1, TimeUnit.MILLISECONDS);
	}
}
