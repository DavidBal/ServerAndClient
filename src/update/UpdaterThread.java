package update;

import config.ManagerClient;

public class UpdaterThread extends Thread {

	/**
	 * Update-Intervall 5 Minuten.
	 */
	private static final long UPDATE_INTERVALL = 300000;
	
	public boolean exit;

	ManagerClient manager;

	/**
	 * 
	 */
	@Override
	public void run() {
		update();
		this.exit = false;
	}

	/**
	 * 
	 * @param manager
	 */
	public UpdaterThread(ManagerClient manager) {
		this.manager = manager;
		this.setName("UpdaterThread");

	}

	/**
	 * 
	 */
	private synchronized void update() {
		while (true) {
			this.manager.server.update(this.manager);
			try {
				this.wait(UPDATE_INTERVALL);
			} catch (InterruptedException e) {	
			}
			if(this.exit){
				return;
			}
		}
	}

}
