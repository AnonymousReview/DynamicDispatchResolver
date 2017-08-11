package com.eanonymous.open.java.commons.subsystems;

public class GarbageCollectionSubsystem extends JavaSubsystem {

	public static final String TAG = "GARBAGE_COLLECTION_SUBSYSTEM";

	@Override
	public String getName() {
		return "Garbage Collection";
	}

	@Override
	public String getDescription() {
		return "Java garbage collection libraries";
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
		return new String[] { "java.lang.ref" };
	}

}
