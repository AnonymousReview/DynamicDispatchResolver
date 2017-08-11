package com.eanonymous.open.java.commons.subsystems;

public class NetworkSubsystem extends JavaSubsystem {

	public static final String TAG = "NETWORK_SUBSYSTEM";

	@Override
	public String getName() {
		return "Network";
	}

	@Override
	public String getDescription() {
		return "Java network IO libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { IOSubsystem.TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] { "java.net", "java.rmi", "java.rmi.activation", "java.rmi.dgc", "java.rmi.registry",
				"java.rmi.server", "javax.net", "javax.net.ssl" };
	}

}