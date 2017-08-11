package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.java.core.script.Common;

public class SystemExitUsage extends Property {

	@Override
	public String getName(){
		return "System.exit Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds calls to System.exit that terminate the program's execution.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"System.exit is called directly."};
	}

	@Override
	public List<Result> getResults(Q context) {
		Q exitMethods = Common.methodSelect("java.lang", "System", "exit");
		List<Result> results = new LinkedList<Result>();
		for(Node exitMethod : exitMethods.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(exitMethod), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("System.exit Usage", interaction));
			}
		}
		return results;
	}
	
}
