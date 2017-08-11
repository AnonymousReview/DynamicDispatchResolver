package com.eanonymous.open.java.commons.subsystems;

public class MathSubsystem extends JavaSubsystem {

	public static final String TAG = "MATH_SUBSYSTEM";

	@Override
	public String getName() {
		return "Math";
	}

	@Override
	public String getDescription() {
		return "Java math libraries";
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
		return new String[] { "java.math" };
	}
	
	@Override
	public String[] getTypes() {
		return new String[] { "java.lang.Math" };
	}
}
