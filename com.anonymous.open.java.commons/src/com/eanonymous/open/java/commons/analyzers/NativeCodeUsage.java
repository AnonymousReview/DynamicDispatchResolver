package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.java.core.script.Common;

public class NativeCodeUsage extends Property {

	@Override
	public String getName(){
		return "Native Code Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds calls to native code.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"All native calls are using JNI and are flagged with the keyword native."};
	}

	@Override
	public List<Result> getResults(Q context) {
		Q nativeMethods = context.nodesTaggedWithAny(XCSG.Java.nativeMethod);
		List<Result> results = new LinkedList<Result>();
		for(Node nativeMethod : nativeMethods.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(nativeMethod), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("Native Method Usage", interaction));
			}
		}
		return results;
	}
	
}
