package com.eanonymous.open.java.commons.subsystems;

import com.anonymous.open.commons.subsystems.Subsystem;

public abstract class JavaSubsystem extends Subsystem {

	public static String CATEGORY = "JAVA_SUBSYSTEM";
	
	@Override
	public String getCategory() {
		return CATEGORY;
	}
	
	@Override
	public String getCategoryDescription(){
		return "Java Subsystems";
	}
	
}
