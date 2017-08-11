package com.eanonymous.open.java.commons.analysis;

import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Common set definitions which are useful for program analysis
 * 
 * @author Tom Deering, Ben Holland
 */
public final class SetDefinitions {

	// hide constructor
	private SetDefinitions() {}

	/**
	 * A collection of specifically just the JDK libraries
	 * @return
	 */
	public static Q JDKLibraries(){
		AtlasSet<Node> libraries = new AtlasHashSet<Node>();
		// TODO: expand list for OpenJDK and Windows systems (this list was compiled from a listing of JDK 8 jars on a standard Mac system)
		String[] jdkLibraries = new String[] { "resources.jar", "rt.jar", "jsse.jar", "jce.jar", "charset.jar",
				"jfr.jar", "cldrdata.jar", "dnsns.jar", "jaccess.jar", "jfxrt.jar", "localedata.jar", "nashorn.jar",
				"sunec.jar", "sunjce_provider.jar", "sunpkcs11.jar", "zipfs.jar", "MRJToolkit.jar", "local_policy.jar",
				"US_export_policy.jar" };
		for(String jdkLibrary : jdkLibraries){
			libraries.addAll(Common.universe().nodesTaggedWithAny(XCSG.Library).selectNode(XCSG.name, jdkLibrary).eval().nodes());
		}
		return Common.toQ(libraries);
	}
	
	/**
	 * Types which represent arrays of other types
	 * 
	 * NOTE: These nodes are NOT declared by anything. They are outside of any
	 * project.
	 */
	public static Q arrayTypes() {
		return com.anonymous.open.commons.analysis.SetDefinitions.arrayTypes();
	}

	/**
	 * Types which represent language primitive types
	 * 
	 * NOTE: These nodes are NOT declared by anything. They are outside of any
	 * project.
	 */
	public static Q primitiveTypes() {
		return com.anonymous.open.commons.analysis.SetDefinitions.primitiveTypes();
	}

	/**
	 * Everything declared under any of the known API projects, if they are in
	 * the index.
	 */
	public static Q libraries() {
		return com.anonymous.open.commons.analysis.SetDefinitions.libraries();
	}
	
	/**
	 * Everything in the universe which is part of the app (not part of the
	 * libraries, or any "floating" nodes).
	 */
	public static Q app() {
		return com.anonymous.open.commons.analysis.SetDefinitions.app();
	}

}
