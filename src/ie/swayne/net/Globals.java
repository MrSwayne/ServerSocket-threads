package ie.swayne.net;

import java.net.InetAddress;
import java.util.LinkedList;

public final class Globals {
	public static int _PORT = 9502;
	public static String _IP = "localhost";
	
	private static LinkedList<Server> _ACTIVE_SERVERS = new LinkedList<>();
	
	public static synchronized void ADD_SERVER(Server server) {
		_ACTIVE_SERVERS.add(server);
	}
	
	public static synchronized LinkedList<Server> GET_SERVERS() {
		return _ACTIVE_SERVERS;
	}
}
