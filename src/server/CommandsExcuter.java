package server;

import java.util.EnumSet;
import java.util.Scanner;

public class CommandsExcuter {
	ServerManager manager;
	boolean exit = false;
	Scanner reader;
	EnumSet<Commands> usable;

	private enum Commands {
		help("Shows All Commnads"), show("Show a List of Connections"), fail("fail passing");

		String info;

		private Commands(String info) {
			this.info = info;
		}

		static Commands stringTOCo(String s) {
			for (Commands c : Commands.values()) {
				if (c.name().equals(s))
					return c;
			}
			return fail;
		}
	}

	public CommandsExcuter(ServerManager manager) {
		this.manager = manager;
		this.reader = new Scanner(System.in);
		usable = EnumSet.of(Commands.help, Commands.show);
	}

	private void println(String s) {
		System.out.println(s);
	}

	@SuppressWarnings("unused")
	private void print(String s) {
		System.out.print(s);
	}

	public void waitForCommand() {

		while (exit == false) {
			String eingabe = reader.nextLine();
			this.executer(Commands.stringTOCo(eingabe));
		}

		reader.close();
	}

	private void executer(Commands command) {

		switch (command) {
		case help:
			for (Commands c : usable) {
				println(c.name() + " - " + c.info);
			}
			break;
		case show:
			println("Actic Connects: " + manager.getActivConnects());
			println("Finished Connects: " + manager.getFinishConnects());
			break;
		default:
			println("Use Command: " + Commands.help + " - " + Commands.help.info);
			break;
		}
	}
}
