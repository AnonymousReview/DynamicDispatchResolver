package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.java.core.script.Common;

public class ReflectionUsage extends Property {

	@Override
	public String getName(){
		return "Reflection Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds usage of Java Reflection.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"All uses of Reflection are through java.lang.reflect package."};
	}

	@Override
	public List<Result> getResults(Q context) {
		// get all the java.lang.reflect methods
		Q reflectionPackage = Common.universe().pkg("java.lang.reflect");
		Q reflectionMethods = reflectionPackage.children().nodesTaggedWithAny(XCSG.Method);
		Q objectMethodOverrides = Common.edges(XCSG.Overrides).reverse(
				CommonQueries.declarations(Common.typeSelect("java.lang", "Object")).nodesTaggedWithAny(XCSG.Method));
		reflectionMethods = reflectionMethods.difference(objectMethodOverrides, Common.methods("getName"), Common.methods("getSimpleName"));
		
		List<Result> results = new LinkedList<Result>();
		for(Node reflectionMethod : reflectionMethods.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(reflectionMethod), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("Reflection Usage", interaction));
			}
		}
		
		return results;
	}
	
}
