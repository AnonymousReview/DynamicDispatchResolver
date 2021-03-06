package com.anonymous.open.commons.ui.views.filter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anonymous.open.commons.filters.Filter;
import com.anonymous.open.commons.filters.Filters;
import com.anonymous.open.commons.filters.InvalidFilterParameterException;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;

public class FilterRootNode implements FilterTreeNode {

	private static Set<String> uniqueNames = new HashSet<String>();
	
	private LinkedList<FilterTreeNode> children = new LinkedList<FilterTreeNode>();
	private LinkedList<Filter> applicableFilters = new LinkedList<Filter>();
	
	private Graph rootSet;
	private String name;
	private boolean expanded;
	
	public FilterRootNode(Q rootSet, String name, boolean expanded){
		this(rootSet, name, expanded, false);
	}
	
	public FilterRootNode(Q rootSet, String name, boolean expanded, boolean allowEmptyRootset){
		this.rootSet = rootSet.eval();
		if(!allowEmptyRootset && rootSet.eval().nodes().isEmpty()){
			throw new IllegalArgumentException("Root set input is empty.");
		}
		this.name = name;
		if(uniqueNames.contains(name)){
			throw new IllegalArgumentException("Root set names must be unique.");
		} else {
			uniqueNames.add(name);
		}
		this.expanded = expanded;
		this.applicableFilters.addAll(Filters.getApplicableFilters(rootSet));
	}
	
	public String getName(){
		return name;
	}
	
	public void rename(String newName){
		if(uniqueNames.contains(newName)){
			throw new IllegalArgumentException("Root set names must be unique.");
		} else {
			uniqueNames.remove(name);
			this.name = newName;
			uniqueNames.add(newName);
		}
	}

	@Override
	public FilterTreeNode getParent() {
		return null;
	}

	@Override
	public Graph getOutput() {
		return rootSet;
	}

	@Override
	public List<Filter> getApplicableFilters() {
		return new LinkedList<Filter>(applicableFilters);
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}
	
	@Override
	public void setExpanded(boolean expanded){
		this.expanded = expanded;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterRootNode other = (FilterRootNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public List<FilterTreeNode> getChildren() {
		return new LinkedList<FilterTreeNode>(children);
	}

	public void delete() {
		uniqueNames.remove(name);
	}

	@Override
	public void addChild(Filter filter, Map<String, Object> filterParameters) throws InvalidFilterParameterException {
		children.add(new FilterNode(this, Common.toQ(getOutput()), filter, filterParameters, true));
		setExpanded(true);
	}
}
