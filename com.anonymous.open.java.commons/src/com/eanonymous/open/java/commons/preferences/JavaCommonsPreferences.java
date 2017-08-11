package com.eanonymous.open.java.commons.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.eanonymous.open.java.commons.Activator;
import com.eanonymous.open.java.commons.log.Log;

public class JavaCommonsPreferences extends AbstractPreferenceInitializer {

	private static boolean initialized = false;
	
	/**
	 * Enable/disable System.exit() control flow refinement
	 */
	public static final String SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT = "SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT";
	public static final Boolean SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DEFAULT = false;
	private static boolean systemExitControlFlowRefinementValue = SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DEFAULT;
	
	/**
	 * Configures System.exit() control flow refinement
	 */
	public static void enableSystemExitControlFlowRefinement(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT, enabled);
		loadPreferences();
	}
	
	/**
	 * Returns true if System.exit() control flow refinement is enabled
	 * @return
	 */
	public static boolean isSystemExitControlFlowRefinementEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return systemExitControlFlowRefinementValue;
	}
	
	/**
	 * Enable/disable thread runnable call summary
	 */
	public static final String THREAD_RUNNABLE_CALL_SUMMARY = "THREAD_RUNNABLE_CALL_SUMMARY";
	public static final Boolean THREAD_RUNNABLE_CALL_SUMMARY_DEFAULT = false;
	private static boolean threadRunnableCallSummaryValue = THREAD_RUNNABLE_CALL_SUMMARY_DEFAULT;
	
	/**
	 * Configures whether or not to run thread runnable call summary
	 */
	public static void enableThreadRunnableCallSummary(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(THREAD_RUNNABLE_CALL_SUMMARY, enabled);
		loadPreferences();
	}
	
	/**
	 * Returns true if thread runnable call summary is enabled
	 * @return
	 */
	public static boolean isThreadRunnableCallSummaryEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return threadRunnableCallSummaryValue;
	}
	
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setDefault(SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT, SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DEFAULT);
		preferences.setDefault(THREAD_RUNNABLE_CALL_SUMMARY, THREAD_RUNNABLE_CALL_SUMMARY_DEFAULT);
	}

	/**
	 * Restores the default preferences
	 */
	public static void restoreDefaults() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT, SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DEFAULT);
		preferences.setValue(THREAD_RUNNABLE_CALL_SUMMARY, THREAD_RUNNABLE_CALL_SUMMARY_DEFAULT);
		loadPreferences();
	}
	
	/**
	 * Loads or refreshes current preference values
	 */
	public static void loadPreferences() {
		try {
			IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
			systemExitControlFlowRefinementValue = preferences.getBoolean(SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT);
			threadRunnableCallSummaryValue = preferences.getBoolean(THREAD_RUNNABLE_CALL_SUMMARY);
		} catch (Exception e){
			Log.warning("Error accessing Java Toolbox Commons preferences, using defaults...", e);
		}
		initialized = true;
	}

}
