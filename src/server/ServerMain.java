package server;

public class ServerMain extends Thread {

	private int ServerPort = 4690;

	/**
	 * Server.jar [Port] or ? for Help
	 * 
	 * @param args
	 */
	public void run() {

		ServerManager manager = new ServerManager();
		Thread serverMain = new Server(ServerPort, manager);
		serverMain.start();

		CommandsExcuter cE = new CommandsExcuter(manager);
		cE.waitForCommand();

		try {
			serverMain.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setServerPort(int serverPort) {
		this.ServerPort = serverPort;
	}
}
