package com.eanonymous.open.java.commons.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.anonymous.open.commons.utilities.DisplayUtils;
import com.eanonymous.open.java.commons.subsystems.JavaCoreSubsystem;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.ui.viewer.graph.XCSGHierarchyGraphUtil;

/**
 * A menu handler for the Java subsystem tag hierarchy
 * 
 * @author Ben Holland
 */
public class ShowJavaSubsystemTagHierarchyHandler extends AbstractHandler {
	public ShowJavaSubsystemTagHierarchyHandler() {}

	/**
	 * Opens a prompt to enter a graph element address to show
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// check to see if the subsystems have been registered yet
		boolean subsystemTagHierarchyExists = false;
		for(String tag : XCSG.HIERARCHY.registeredTags()){
			if(tag.equals(JavaCoreSubsystem.TAG)){
				subsystemTagHierarchyExists = true;
			}
		}
		
		// show the subsystem hierarchy
		if(subsystemTagHierarchyExists){
			Q hierarchy = XCSGHierarchyGraphUtil.getXCSGHiearchyQ(JavaCoreSubsystem.TAG);
			DisplayUtils.show(hierarchy, "Java Subsystem Hierarchy");
		} else {
			DisplayUtils.showError("Java subsystems have not been registered yet!");
		}

		// returns the result of the execution (reserved for future use, must be null)
		return null;
	}
	
}