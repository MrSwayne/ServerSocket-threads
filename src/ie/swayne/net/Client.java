package ie.swayne.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import static ie.swayne.net.Globals._IP;
public class Client extends Thread {
	private final int PORT;
	
	public Client(int PORT) {
		this.PORT = PORT;
	}
	
	public void run()  {
		try (
				Socket socket = new Socket(_IP, PORT);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		)
		{	
			while(true) {
				System.out.println(in.readUTF());
				String toSend = keyboard.readLine();
				out.writeUTF(toSend);
				
				if(toSend.equalsIgnoreCase("exit")) {
					System.out.println("Closing connection " + socket);
					socket.close();
					break;
				}
				
				String received = in.readUTF();
				System.out.println(received);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
