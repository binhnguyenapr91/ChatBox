package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import message.Message;

public class SocketClient implements Runnable {

	public int port;
	public String serverAddress;
	public Socket socket;
	public ClientGui ui;
	public ObjectInputStream dataIn;
	public ObjectOutputStream dataOut;

	public SocketClient(ClientGui clientGui) {
		ui = clientGui;
		this.serverAddress = ui.serverAddr;
		this.port = ui.port;
		try {
			socket = new Socket(InetAddress.getByName(serverAddress), port);
			dataOut = new ObjectOutputStream(socket.getOutputStream());
			dataOut.flush();
			dataIn = new ObjectInputStream(socket.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean isRunning = true;
		while (isRunning) {
			try {
				Message msg = (Message) dataIn.readObject();
				System.out.println(Config.STRING_IN + msg.toString());
				if (msg.getType().equals(Config.TOKEN_STRING_TEST)) {
					ui.textArea.append("[" + msg.getSender() + " > Me] : Test connection ok.\n");
				} else if (msg.getType().equals(Config.TOKEN_STRING_LOGIN)) {
					if (msg.getContent().equals("TRUE")) {
						ui.textArea.append(Config.STRING_LOGIN_OK);
					} else {
						ui.textArea.append(Config.STRING_LOGIN_FAIL);
					}
				} else if (msg.getType().equals(Config.TOKEN_STRING_MESSAGE)) {
					if (msg.getRecipient().equals(ui.username)) {
						ui.textArea.append("[" + msg.getSender() + " > Me] : " + msg.getContent() + "\n");
					} else {
						ui.textArea.append("[" + msg.getSender() + " > " + msg.getRecipient() + "] : " + msg.getContent() + "\n");
					}
				} else if (msg.getType().equals(Config.TOKEN_STRING_NEWUSER)) {
					if (!msg.getContent().equals(ui.username)) {
						boolean exists = false;
						for (int i = 0; i < ui.model.getSize(); i++) {
							if (ui.model.getElementAt(i).equals(msg.getContent())) {
								exists = true;
								break;
							}
						}
						if (!exists) {
							ui.model.addElement(msg.getContent());
						}
					}
				} else if (msg.getType().equals(Config.TOKEN_STRING_SIGNUP)) {
					if (msg.getContent().equals("TRUE")) {
						ui.textArea.append(Config.STRING_SIGNUP_OK);
					} else {
						ui.textArea.append(Config.STRING_SIGNUP_FAIL);
					}
				} else if (msg.getType().equals(Config.TOKEN_STRING_LOGOUT)) {
					if (msg.getContent().equals(ui.username)) {
						ui.textArea.append("[" + msg.getSender() + " > Me] : Bye\n");
						for (int i = 1; i < ui.model.size(); i++) {
							ui.model.removeElementAt(i);
						}

						ui.clientThread.stop();
					} else {
						ui.model.removeElement(msg.getContent());
						ui.textArea
								.append("[" + msg.getSender() + " > All] : " + msg.getContent() + " has signed out\n");
					}
				}
			} catch (Exception e) {
				isRunning = false;
				ui.textArea.append(Config.STRING_CONNECTION_FAIL);
			}
		}
	}

	public void send(Message msg) {
		try {
			dataOut.writeObject(msg);
			dataOut.flush();
			System.out.println(Config.STRING_OUT + msg.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeClientThread(Thread clientThread) {
		clientThread = null;
	}
}
