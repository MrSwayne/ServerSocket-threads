package ie.swayne.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static ie.swayne.net.Globals._PORT;

public class Server extends Thread {
	private final int MAX_CONNECTIONS = 2;
	private final int PORT;
	private ServerSocket server;
	private volatile ArrayList<Connection> connectedClients;
	private ListenThread listen;
	
	public Server() {
		
		this.PORT = _PORT++;
		connectedClients = new ArrayList<>();
	}
	
	public int getPort() {
		return this.PORT;
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(this.PORT);
			Globals.ADD_SERVER(this);
			listen = new ListenThread(server);
			listen.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	
	private boolean checkAvailable(int port) {
		try ( Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException e) {
			return true;
		}
	}
	
	class Connection extends Thread {
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		
		DateFormat fd = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat ft = new SimpleDateFormat("hh:mm:ss");
		
		public Connection(Socket socket, DataInputStream in, DataOutputStream out) {
			this.socket = socket;
			this.in = in;
			this.out = out; 
		}
		
		public Socket getSocket() {
			return this.socket;
		}
		
		@Override
		public void run() {
			String received;
			String toSend;
			while(true) {
				try {
					out.writeUTF("What do you want? [Date | Time | Exit]");
					
					received = in.readUTF();
					
					if(received.equalsIgnoreCase("Exit")) {
						System.out.println("Client " + this.socket + " wants to exit.");
						this.socket.close();
						System.out.println("Connection closed.");
						connectedClients.remove(this);
						break;
					}
					
					Date date = new Date();
					
					switch(received.toLowerCase()) {
					case "date": 
						toSend = fd.format(date);
						out.writeUTF(toSend);
						break;
					case "time":
						toSend = ft.format(date);
						out.writeUTF(toSend);
						break;
					default:
						out.writeUTF("Invalid input");
						break;							
					}			
				
				} catch (IOException e) {
					try {
						this.socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			
			try {
				this.in.close();
				this.out.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	class ListenThread extends Thread {
		private ServerSocket server;
		public ListenThread(ServerSocket server) {
			this.server = server;
		}
		
		@Override
		public void run() {
			while(true) {
			while(connectedClients.size() < MAX_CONNECTIONS) {
				Socket client;
				try {
					client = server.accept();
					System.out.println("Accepted Connection from " + client.toString());
					DataInputStream in = new DataInputStream(client.getInputStream());
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					
					Connection conn = new Connection(client, in, out);
					connectedClients.add(conn);
					conn.start();
				} catch (IOException e) {
					try {
						server.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			System.out.println("Max connections exceeded. Exiting listener.");
			}
		}
	}

	public void close() {
		try {
			server.close();
			System.out.println("Server closed.");
		} catch(IOException e) {
			System.out.println("WTF");
		}
		
	}
}
