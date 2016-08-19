/**
 *  
 * 
 */

package client;

import clientTUISimpel.Konsole;
import config.ManagerClient;

public class ClientMain extends Thread {

	private int ServerPort = 4690;
	private String ServerAdress = "localhost";

	/**
	 *
	 * @param args
	 */
	public void run() {
		try {

			ManagerClient manager = new ManagerClient();

			ServerConector test = new ServerConector(this.ServerAdress, this.ServerPort);

			manager.server = test;
			// TODO Thread der das Auto-Uppdate der Daten �bernimmt!

			// ------ Testfaelle - Start------
			manager.server.ping();

			manager.server.sendNewMessage("Hallo Welt!!");

			manager.server.auth("test", "test"); // Alles richtig
			manager.server.auth("Nein", "test"); // User falsch
			manager.server.auth("test", "Nein"); // PW falsch
			manager.server.auth("nein", "nein"); // Alles falsch
			// ------ Testfaelle - Ende ------

			manager.startUpdater();
			// Ruft das Test Menue auf !!Konsole!!
			Konsole c = new Konsole(manager);
			c.hauptMenue();

			manager.exitUpdater();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
