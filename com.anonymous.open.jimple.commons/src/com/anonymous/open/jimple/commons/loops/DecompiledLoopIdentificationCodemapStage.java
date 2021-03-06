package com.anonymous.open.jimple.commons.loops;

import org.eclipse.core.runtime.IProgressMonitor;

import com.anonymous.open.commons.codemap.PrioritizedCodemapStage;
import com.anonymous.open.jimple.commons.log.Log;
import com.anonymous.open.jimple.commons.preferences.JimpleCommonsPreferences;

public class DecompiledLoopIdentificationCodemapStage extends PrioritizedCodemapStage {

	public static final String IDENTIFIER = "com.ensoftcorp.open.jimple.commons.loops.recovery";
	
	@Override
	public String getDisplayName() {
		return "Decompiled Loop Identification";
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
			if(JimpleCommonsPreferences.isDecompiledLoopRecoveryEnabled()){
				Log.info("Recovering Decompiled Loops...");
				DecompiledLoopIdentification.recoverLoops(monitor);
				Log.info("Finished Recovering Decompiled Loops");
			}
		} catch (Exception e) {
			Log.error("Error recovering decompiled loops", e);
		}
	}

}
