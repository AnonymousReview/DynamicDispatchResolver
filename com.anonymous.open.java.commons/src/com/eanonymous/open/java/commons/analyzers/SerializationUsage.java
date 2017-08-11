package com.eanonymous.open.java.commons.analyzers;

import java.util.LinkedList;
import java.util.List;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.anonymous.open.commons.analyzers.Property;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

public class SerializationUsage extends Property {
	
	@Override
	public String getName(){
		return "Serialization Usage";
	}
	
	@Override
	public String getDescription() {
		return "Finds uses of Serializable classes.";
	}
	
	@Override
	public String[] getAssumptions() {
		return new String[]{"A serializer implements the Serializable interface"};
	}

	@Override
	public List<Result> getResults(Q context) {
		Q supertypeEdges = Common.universe().edges(XCSG.Supertype);
		Q serializers = supertypeEdges.reverse(Common.typeSelect("java.io", "Serializable"));
		Q serializerConstructors = serializers.children().nodes(XCSG.Constructor);
		
		// TODO: should really filter these results to just Serializable types passed to ObjectOutputStream's/marshaling APIs
		// see https://www.javatpoint.com/serialization-in-java
		// may need to use data flow or ideally points-to analysis results to track aliases
		
		List<Result> results = new LinkedList<Result>();
		for(Node serializerConstructor : serializerConstructors.eval().nodes()){
			Q interaction = CommonQueries.interactions(context, Common.toQ(serializerConstructor), XCSG.Call);
			if(!interaction.eval().edges().isEmpty()){
				results.add(new Result("Serialization Usage", interaction));
			}
		}
		return results;
	}
	
}
