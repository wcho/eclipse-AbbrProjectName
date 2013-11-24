package cho.abbrprojectname.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import cho.abbrprojectname.AbbrProjectNameActivator;
import cho.abbrprojectname.AbbrProjectNameLabelDecorator;
import cho.abbrprojectname.ToggleAbbrProjectNamesDelegate;

public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	StringFieldEditor abbr;
	
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(AbbrProjectNameActivator.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		abbr = new StringFieldEditor("abbr", "&Abbreviations:", getFieldEditorParent());
		addField(abbr);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setDescription("Comma separated 'project_name=abbreviation' style abbreviation rules " +
				"(e.g. rules 'org.eclipse={EC}' will abbreviate 'org.eclipse.ui' to '{EC}.ui').");
	}
	
	@Override
	protected void performApply() {
		AbbrProjectNameLabelDecorator.parseAbbreviationPattern(abbr.getStringValue());
		ToggleAbbrProjectNamesDelegate.refreshPackageExplorer();
	}
	
	@Override
	public boolean performOk() {
		super.performOk();
		performApply();
		return true;
	}
}