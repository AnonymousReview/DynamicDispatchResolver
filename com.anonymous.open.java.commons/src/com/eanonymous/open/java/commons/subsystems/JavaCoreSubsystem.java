package com.eanonymous.open.java.commons.subsystems;

import com.anonymous.open.commons.subsystems.Subsystem;

public class JavaCoreSubsystem extends JavaSubsystem {

	public static final String TAG = "JAVACORE_SUBSYSTEM";
	
	@Override
	public String getName() {
		return "Java Core";
	}

	@Override
	public String getDescription() {
		return "Java core language libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { Subsystem.ROOT_SUBSYSTEM_TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] { "java.lang", "java.util", "java.util.regex", "java.util.spi" };
	}

}
