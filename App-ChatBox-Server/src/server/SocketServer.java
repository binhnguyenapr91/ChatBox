package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;

public class SocketServer implements Runnable {
	
	public ServerThread clients[];
	public ServerSocket server = null;
	public Thread thread = null;
	public int clientCount = 0;
	public int port = 8080;
	public ServerGui ui;
	public Database db;

	public SocketServer(ServerGui serverFrame) {
		clients = new ServerThread[10];
		ui = serverFrame;
		db = new Database(ui.txtDatabasePath.getText());
		try {
			server = new ServerSocket(port);
			port = server.getLocalPort();
			ui.txtServerLog.append(Config.SERVER_STARTED_IP + InetAddress.getLocalHost() + Config.PORT + server.getLocalPort());
			start();
		} catch (Exception e) {
			ui.txtServerLog.append(Config.CAN_NOT_USE_THIS_PORT + port);
			ui.retryStart(0);
		}
	}

	public SocketServer(ServerGui serverFrame, int inputPort) {
		clients = new ServerThread[10];
		ui = serverFrame;
		port = inputPort;
		db = new Database(ui.txtDatabasePath.getText());
		
		try {
			server = new ServerSocket(port);
			port = server.getLocalPort();
			ui.txtServerLog.append(Config.SERVER_STARTED_IP + InetAddress.getLocalHost() + Config.PORT + server.getLocalPort());
			start();
		} catch (Exception e) {
			ui.txtServerLog.append(Config.CAN_NOT_USE_THIS_PORT + port);
			ui.retryStart(0);
		}
	}

	public void run() {
		while (thread != null) {
			ui.txtServerLog.append(Config.WAITING_FOR_CLIENT);
			try {
				addThread(server.accept());
			} catch (IOException e) {
				ui.txtServerLog.append(Config.SERVER_CAN_NOT_ACCEPT);
				ui.retryStart(0);
			}
		}
	}

	public void handleServer(int ID, Message msg) {
		if (msg.getContent().equals(Config.MESSAGE_OUT)) {
			broadcast(Config.TOKEN_STRING_LOGOUT, Config.RECIPIENT_SERVER, msg.getSender());
			remove(ID);
		} else {
			if (msg.getType().equals(Config.TOKEN_STRING_TEST)) {
				System.out.println(Config.STRING_IN + msg.toString());
				clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_TEST, Config.RECIPIENT_SERVER, Config.STRING_OK, msg.getSender()));
			}else if (msg.getType().equals(Config.TOKEN_STRING_LOGIN)) {
				if (findClientThread(msg.getSender()) == null) {
					if (db.checkLogin(msg.getSender(), msg.getContent())) {
						clients[findClient(ID)].username = msg.getSender();
						clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_LOGIN, Config.RECIPIENT_SERVER, Config.STRING_TRUE, msg.getSender()));
						broadcast(Config.TOKEN_STRING_NEWUSER, Config.RECIPIENT_SERVER, msg.getSender());
						sendClientList(msg.getSender());
					} else {
						clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_LOGIN, Config.RECIPIENT_SERVER, Config.STRING_FALSE, msg.getSender()));
					}
				} else {
					clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_LOGIN, Config.RECIPIENT_SERVER, Config.STRING_FALSE, msg.getSender()));
				}
			} else if (msg.getType().equals(Config.TOKEN_STRING_MESSAGE)) {
				if (msg.getRecipient().equals(Config.ALL_CLIENTS)) {
					broadcast(Config.TOKEN_STRING_MESSAGE, msg.getSender(), msg.getContent());
				} else {
					findClientThread(msg.getRecipient()).send(
							new Message(msg.getType(), msg.getSender(), msg.getContent(), msg.getRecipient()));
					clients[findClient(ID)].send(
							new Message(msg.getType(), msg.getSender(), msg.getContent(), msg.getRecipient()));
				}
			} else if (msg.getType().equals(Config.TOKEN_STRING_SIGNUP)) {
				if (findClientThread(msg.getSender()) == null) {
					if (!db.userExists(msg.getSender())) {
						db.addUser(msg.getSender(), msg.getContent());
						clients[findClient(ID)].username = msg.getSender();
						clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_SIGNUP, Config.RECIPIENT_SERVER, Config.STRING_TRUE, msg.getSender()));
						clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_LOGIN, Config.RECIPIENT_SERVER, Config.STRING_TRUE, msg.getSender()));
						broadcast(Config.TOKEN_STRING_NEWUSER, Config.RECIPIENT_SERVER, msg.getSender());
						sendClientList(msg.getSender());
					} else {
						clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_SIGNUP, Config.RECIPIENT_SERVER, Config.STRING_FALSE, msg.getSender()));
					}
				} else {
					clients[findClient(ID)].send(new Message(Config.TOKEN_STRING_SIGNUP, Config.RECIPIENT_SERVER, Config.STRING_FALSE, msg.getSender()));
				}
			}
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			ui.txtServerLog.append(Config.CLIENT_ACCEPTED + socket);
			clients[clientCount] = new ServerThread(this, socket);
			try {
				clients[clientCount].open();
			}catch(Exception e) {
				ui.txtServerLog.append(Config.CAN_NOT_OPEN_THREAD);
			}
				clients[clientCount].start();
			clientCount++;
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	public void broadcast(String type, String sender, String content) {
		Message msg = new Message(type, sender, content, Config.ALL_CLIENTS);
		for (int i = 0; i < clientCount; i++) {
			clients[i].send(msg);
		}
	}

	@SuppressWarnings("deprecation")
	public synchronized void remove(int ID) {
		int position = findClient(ID);
		if (position >= 0) {
			ServerThread needToRemoveThread = clients[position];
			ui.txtServerLog.append(Config.REMOVING_CLIENT_THREAD + ID + Config.AT + position);
			if (position < clientCount - 1) {
				for (int i = position + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
			clientCount--;
			try {
				needToRemoveThread.close();	
			}catch(Exception e) {
				ui.txtServerLog.append(Config.CAN_NOT_CLOSE_THREAD+ e);
			}			
			needToRemoveThread.stop();
		}

	}

	public int findClient(int ID) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}

	public ServerThread findClientThread(String client) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].username.equals(client)) {
				return clients[i];
			}
		}
		return null;
	}
	
	public void sendClientList(String client) {
		for (int i = 0; i < clientCount; i++) {
			findClientThread(client).send(new Message(Config.TOKEN_STRING_NEWUSER, Config.RECIPIENT_SERVER, clients[i].username, client));
		}
	}

}
