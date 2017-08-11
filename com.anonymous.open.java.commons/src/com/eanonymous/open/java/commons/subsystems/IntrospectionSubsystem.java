package com.eanonymous.open.java.commons.subsystems;

public class IntrospectionSubsystem extends JavaSubsystem {

	public static final String TAG = "INTROSPECTION_SUBSYSTEM";

	@Override
	public String getName() {
		return "Introspection";
	}

	@Override
	public String getDescription() {
		return "Java reflection and runtime introspection libraries";
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
		return new String[] { "java.lang.annotation", "java.lang.instrument", "java.lang.invoke",
				"java.lang.management", "java.lang.reflect", "javax.annotation", "javax.annotation.processing",
				"javax.lang.model", "javax.lang.model.element", "javax.lang.model.type", "javax.lang.model.util",
				"javax.management", "javax.management.loading", "javax.management.modelmbean",
				"javax.management.monitor", "javax.management.openmbean", "javax.management.relation",
				"javax.management.remote", "javax.management.timer", "javax.script", "javax.tools" };
	}

}
