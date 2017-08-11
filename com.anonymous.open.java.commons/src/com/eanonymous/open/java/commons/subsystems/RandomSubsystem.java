package com.eanonymous.open.java.commons.subsystems;

public class RandomSubsystem extends JavaSubsystem {

	public static final String TAG = "RANDOM_SUBSYSTEM";

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public String getDescription() {
		return "Java random libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { MathSubsystem.TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] {};
	}
	
	@Override
	public String[] getTypes() {
		return new String[] { "java.util.Random", "java.security.SecureRandom" };
	}
	
	@Override
	public String[] getMethods() {
		return new String[] { "java.lang.Math random" };
	}
}
