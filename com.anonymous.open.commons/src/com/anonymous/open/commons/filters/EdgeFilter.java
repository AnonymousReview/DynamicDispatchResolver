package com.anonymous.open.commons.filters;

import java.util.Map;

import com.ensoftcorp.atlas.core.query.Q;

public abstract class EdgeFilter extends Filter {

	/**
	 * Node filters do not operate on nodes
	 * @return
	 */
	protected String[] getSupportedNodeTags() {
		return NOTHING;
	}
	
	/**
	 * Returns the filtered result. Note that this method enforces that the
	 * result must be a subset of the original input and retains only edges.
	 * 
	 * @return
	 */
	public Q filter(Q input, Map<String, Object> parameters) throws InvalidFilterParameterException {
		return getSupportedInput(input).intersection(input).retainEdges();
	}

}
