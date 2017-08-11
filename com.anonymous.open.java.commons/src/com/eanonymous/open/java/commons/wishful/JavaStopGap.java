package com.eanonymous.open.java.commons.wishful;

import com.eanonymous.open.java.commons.log.Log;
import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * This class contains utilities that probably shouldn't exist outside of Atlas.
 * 
 * It's used as a stop gap measure until EnSoft can integrate or implement a
 * better solution.
 * 
 * @author Ben Holland
 */
public class JavaStopGap {

	/**
	 * An undocumented, but very useful Atlas attribute.
	 * 
	 * This attribute key corresponds to the raw function signature as a string,
	 * which includes the return type, function name, and parameters.
	 */
	public static final String SIGNATURE = "##signature";
	
	/**
	 * A tag placed on class variable assignments. This is added to be
	 * symmetrical with the instance variable assignment scheme.
	 */
	public static final String CLASS_VARIABLE_ASSIGNMENT = "CLASS_VARIABLE_ASSIGNMENT";

	/**
	 * A tag placed on class variable values. This is added to be symmetrical
	 * with the instance variable assignment scheme.
	 */
	public static final String CLASS_VARIABLE_VALUE = "CLASS_VARIABLE_VALUE";

	/**
	 * A tag placed on a class variable access. This is added to be symmetrical
	 * with the instance variable assignment scheme.
	 */
	public static final String CLASS_VARIABLE_ACCESS = "CLASS_VARIABLE_ACCESS";
	
	/**
	 * Adds CLASS_VARIABLE_ASSIGNMENT, CLASS_VARIABLE_VALUE, and
	 * CLASS_VARIABLE_ACCESS tags to reads/writes on static variables
	 */
	public static void addClassVariableAccessTags() {
		Log.info("Adding class variable access tags...");
		Q classVariables = Common.universe().nodesTaggedWithAny(XCSG.ClassVariable);
		Q interproceduralDataFlowEdges = Common.universe().edgesTaggedWithAny(XCSG.InterproceduralDataFlow);
		AtlasSet<Node> classVariableAssignments = interproceduralDataFlowEdges.predecessors(classVariables).eval().nodes();
		for (GraphElement classVariableAssignment : classVariableAssignments) {
			classVariableAssignment.tag(CLASS_VARIABLE_ASSIGNMENT);
			classVariableAssignment.tag(CLASS_VARIABLE_ACCESS);
		}
		AtlasSet<Node> classVariableValues = interproceduralDataFlowEdges.successors(classVariables).eval().nodes();
		for (GraphElement classVariableValue : classVariableValues) {
			classVariableValue.tag(CLASS_VARIABLE_VALUE);
			classVariableValue.tag(CLASS_VARIABLE_ACCESS);
		}
	}

	/**
	 * Removes CLASS_VARIABLE_ASSIGNMENT, CLASS_VARIABLE_VALUE, and
	 * CLASS_VARIABLE_ACCESS tags to reads/writes on static variables
	 */
	public static void removeClassVariableAccessTags() {
		Log.info("Removing class variable access tags...");
		Q classVariables = Common.universe().nodesTaggedWithAny(XCSG.ClassVariable);
		Q interproceduralDataFlowEdges = Common.universe().edgesTaggedWithAny(XCSG.InterproceduralDataFlow);

		// untag class variable assignments
		AtlasSet<Node> classVariableAssignments = interproceduralDataFlowEdges.predecessors(classVariables).eval()
				.nodes();
		AtlasHashSet<Node> classVariableAssignmentsToUntag = new AtlasHashSet<Node>();
		for (Node classVariableAssignmentToUntag : classVariableAssignments) {
			classVariableAssignmentsToUntag.add(classVariableAssignmentToUntag);
		}
		while (!classVariableAssignmentsToUntag.isEmpty()) {
			Node classVariableAssignmentToUntag = classVariableAssignmentsToUntag.getFirst();
			classVariableAssignmentsToUntag.remove(classVariableAssignmentToUntag);
			classVariableAssignmentToUntag.tags().remove(CLASS_VARIABLE_ASSIGNMENT);
			classVariableAssignmentToUntag.tags().remove(CLASS_VARIABLE_ACCESS);
		}
		// untag class variable values
		AtlasSet<Node> classVariableValues = interproceduralDataFlowEdges.successors(classVariables).eval().nodes();
		AtlasHashSet<Node> classVariableValuesToUntag = new AtlasHashSet<Node>();
		for (Node classVariableValueToUntag : classVariableValues) {
			classVariableValuesToUntag.add(classVariableValueToUntag);
		}
		while (!classVariableValuesToUntag.isEmpty()) {
			Node classVariableValueToUntag = classVariableValuesToUntag.getFirst();
			classVariableValuesToUntag.remove(classVariableValueToUntag);
			classVariableValueToUntag.tags().remove(CLASS_VARIABLE_VALUE);
			classVariableValueToUntag.tags().remove(CLASS_VARIABLE_ACCESS);
		}
	}

}
