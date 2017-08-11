package com.eanonymous.open.java.commons.subsystems;

public class DataStructureSubsystem extends JavaSubsystem {

	public static final String TAG = "DATA_STRUCTURE_SUBSYSTEM";

	@Override
	public String getName() {
		return "Data Structure";
	}

	@Override
	public String getDescription() {
		return "Java common collection-like data structures";
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
		return new String[] { "java.beans", "java.beans.beancontext", "java.text", "java.text.spi" };
	}
}
