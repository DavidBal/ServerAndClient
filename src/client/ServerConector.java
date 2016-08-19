package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import config.ManagerClient;

public class ServerConector {

	private String name = "";
	private int serverPort;
	private InetAddress serverIP;

	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort) throws UnknownHostException {

		this.serverIP = InetAddress.getByName(serverIP);
		this.serverPort = serverPort;

	}

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @param name
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort, String name) throws UnknownHostException {
		this.serverIP = InetAddress.getByName(serverIP);
		this.serverPort = serverPort;
		this.name = name;
	}

	/**
	 * Connecting to the Server If everything goes right connected = TRUE
	 * 
	 * 
	 */
	public void connect() {
		try {
			this.socket = new Socket(this.serverIP, serverPort);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void disconnect() {
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send a "Ping" to the Server and if he work correct the Server will return
	 * a "Pong"!
	 * 
	 * @return TRUE = Server Okay ; FALSE = Server fail
	 * 
	 */
	public boolean ping() {
		this.connect();
		this.out.println("Ping");

		String tmp = "";
		try {
			tmp = this.in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ping  :  " + tmp);

		this.disconnect();

		if (tmp.equals("Pong")) {
			return true;
		}
		return false;
	}

	/**
	 * Sendet eine Nachricht an den Server.
	 * @param msg
	 */
	public void sendNewMessage(String msg) {
		this.connect();

		this.out.println(ControllCalls.ControllCalls.NEW);
		//
		this.out.println(msg);
		//
		this.out.println(ControllCalls.ControllCalls.END);

		this.disconnect();
	}

	/**
	 * Sendet eine Update auffoderung an den Server.
	 * @param manager
	 */
	public void update(ManagerClient manager) {
		this.connect();

		manager.msg.clear(); //TODO Besser
		
		this.out.println(ControllCalls.ControllCalls.UPDATE);

		String msg = "";
		
		try {
			msg = in.readLine();

			while (! msg.equals(ControllCalls.ControllCalls.END.toString())) {
				manager.msg.add(msg);
				msg = in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.disconnect();
	}

	// TODO Make this Function usefull
	/**
	 * 
	 */
	public void auth(String userName, String pw) {
		this.connect();

		this.out.println(ControllCalls.ControllCalls.LOGIN);
		this.out.println(userName);
		this.out.println(pw);
		this.out.println(ControllCalls.ControllCalls.END);

		try {
			System.out.println(this.in.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.disconnect();
	}

	@Override
	public String toString() {
		String tmp = this.name + ":" + this.serverIP.getHostName() + ":" + this.serverPort;
		return tmp;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ServerConector) {
			ServerConector other = (ServerConector) o;
			if (this.name.equals(other.name)) {
				if (this.serverIP.equals(other.serverIP)) {
					if (this.serverPort == other.serverPort) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
