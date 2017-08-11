package com.eanonymous.open.java.commons.refinement;

import org.eclipse.core.runtime.IProgressMonitor;

import com.anonymous.open.commons.codemap.PrioritizedCodemapStage;
import com.eanonymous.open.java.commons.log.Log;
import com.eanonymous.open.java.commons.preferences.JavaCommonsPreferences;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.index.Index;
import com.ensoftcorp.atlas.core.query.Attr;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Runs the call graph refinement step as a codemap stage
 * 
 * @author Jon Mathews, refinement logic
 * @author Ben Holland, conversion to prioritized codemap
 */
public class ThreadRunnableCallSummarization extends PrioritizedCodemapStage {

	private static final String INDEX = Index.INDEX_VIEW_TAG;
	
	/**
	 * The unique identifier for the codemap stage
	 */
	public static final String IDENTIFIER = "com.ensoftcorp.open.java.refinement.threadsummary";
	
	@Override
	public String getDisplayName() {
		return "Refining call graph to include threading";
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
		if(JavaCommonsPreferences.isThreadRunnableCallSummaryEnabled()){
			Log.info("Summarizing flows from Thread.start to Runnable.run...");
			// connect Thread.start() to Runnable.run()
			for(Node start : findMethod("java.lang.Thread", "start").eval().nodes()){
				//If the project(s) under index does not use Threads, and Atlas preferences are set to map used classes only in jar, 
				//then start may be empty.  In that case, just move on
				if (start == null){
					continue;
				}
				
				Q runMethods = findMethod("java.lang.Runnable", "run").reverseOn(Query.universe().edgesTaggedWithAny(XCSG.Overrides));
				Q concreteRunMethods = runMethods.difference(Query.universe().nodesTaggedWithAny(XCSG.abstractMethod));
				
				for (GraphElement target : concreteRunMethods.eval().nodes()) {
					GraphElement edge = Graph.U.createEdge(start, target);
					edge.tag(XCSG.Call);
					edge.tag(INDEX);
				}
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
