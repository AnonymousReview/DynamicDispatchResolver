package com.anonymous.open.commons.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.anonymous.open.commons.Activator;
import com.anonymous.open.commons.log.Log;
import com.anonymous.open.commons.subsystems.Subsystem;
import com.anonymous.open.commons.subsystems.Subsystems;

public class SubsystemPreferences extends AbstractPreferenceInitializer {

	@SuppressWarnings("unused")
	private static boolean initialized = false;
	
	/**
	 * Enables or disables the subsystem category
	 * @param subsystemCategory
	 * @param enabled
	 */
	public static void enableSubsystemCategory(String subsystemCategory, boolean enabled){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(subsystemCategory, enabled);
	}
	
	/**
	 * Returns true if the subsystem category is enabled for tagging
	 * 
	 * @param subsystemCategory
	 * @return
	 */
	public static boolean isSubsystemCategoryEnabled(String subsystemCategory){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		Boolean result = preferences.getBoolean(subsystemCategory);
		return result.booleanValue();
	}
	
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		// disable subsystem tagging categories by default
		Subsystems.loadSubsystemContributions();
		for(Subsystem subsystem : Subsystems.getRegisteredSubsystems()){
			preferences.setDefault(subsystem.getCategory(), false);
		}
	}
	
	/**
	 * Restores the default preferences
	 */
	public static void restoreDefaults(){
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		// disable subsystem tagging categories by default
		Subsystems.loadSubsystemContributions();
		for(Subsystem subsystem : Subsystems.getRegisteredSubsystems()){
			preferences.setValue(subsystem.getCategory(), false);
		}
		loadPreferences();
	}
	
	/**
	 * Loads or refreshes current preference values
	 */
	public static void loadPreferences() {
		try {
			Activator.getDefault().getPreferenceStore();
		} catch (Exception e){
			Log.warning("Error accessing commons subsystem preferences, using defaults...", e);
		}
		initialized = true;
	}

}