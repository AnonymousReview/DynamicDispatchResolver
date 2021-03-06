package com.anonymous.open.commons.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.anonymous.open.commons.Activator;
import com.anonymous.open.commons.log.Log;

public class CommonsPreferences extends AbstractPreferenceInitializer {

	private static boolean initialized = false;
	
	/**
	 * Enable/disable computing control flow graph dominance trees
	 */
	public static final String COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES = "COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES";
	public static final Boolean COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT = false;
	private static boolean computeControlFlowGraphDominanceTreesValue = COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT;
	
	/**
	 * Configures inference rule logging
	 */
	public static void enableComputeControlFlowGraphDominanceTrees(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, enabled);
		loadPreferences();
	}
	
	public static boolean isComputeControlFlowGraphDominanceTreesEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return computeControlFlowGraphDominanceTreesValue;
	}
	
	/**
	 * Enable/disable computing exceptional control flow graph dominance trees
	 */
	public static final String COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES = "COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES";
	public static final Boolean COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT = false;
	private static boolean computeExceptionalControlFlowGraphDominanceTreesValue = COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT;
	
	/**
	 * Configures inference rule logging
	 */
	public static void enableComputeExceptionalControlFlowGraphDominanceTrees(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, enabled);
		loadPreferences();
	}
	
	public static boolean isComputeExceptionalControlFlowGraphDominanceTreesEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return computeExceptionalControlFlowGraphDominanceTreesValue;
	}
	
	/**
	 * Enable/disable adding master entry/exit containment relationships
	 */
	public static final String ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS = "ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS";
	public static final Boolean ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS_DEFAULT = true;
	private static boolean addMasterEntryExitContainmentRelationships = ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS_DEFAULT;
	
	/**
	 * Configures inference rule logging
	 */
	public static void enableMasterEntryExitContainmentRelationships(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS, enabled);
		loadPreferences();
	}
	
	public static boolean isMasterEntryExitContainmentRelationshipsEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return addMasterEntryExitContainmentRelationships;
	}
	
	/**
	 * Enable/disable displaying filter view result containers
	 */
	public static final String DISPLAY_FILTER_VIEW_RESULT_CONTAINERS = "DISPLAY_FILTER_VIEW_RESULT_CONTAINERS";
	public static final Boolean DISPLAY_FILTER_VIEW_RESULT_CONTAINERS_DEFAULT = false;
	private static boolean displayFilterViewResultContainersValue = DISPLAY_FILTER_VIEW_RESULT_CONTAINERS_DEFAULT;

	/**
	 * Configures displaying filter view result containers
	 */
	public static void enableDisplayFilterViewResultContainers(boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(DISPLAY_FILTER_VIEW_RESULT_CONTAINERS, enabled);
		loadPreferences();
	}

	public static boolean isDisplayFilterViewResultContainersEnabled(){
		if(!initialized){
			loadPreferences();
		}
		return displayFilterViewResultContainersValue;
	}
	
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setDefault(COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT);
		preferences.setDefault(COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT);
		preferences.setDefault(ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS, ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS_DEFAULT);
		preferences.setDefault(DISPLAY_FILTER_VIEW_RESULT_CONTAINERS, DISPLAY_FILTER_VIEW_RESULT_CONTAINERS_DEFAULT);
	}
	
	/**
	 * Restores the default preferences
	 */
	public static void restoreDefaults(){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT);
		preferences.setValue(COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES, COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES_DEFAULT);
		preferences.setValue(ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS, ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS_DEFAULT);
		preferences.setValue(DISPLAY_FILTER_VIEW_RESULT_CONTAINERS, DISPLAY_FILTER_VIEW_RESULT_CONTAINERS_DEFAULT);
		loadPreferences();
	}
	
	/**
	 * Loads or refreshes current preference values
	 */
	public static void loadPreferences() {
		try {
			IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
			computeControlFlowGraphDominanceTreesValue = preferences.getBoolean(COMPUTE_CONTROL_FLOW_GRAPH_DOMINANCE_TREES);
			computeExceptionalControlFlowGraphDominanceTreesValue = preferences.getBoolean(COMPUTE_EXCEPTIONAL_CONTROL_FLOW_GRAPH_DOMINANCE_TREES);
			addMasterEntryExitContainmentRelationships = preferences.getBoolean(ADD_MASTER_ENTRY_EXIT_CONTAINMENT_RELATIONSHIPS);
			displayFilterViewResultContainersValue = preferences.getBoolean(DISPLAY_FILTER_VIEW_RESULT_CONTAINERS);
		} catch (Exception e){
			Log.warning("Error accessing commons preferences, using defaults...", e);
		}
		initialized = true;
	}
}