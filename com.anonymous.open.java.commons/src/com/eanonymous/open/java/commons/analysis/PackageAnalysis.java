package com.eanonymous.open.java.commons.analysis;

import com.anonymous.open.commons.analysis.CommonQueries;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

public class PackageAnalysis {
	
	private PackageAnalysis() {
		// use to prevent object construction
	}

	/**
	 * Returns a Q containing package nodes including the given package 
	 * and its subpackages
	 * @param packageName
	 * @return
	 */
	public static Q getPackageWithSubpackages(String packageName){
		Q allPackages = Common.universe().nodesTaggedWithAny(XCSG.Package);
		return CommonQueries.nodesStartingWith(allPackages, packageName);
	}
	
	/**
	 * Returns a Q containing the types in the given collection of packages
	 * @param packages
	 * @return
	 */
	public static Q getPackageTypes(Q packages){
		packages = packages.nodesTaggedWithAny(XCSG.Package);
		return CommonQueries.declarations(Common.universe(), packages).nodesTaggedWithAny(XCSG.Type);
	}
	
	/**
	 * Returns everything declared under the given package and subpackages
	 * @param basePackageName
	 * @return
	 */
	public static Q getPackageDeclarations(String basePackageName){
		return CommonQueries.declarations(Common.universe(), getPackageWithSubpackages(basePackageName));
	}
	

}
