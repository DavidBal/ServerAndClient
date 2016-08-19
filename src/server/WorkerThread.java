package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ControllCalls.ControllCalls;

/**
 * Jede Anfrage eines Clients �ffnet eine neue Thread ...
 * 
 * @author David
 *
 */
public class WorkerThread extends Thread {

	Socket client;
	BufferedReader in;
	PrintWriter out;
	int id;

	ServerManager manager;

	@Override
	public void run() {

		try {

			if (ServerManager.debug)
				System.out.println("Worker- " + this.id + ": I‘m UP!!");

			String controllCall = this.in.readLine(); // Lese Befehl

			if (ServerManager.debug)
				System.out.println(controllCall);

			this.controll(controllCall);

			client.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		manager.onFinish();

		if (ServerManager.debug)
			System.out.println("Worker - " + this.id + " END!!");
	}

	WorkerThread(Socket client, int id, ServerManager manager) {
		this.client = client;
		this.id = id;
		this.manager = manager;

		this.setName("WorkerThread" + this.id);

		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void controll(String controllCall) {

		/**
		 * 
		 */
		/**
		 * TODO ------ * CC -> One Line Every Msg -> one Line END -> One Line
		 */

		switch (ControllCalls.stringToControllCall(controllCall)) {
		case Ping:
			this.out.println("Pong");
			break;
		case NEW:
			try {
				String msg = this.in.readLine();
				if (ServerManager.debug)
					System.out.println(msg);
				this.manager.messages.add(msg);
				this.in.readLine();
				// TODO END ??
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
			}

			break;
		case LOGIN:
			try {
				// User Name
				String uN = this.in.readLine();
				// PW
				String pW = this.in.readLine();

				//
				if (this.manager.database.getUserBerechtigung(uN, pW) != 0) {
					this.out.println("Yes");
				} else {
					this.out.println("NO");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UPDATE:

			for (String s : this.manager.messages) {
				this.out.println(s);
			}

			this.out.println(ControllCalls.END);

			break;

		default:
			break;

		}
	}

}
