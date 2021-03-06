package com.anonymous.open.jimple.commons.filters;

import java.util.Map;

import com.anonymous.open.commons.filters.InvalidFilterParameterException;
import com.anonymous.open.commons.filters.NodeFilter;
import com.anonymous.open.jimple.commons.loops.DecompiledLoopIdentification.CFGNode;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Filters loop based on how deeply they are locally nested
 * Does not consider inter-procedural nesting
 * Supports decompiled and source Java loops
 * 
 * @author Ben Holland
 */
public class LoopNestingDepthFilter extends NodeFilter {

	private static final String DEPTH_GREATER_THAN = "DEPTH_GREATER_THAN";
	private static final String DEPTH_GREATER_THAN_EQUAL_TO = "DEPTH_GREATER_THAN_EQUAL_TO";
	private static final String DEPTH_LESS_THAN = "DEPTH_LESS_THAN";
	private static final String DEPTH_LESS_THAN_EQUAL_TO = "DEPTH_LESS_THAN_EQUAL_TO";

	public LoopNestingDepthFilter() {
		this.addPossibleParameter(DEPTH_GREATER_THAN, Integer.class, false, "Filters loops with local nesting depth less than or equal to the specified value");
		this.addPossibleParameter(DEPTH_GREATER_THAN_EQUAL_TO, Integer.class, false, "Filters loops with local nesting depth less than the specified value");
		this.addPossibleParameter(DEPTH_LESS_THAN, Integer.class, false, "Filters loops with local nesting depth greater than or equal to the specified value");
		this.addPossibleParameter(DEPTH_LESS_THAN_EQUAL_TO, Integer.class, false, "Filters loops with local nesting depth greater than the specified value");
		this.setMinimumNumberParametersRequired(1);
	}

	@Override
	public String getName() {
		return "Loop Nesting Depth";
	}

	@Override
	public String getDescription() {
		return "Filters loop headers based on thier local nesting depth.";
	}

	@Override
	public Q filter(Q input, Map<String,Object> parameters) throws InvalidFilterParameterException {
		checkParameters(parameters);
		input = super.filter(input, parameters);
		
		AtlasSet<Node> result = new AtlasHashSet<Node>();
		Q loopChildEdges = Common.universe().edgesTaggedWithAny(XCSG.LoopChild).retainEdges();
		Q loopRoots = loopChildEdges.roots();
		Q loopHeaders = loopChildEdges.nodesTaggedWithAny(CFGNode.LOOP_HEADER);

		for (Node header : loopHeaders.eval().nodes()) {
			Q path = loopChildEdges.between(loopRoots, Common.toQ(header));
			long depth = path.eval().edges().size();
			
			boolean add = true;
			
			if(isParameterSet(DEPTH_GREATER_THAN, parameters)){
				int min = (Integer) getParameterValue(DEPTH_GREATER_THAN, parameters);
				if(depth <= min){
					add = false;
				}
			}
			
			if(isParameterSet(DEPTH_GREATER_THAN_EQUAL_TO, parameters)){
				int minEq = (Integer) getParameterValue(DEPTH_GREATER_THAN_EQUAL_TO, parameters);
				if(depth < minEq){
					add = false;
				}
			}
			
			if(isParameterSet(DEPTH_LESS_THAN, parameters)){
				int max = (Integer) getParameterValue(DEPTH_LESS_THAN, parameters);
				if(depth >= max){
					add = false;
				}
			}
			
			if(isParameterSet(DEPTH_LESS_THAN_EQUAL_TO, parameters)){
				int maxEq = (Integer) getParameterValue(DEPTH_LESS_THAN_EQUAL_TO, parameters);
				if(depth > maxEq){
					add = false;
				}
			}
			
			if(add){
				result.add(header);
			}
		}
		
		return super.filter(Common.toQ(result), parameters);
	}

	@Override
	protected String[] getSupportedNodeTags() {
		return new String[]{ XCSG.Loop, CFGNode.LOOP_HEADER };
	}

}
