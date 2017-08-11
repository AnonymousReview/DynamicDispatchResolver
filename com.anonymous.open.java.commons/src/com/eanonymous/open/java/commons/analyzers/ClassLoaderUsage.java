package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.java.core.script.Common;

public class ClassLoaderUsage extends Property {

	@Override
	public String getName(){
		return "Class Loader Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds calls to Class Loaders.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"All uses of Reflection are through java.lang.reflect package."};
	}
	
	@Override
	public List<Result> getResults(Q context) {
		Q supertypeEdges = Common.universe().edges(XCSG.Supertype);
		Q overridesEdges = Common.universe().edges(XCSG.Overrides);
		Q loaders = supertypeEdges.reverse(Common.typeSelect("java.lang", "ClassLoader"));
		Q objectMethodOverrides = overridesEdges.reverse(Common.typeSelect("java.lang", "Object").contained().nodes(XCSG.Method));
		Q loaderMethods = loaders.children().nodes(XCSG.Method).difference(objectMethodOverrides);
		List<Result> results = new LinkedList<Result>();
		for(Node loaderMethod : loaderMethods.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(loaderMethod), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("Class Loader Usage", interaction));
			}
		}
		return results;
	}
	
}
