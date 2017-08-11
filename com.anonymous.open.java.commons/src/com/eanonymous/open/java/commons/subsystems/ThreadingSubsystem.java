package com.eanonymous.open.java.commons.subsystems;

public class ThreadingSubsystem extends JavaSubsystem {

	public static final String TAG = "THREADING_SUBSYSTEM";

	@Override
	public String getName() {
		return "Threading";
	}

	@Override
	public String getDescription() {
		return "Java threading libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { JavaCoreSubsystem.TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] { "java.util.concurrent", "java.util.concurrent.atomic", "java.util.concurrent.locks" };
	}

}
