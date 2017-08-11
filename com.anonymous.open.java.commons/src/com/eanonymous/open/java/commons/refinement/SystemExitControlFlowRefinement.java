package com.eanonymous.open.java.commons.refinement;

import org.eclipse.core.runtime.IProgressMonitor;

import com.anonymous.open.commons.analysis.CallSiteAnalysis;
import com.anonymous.open.commons.analysis.G;
import com.anonymous.open.commons.codemap.PrioritizedCodemapStage;
import com.eanonymous.open.java.commons.log.Log;
import com.eanonymous.open.java.commons.preferences.JavaCommonsPreferences;
import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Attr;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Runs the control flow graph refinement step as a codemap stage
 * 
 * @author Jon Mathews, refinement logic
 * @author Ben Holland, conversion to prioritized codemap
 */
public class SystemExitControlFlowRefinement extends PrioritizedCodemapStage {

	/**
	 * The unique identifier for the codemap stage
	 */
	public static final String IDENTIFIER = "com.ensoftcorp.open.java.refinement.cfsysexit";
	
	@Override
	public String getDisplayName() {
		return "Refining control flow following System.exit callsites";
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String[] getCodemapStageDependencies() {
		return new String[]{}; // no dependencies
	}

	@Override
	public void performIndexing(IProgressMonitor monitor) {
		if(JavaCommonsPreferences.isSystemExitControlFlowRefinementEnabled()){
			Log.info("Refining control flow following System.exit...");
			// remove outgoing control flow edges after System.exit
			AtlasSet<Node> exits = findMethod("java.lang.System", "exit").eval().nodes();
			AtlasSet<Node> callSites = CallSiteAnalysis.getCallSites(exits);
			AtlasSet<Edge> controlFlowEdgesToDelete = new AtlasHashSet<Edge>();
			for (Node callSite : callSites) {
				Node controlFlowNode = G.in(Graph.U, callSite, XCSG.Contains);
				AtlasSet<Edge> outEdges = G.outEdges(Graph.U, controlFlowNode, XCSG.ControlFlow_Edge);
				controlFlowEdgesToDelete.addAll(outEdges);
			}
			while(!controlFlowEdgesToDelete.isEmpty()){
				Edge edgeToDelete = controlFlowEdgesToDelete.one();
				controlFlowEdgesToDelete.remove(edgeToDelete);
				Graph.U.delete(edgeToDelete);
			}
		}
	}
	
	private static Q findType(String binaryName) {
		Q system = Common.universe().selectNode(Attr.Node.BINARY_NAME, binaryName);
		return system;
	}
	
	private static Q findMethod(String binaryName, String methodName) {
		Q t = findType(binaryName);
		Q methods = t.children().methods(methodName);
		return methods;
	}

}
