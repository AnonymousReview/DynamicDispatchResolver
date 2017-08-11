package com.eanonymous.open.java.commons.subsystems;

public class DatabaseSubsystem extends JavaSubsystem {

	public static final String TAG = "DATABASE_SUBSYSTEM";

	@Override
	public String getName() {
		return "Database";
	}

	@Override
	public String getDescription() {
		return "Java database libraries";
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
		return new String[] { "java.sql", "javax.sql", "javax.sql.rowset", "javax.sql.rowset.serial",
				"javax.sql.rowset.spi" };
	}

}
