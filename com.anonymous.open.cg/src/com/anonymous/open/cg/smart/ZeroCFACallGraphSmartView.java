package com.anonymous.open.cg.smart;

import com.anonymous.open.cg.analysis.CGAnalysis;
import com.anonymous.open.cg.analysis.ZeroControlFlowAnalysis;
import com.anonymous.open.cg.preferences.CallGraphPreferences;
import com.ensoftcorp.atlas.core.log.Log;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.java.core.script.Common; 

public class ZeroCFACallGraphSmartView extends CallGraphSmartView {

	@Override
	public String getTitle() {
		return "0-CFA Call Graph";
	}

	@Override
	protected Q getCallGraph() {
		CGAnalysis cgAnalysis = ZeroControlFlowAnalysis.getInstance();
		if(!CallGraphPreferences.isZeroCFAEnabled()){
			Log.warning(cgAnalysis.getName() + " has not been run. Smart View will not contain results.");
			return Common.empty();
		}
		return cgAnalysis.getCallGraph();
	}

}
