package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;

public class ServerThread extends Thread {
	public SocketServer server = null;
	public Socket socket = null;
	public int ID = -1;
	public String username = "";
	public ObjectInputStream streamIn = null;
	public ObjectOutputStream streamOut = null;
	public ServerGui ui;

	public ServerThread(SocketServer socketServer, Socket socketClient) {
		super();
		server = socketServer;
		socket = socketClient;
		ID = socket.getPort();
		ui = socketServer.ui;
	}

	

	@SuppressWarnings("deprecation")
	public void run() {
		ui.txtServerLog.append("\nServer Thread " + ID + " is running.");
		while (true) {
			try {
				Message msg = (Message) streamIn.readObject();
				server.handleServer(ID, msg);
			} catch (Exception e) {
				System.out.println(ID + " Error reading: " + e.getMessage());
				server.remove(ID);
				stop();
			}
		}
	}

	public int getID() {
		return ID;
	}
	
	public void send(Message msg){
        try {
            streamOut.writeObject(msg);
            streamOut.flush();
        } 
        catch (IOException ex) {
            System.out.println("Can not send");
        }
    }
	public void open() {
		try {
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			streamIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (streamIn != null) {
				streamIn.close();
			}
			if (streamOut != null) {
				streamOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
