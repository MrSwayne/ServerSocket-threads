package ie.swayne.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LinkedList<Server> servers = Globals.GET_SERVERS();
				for(int i = 0;i < servers.size();i++)
					servers.get(i).close();
			}
		});
		
		new Frame();
	}
}
