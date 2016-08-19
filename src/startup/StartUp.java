package startup;

import server.ServerMain;

public class StartUp {

	public static Thread thread;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println(" -c  : Starting the Client");
			System.out.println(" -s  : Starting the Server");
		}

		if (args.length > 0) {
			if (args[0].equals("-s") || args[0].equals("-S")) {
				StartUp.server(args);
			}
			if (args[0].equals("-c") || args[0].equals("-C")) {
				StartUp.client(args);
			}
		}

		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void client(String[] arg) {

	}

	public static void server(String[] arg) {

		System.out.println("Starting Server:");

		ServerMain server = new ServerMain();

		if (arg.length == 2) {
			if (arg[1].equals("?")) {
				System.out.println("Change port by: [Port]");
				return;
			}
			server.setServerPort(Integer.valueOf(arg[1]));
			System.out.println("ServerPort Changed: " + arg[1]);
		}

		thread = server;
		thread.start();

	}

}
