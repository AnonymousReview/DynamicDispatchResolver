package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.java.core.script.Common;

public class ProcessUsage extends Property {

	@Override
	public String getName(){
		return "Process Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds calls to methods that would allow the application to run a shell command.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"The only way to run shell commands is by directly invoking the Runtime.exec method."};
	}

	@Override
	public List<Result> getResults(Q context) {
		Q runtimeType = Common.typeSelect("java.lang", "Runtime");
		Q runtimeMethods = runtimeType.children().nodesTaggedWithAny(XCSG.Method);
		Q execMethods = runtimeMethods.intersection(Common.methods("exec"));
		List<Result> results = new LinkedList<Result>();
		for(Node execMethod : execMethods.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(execMethod), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("Process Usage", interaction));
			}
		}
		return results;
	}
	
}
