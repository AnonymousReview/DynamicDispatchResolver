package com.eanonymous.open.java.commons.filters;

import java.util.ArrayList;
import java.util.Map;

import com.anonymous.open.commons.filters.InvalidFilterParameterException;
import com.anonymous.open.commons.filters.NodeFilter;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Filters call site nodes based on how whether the call site parameter(s) are fixed literal values
 * 
 * @author Ben Holland
 */
public class LiteralPassedParameterCallSiteFilter extends NodeFilter {

	private static final String EXCLUDE_MATCHES = "EXCLUDE_MATCHES";
	private static final String PARAMETER_INDEXES = "PARAMETER_INDEXES";

	public LiteralPassedParameterCallSiteFilter() {
		this.addPossibleFlag(EXCLUDE_MATCHES, "Retain only call sites whose selected passed parameters are not literals.");
		this.addPossibleParameter(PARAMETER_INDEXES, String.class, false, "A comma seperated list of integers denoting the parameter indexes to consider. Index values begin at 0. By default all parameters are considered.");
	}
	
	@Override
	public String getName() {
		return "Call Site Literal Value Passed Parameters";
	}

	@Override
	public String getDescription() {
		return "Filters call site nodes based on how whether the call site parameter(s) are fixed literal values.";
	}

	@Override
	public Q filter(Q input, Map<String,Object> parameters) throws InvalidFilterParameterException {
		checkParameters(parameters);
		input = super.filter(input, parameters);
		
		Q parameterPassedToEdges = Common.universe().edges(XCSG.ParameterPassedTo);
		Q dataFlowEdges = Common.universe().edges(XCSG.DataFlow_Edge);
		
		AtlasSet<Node> literalOnlyCallSites = new AtlasHashSet<Node>();
		for(Node callSite : input.eval().nodes()){
			Q parametersPass = parameterPassedToEdges.predecessors(Common.toQ(callSite));
			if(this.isParameterSet(PARAMETER_INDEXES, parameters)){
				Integer[] indexes = getParameterIndexes(parameters);
				parametersPass = parametersPass.selectNode(XCSG.parameterIndex, (Object[]) indexes);
			}
			Q parameterPassValues = dataFlowEdges.predecessors(parametersPass);
			// if all passed parameter values are literals, add to results
			long parameterPassValuesSize = parameterPassValues.eval().nodes().size();
			if(parameterPassValuesSize > 0 && parameterPassValuesSize == parameterPassValues.nodes(XCSG.Literal).eval().nodes().size()){
				literalOnlyCallSites.add(callSite);
			}
		}
		
		Q result = Common.toQ(literalOnlyCallSites);
		if(isFlagSet(EXCLUDE_MATCHES, parameters)){
			return input.difference(result);
		} else {
			return result;
		}
	}

	private Integer[] getParameterIndexes(Map<String, Object> parameters) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		try {
			String inputString = (String) this.getParameterValue(PARAMETER_INDEXES, parameters);
			inputString = inputString.replaceAll("\\s","");
			String[] indexValues = inputString.split(",");
			for(String indexValue : indexValues){
				indexes.add(Integer.parseInt(indexValue));
			}
		} catch (Exception e){
			throw new RuntimeException(PARAMETER_INDEXES + " must be a comma seperated list of integers!");
		}
		Integer[] result = new Integer[indexes.size()];
		indexes.toArray(result);
		return result;
	}

	@Override
	protected String[] getSupportedNodeTags() {
		return new String[]{ XCSG.CallSite };
	}

}