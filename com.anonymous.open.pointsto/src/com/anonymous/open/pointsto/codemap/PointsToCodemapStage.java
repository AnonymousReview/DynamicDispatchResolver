package com.anonymous.open.pointsto.codemap;

import org.eclipse.core.runtime.IProgressMonitor;

import com.anonymous.open.commons.codemap.PrioritizedCodemapStage;
import com.anonymous.open.pointsto.analysis.JavaPointsTo;
import com.anonymous.open.pointsto.analysis.JimplePointsTo;
import com.anonymous.open.pointsto.analysis.PointsTo;
import com.anonymous.open.pointsto.log.Log;
import com.anonymous.open.pointsto.preferences.PointsToPreferences;
import com.anonymous.open.pointsto.utilities.GraphEnhancements;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * A hook for automatically running points-to analysis with the user's
 * preferences after Atlas creates a program graph
 * 
 * @author Ben Holland
 */
public class PointsToCodemapStage extends PrioritizedCodemapStage {

	public static final String IDENTIFIER = "com.ensoftcorp.open.pointsto";
	
	@Override
	public String getDisplayName() {
		return "Points-to Analysis";
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
		try {
			if(PointsToPreferences.isPointsToAnalysisEnabled()){
				PointsTo pointsToAnalysis = null;
				if(PointsToPreferences.isJavaPointsToAnalysisModeEnabled()){
					pointsToAnalysis = new JavaPointsTo();
					pointsToAnalysis.run();
				} else if(PointsToPreferences.isJimplePointsToAnalysisModeEnabled()){
					pointsToAnalysis = new JimplePointsTo();
					pointsToAnalysis.run();
				}
				
				// make some graph enhancements
				if(pointsToAnalysis != null){					
					if(PointsToPreferences.isTagAliasesEnabled()){
						long numMemoryModels = GraphEnhancements.serializeArrayMemoryModels(pointsToAnalysis);
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Applied " + numMemoryModels + " array memory model tags.");
						
						long numTaggedAliases = GraphEnhancements.serializeAliases(pointsToAnalysis);
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Applied " + numTaggedAliases + " aliasing tags.");
					}
					
					if(PointsToPreferences.isRewriteArrayComponentsEnabled()){
						long numArrayComponents = Common.universe().nodesTaggedWithAny(XCSG.ArrayComponents).eval().nodes().size();
						long numRewrittenArrayComponents = GraphEnhancements.rewriteArrayComponents(pointsToAnalysis);
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Rewrote " + numArrayComponents + " array components to " + numRewrittenArrayComponents + " array components.");
						
					}
					
					if(PointsToPreferences.isTagInferredDataflowsEnabled()){
						long numInferredDFEdges = GraphEnhancements.tagInferredDataFlowEdges(pointsToAnalysis);
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Applied " + numInferredDFEdges + " inferred data flow edge tags.");
					}
					
					if(PointsToPreferences.isTagRuntimeTypesEnabled()){
						long numInferredTypeOfEdges = GraphEnhancements.tagInferredTypeOfEdges(pointsToAnalysis);
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Applied " + numInferredTypeOfEdges + " inferred type of edge tags.");
					}
					
					if(PointsToPreferences.isDisposeResourcesEnabled()){
						// throw away references we don't need anymore
						if(PointsToPreferences.isGeneralLoggingEnabled()) Log.info("Disposing temporary resources...");
						pointsToAnalysis.dispose();
					}
				} else {
					throw new IllegalArgumentException("Points-to analysis was enabled with an invalid analysis mode.");
				}
			}
		} catch (Exception e) {
			Log.error("Error performing points-to analysis", e);
		}
	}

}