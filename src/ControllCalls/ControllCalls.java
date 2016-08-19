package ControllCalls;

public enum ControllCalls {

	END("End"), Ping("Ping"), Fail("Fail"), LOGIN("Login"), NEW("New"), UPDATE("Update");

	private ControllCalls(String call) {
		this.call = call;
	}

	private String call;

	/**
	 * Macht aus einem String ein ControllCall
	 * 
	 * @param s
	 * @return
	 */
	public static ControllCalls stringToControllCall(String s) {
		for (ControllCalls cc : ControllCalls.values()) {
			if (cc.call.equals(s)) {
				return cc;
			}
		}

		// TODO Exception

		return Fail;
	}

	@Override
	public String toString() {
		return this.call;
	}

}
