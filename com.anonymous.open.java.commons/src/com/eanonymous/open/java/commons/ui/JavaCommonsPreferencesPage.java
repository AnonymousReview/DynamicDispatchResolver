package com.eanonymous.open.java.commons.ui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eanonymous.open.java.commons.Activator;
import com.eanonymous.open.java.commons.preferences.JavaCommonsPreferences;

/**
 * UI for Java Commons Toolbox analysis preferences
 * 
 * @author Ben Holland
 */
public class JavaCommonsPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private static final String SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DESCRIPTION = "Refine control flow following System.exit";
	private static final String THREAD_RUNNABLE_CALL_SUMMARY_DESCRIPTION = "Apply a CHA based Thread/Runnable summary";
	
	private static boolean changeListenerAdded = false;

	public JavaCommonsPreferencesPage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(preferences);
		setDescription("Configure Java Toolbox preferences.");
		// use to update cached values if user edits a preference
		if(!changeListenerAdded){
			getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
				@Override
				public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
					// reload the preference variable cache
					JavaCommonsPreferences.loadPreferences();
				}
			});
			changeListenerAdded = true;
		}
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(JavaCommonsPreferences.SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT, "&" + SYSTEM_EXIT_CONTROL_FLOW_REFINEMENT_DESCRIPTION, getFieldEditorParent()));
		addField(new BooleanFieldEditor(JavaCommonsPreferences.THREAD_RUNNABLE_CALL_SUMMARY, "&" + THREAD_RUNNABLE_CALL_SUMMARY_DESCRIPTION, getFieldEditorParent()));
	}

}

