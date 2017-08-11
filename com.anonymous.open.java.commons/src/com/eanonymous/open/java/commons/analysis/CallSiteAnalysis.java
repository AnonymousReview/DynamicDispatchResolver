package com.eanonymous.open.java.commons.analysis;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import com.anonymous.open.commons.analysis.G;
import com.anonymous.open.commons.analysis.CallSiteAnalysis.LanguageSpecificCallSiteAnalysis;
import com.eanonymous.open.java.commons.log.Log;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.db.set.EmptyAtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

public class CallSiteAnalysis extends LanguageSpecificCallSiteAnalysis {

	// constructor must be visible to contribute as language specific analysis extension point
	public CallSiteAnalysis(){}
	
	/**
	 * Given a {@link XCSG#CallSite}, return formal {@link XCSG#Identity} arguments for possible target methods.  
	 **/
	public static Q resolveToFormalIdentity(Node callsite) { 
		return resolveToFormalIdentity(Common.toQ(callsite));
	}
	
	/**
	 * Given {@link XCSG#CallSite}s, return formal {@link XCSG#Identity} arguments for possible target methods.  
	 **/
	public static Q resolveToFormalIdentity(Q callsites) { 
		Q actualArgument = Common.universe().edgesTaggedWithAny(XCSG.IdentityPassedTo).predecessors(callsites);
		return Common.universe().edgesTaggedWithAny(XCSG.InterproceduralDataFlow)
			.successors(actualArgument); 
	}
	
	/**
	 * Given a callsite, return the method representing the invoked signature
	 * @param callsite
	 * @return
	 */
	public static Q getSignature(Node callsite) {
		return getSignature(Common.toQ(callsite));
	}
	
	/**
	 * Given call sites, returns the methods representing the invoked signatures
	 * @param callsites
	 * @return
	 */
	public static Q getSignature(Q callsites) {
		Q signature = Common.universe().edgesTaggedWithAny(XCSG.InvokedFunction, XCSG.InvokedSignature).successors(callsites);
		return signature;
	}
	
	/**
	 * Returns the set of target methods that the given callsites could resolve to
	 * @param callsites
	 * @return
	 */
	public static AtlasSet<Node> getTargetMethods(Q callsites){
		AtlasSet<Node> targets = new AtlasHashSet<Node>();
		for(Node callsite : callsites.eval().nodes()){
			targets.addAll(getTargetMethods(callsite));
		}
		return targets;
	}
	
	/**
	 * Given a StaticDispatchCallSite or a DynamicDispatchCallSite, return the methods which may
	 * have been invoked.
	 * 
	 * @param callsite
	 * @return
	 */
	public static AtlasSet<Node> getTargetMethods(Node callsite) {
		// Note: nodes and edges currently need not be bounded (i.e. any ModelElement is acceptable)
		// The following are used if present: 
		//     nodes <- DataFlow_Node | Variable 
		//     edges <- DataFlow_Edge | InvokedSignature | InvokedFunction | IdentityPassedTo
		Graph dataFlowGraph = Common.universe().eval();
		return getTargetMethods(new NullProgressMonitor(), dataFlowGraph, callsite);
	}
	
	/**
	 * Given a StaticDispatchCallSite or a DynamicDispatchCallSite, return the methods which may
	 * have been invoked.
	 * 
	 * @param monitor
	 * @param dataFlowGraph
	 * @param callsite
	 * @return
	 */
	private static AtlasSet<Node> getTargetMethods(IProgressMonitor monitor, Graph dataFlowGraph, Node callsite) {
		if (callsite.taggedWith(XCSG.StaticDispatchCallSite)) {
			return Common.toQ(dataFlowGraph).edgesTaggedWithAny(XCSG.InvokedFunction)
				.successors(Common.toQ(Common.toGraph(callsite))).eval().nodes();
		} else if (callsite.taggedWith(XCSG.DynamicDispatchCallSite)) {
			AtlasSet<Node> targetMethods = new AtlasHashSet<Node>();
			AtlasSet<Node> targetIdentities = getIdentity(dataFlowGraph, callsite).eval().nodes();
			for (Node targetIdentity : targetIdentities) {
				Node targetMethod = CommonQueries.getContainingMethod(targetIdentity);
				if (targetMethod != null){
					targetMethods.add(targetMethod);
				} else {
					Log.warning("Cannot find containing Method for Identity: " + targetIdentity);
				}
				if (monitor.isCanceled()){
					throw new OperationCanceledException();
				}
			}
			return targetMethods;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Given a call site, return formal identity arguments for possible target methods.  
	 **/
	private static Q getIdentity(Graph dataFlowGraph, Node callsite) {
		return getIdentity(Common.toQ(dataFlowGraph), Common.toQ(callsite));
	}
	
	/**
	 * Given a call site, return formal identity arguments for possible target methods. 
	 * @param dataFlowGraph
	 * @param callsites
	 * @return
	 */
	private static Q getIdentity(Q dataFlowGraph, Q callsites) { 
		Q actualArgument = dataFlowGraph.edgesTaggedWithAny(XCSG.IdentityPassedTo).predecessors(callsites);
		return dataFlowGraph.edgesTaggedWithAny(XCSG.InterproceduralDataFlow)
			.successors(actualArgument); 
	}
	
	/**
	 * Given methods, return possible call sites.
	 * @param methods
	 * @return
	 */
	public static Q getMethodCallSites(Q methods) {
		AtlasHashSet<Node> callSites = new AtlasHashSet<Node>();
		for (Node method : methods.eval().nodes()) {
			callSites.addAll(getMethodCallSites(method));
		}
		return Common.toQ(callSites);
	}

	/**
	 * Given methods, return possible call sites.
	 * @param method
	 * @return
	 */
	public static AtlasSet<Node> getMethodCallSites(Node method) {
		// if ClassMethod or Constructor, go back along InvokedFunction
		// if InstanceMethod, go back along Identity <- IdentityPass -IdentityPassedTo> CallSite
		if (method.taggedWith(XCSG.ClassMethod) || method.taggedWith(XCSG.Constructor)) {
			return G.ins(Common.universe().eval(),method,XCSG.InvokedFunction);
		} else if (method.taggedWith(XCSG.InstanceMethod)) {
			Q formalIdentity = CommonQueries.methodThis(Common.universe(), Common.toQ(method));
			
			Q df = Common.universe().edgesTaggedWithAny(XCSG.DataFlow_Edge);
			Q actualIdentities = formalIdentity.predecessorsOn(df);
			
			Q ipt = Common.universe().edgesTaggedWithAny(XCSG.IdentityPassedTo);
			Q callSites = actualIdentities.successorsOn(ipt);
			return callSites.eval().nodes();
		} else {
			return EmptyAtlasSet.<Node>instance(Graph.U);
		}
	}

	@Override
	public AtlasSet<Node> getTargets(Node callSite) {
		return getTargetMethods(callSite);
	}

	@Override
	public AtlasSet<Node> getCallSites(Node function) {
		return getMethodCallSites(function);
	}

	@Override
	public String getName() {
		return "Java Call Site Analysis";
	}

	@Override
	public String getDescription() {
		return "Resolves potential method targets for call sites and vice versa.";
	}

	@Override
	public Set<String> getSupportedLanguages() {
		HashSet<String> languages = new HashSet<String>();
		languages.add(XCSG.Language.Java);
		return languages;
	}
}
