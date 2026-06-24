package org.zenframework.z8.pde.preferences;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.zenframework.z8.pde.ColorProvider;
import org.zenframework.z8.pde.ColorRestartManager;
import org.zenframework.z8.pde.Plugin;

public class EditorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button btn_alwaysSave;
	private ColorSelector color_multi_line_comment;
	private ColorSelector color_single_line_comment;
	private ColorSelector color_keyword;
	private ColorSelector color_attribute;
	private ColorSelector color_type;
	private ColorSelector color_string;
	private ColorSelector color_default;
	private ColorSelector color_doc_keyword;
	private ColorSelector color_doc_tag;
	private ColorSelector color_doc_link;
	private ColorSelector color_doc_default;

	@Override
	protected Control createContents(Composite parent) {
		// GridData gd = null;
		// Object grd = null;

		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);

		btn_alwaysSave = new Button(composite, SWT.CHECK);
		btn_alwaysSave.setText("&Save editor on all actions");

		Composite colorComposite = new Composite(composite, SWT.NONE);
		colorComposite.setLayout(new GridLayout(2, false));
		colorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		color_multi_line_comment = initColorSelector(colorComposite, "Multi-line comment color");
		color_single_line_comment = initColorSelector(colorComposite, "Single-line comment color*");
		color_keyword = initColorSelector(colorComposite, "Keyword color*");
		color_attribute = initColorSelector(colorComposite, "Attribute color*");
		color_type = initColorSelector(colorComposite, "Type color*");
		color_string = initColorSelector(colorComposite, "String color*");
		color_default = initColorSelector(colorComposite, "Default text color*");
		color_doc_keyword = initColorSelector(colorComposite, "Documentation keyword color");
		color_doc_tag = initColorSelector(colorComposite, "Documentation tag color");
		color_doc_link = initColorSelector(colorComposite, "Documentation link color");
		color_doc_default = initColorSelector(colorComposite, "Default documentation text color");

		new Label(colorComposite, SWT.NONE)
			.setText("  * - requires IDE restart");

		initializeValues();
		return composite;
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	public boolean performOk() {
		storeValues();
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		initializeDefaults();
		super.performDefaults();
	}

	private void initializeDefaults() {
		btn_alwaysSave.setSelection(false);

		color_multi_line_comment.setColorValue(ColorProvider.Colors.MULTI_LINE_COMMENT.getRGBDefault());
		color_single_line_comment.setColorValue(ColorProvider.Colors.SINGLE_LINE_COMMENT.getRGBDefault());
		color_keyword.setColorValue(ColorProvider.Colors.KEYWORD.getRGBDefault());
		color_attribute.setColorValue(ColorProvider.Colors.ATTRIBUTE.getRGBDefault());
		color_type.setColorValue(ColorProvider.Colors.TYPE.getRGBDefault());
		color_string.setColorValue(ColorProvider.Colors.STRING.getRGBDefault());
		color_default.setColorValue(ColorProvider.Colors.DEFAULT.getRGBDefault());
		color_doc_keyword.setColorValue(ColorProvider.Colors.DOC_KEYWORD.getRGBDefault());
		color_doc_tag.setColorValue(ColorProvider.Colors.DOC_TAG.getRGBDefault());
		color_doc_link.setColorValue(ColorProvider.Colors.DOC_LINK.getRGBDefault());
		color_doc_default.setColorValue(ColorProvider.Colors.DOC_DEFAULT.getRGBDefault());
	}

	private void initializeValues() {
		btn_alwaysSave.setSelection(doGetPreferenceStore().getBoolean(PreferencePageConsts.ATTR_EDITOR_ALWAYS_SAVE));

		color_multi_line_comment.setColorValue(ColorProvider.Colors.MULTI_LINE_COMMENT.getRGB());
		color_single_line_comment.setColorValue(ColorProvider.Colors.SINGLE_LINE_COMMENT.getRGB());
		color_keyword.setColorValue(ColorProvider.Colors.KEYWORD.getRGB());
		color_attribute.setColorValue(ColorProvider.Colors.ATTRIBUTE.getRGB());
		color_type.setColorValue(ColorProvider.Colors.TYPE.getRGB());
		color_string.setColorValue(ColorProvider.Colors.STRING.getRGB());
		color_default.setColorValue(ColorProvider.Colors.DEFAULT.getRGB());
		color_doc_keyword.setColorValue(ColorProvider.Colors.DOC_KEYWORD.getRGB());
		color_doc_tag.setColorValue(ColorProvider.Colors.DOC_TAG.getRGB());
		color_doc_link.setColorValue(ColorProvider.Colors.DOC_LINK.getRGB());
		color_doc_default.setColorValue(ColorProvider.Colors.DOC_DEFAULT.getRGB());
		validatePage();
	}

	private void validatePage() {
		setErrorMessage(null);
		setValid(true);
	}

	private void storeValues() {
		IPreferenceStore store = doGetPreferenceStore();
		store.setValue(PreferencePageConsts.ATTR_EDITOR_ALWAYS_SAVE, btn_alwaysSave.getSelection());

		ColorProvider.Colors.MULTI_LINE_COMMENT.setColor(color_multi_line_comment.getColorValue());
		ColorProvider.Colors.SINGLE_LINE_COMMENT.setColor(color_single_line_comment.getColorValue());
		ColorProvider.Colors.KEYWORD.setColor(color_keyword.getColorValue());
		ColorProvider.Colors.ATTRIBUTE.setColor(color_attribute.getColorValue());
		ColorProvider.Colors.TYPE.setColor(color_type.getColorValue());
		ColorProvider.Colors.STRING.setColor(color_string.getColorValue());
		ColorProvider.Colors.DEFAULT.setColor(color_default.getColorValue());
		ColorProvider.Colors.DOC_KEYWORD.setColor(color_doc_keyword.getColorValue());
		ColorProvider.Colors.DOC_TAG.setColor(color_doc_tag.getColorValue());
		ColorProvider.Colors.DOC_LINK.setColor(color_doc_link.getColorValue());
		ColorProvider.Colors.DOC_DEFAULT.setColor(color_doc_default.getColorValue());

		ColorRestartManager.applyRestartIfNeeded(getShell());
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Plugin.getDefault().getPreferenceStore();
	}

	static private ColorSelector initColorSelector(Composite parent, String labelText) {
		Label lblMultiLineComment = new Label(parent, SWT.NONE);
		lblMultiLineComment.setText(labelText);

		return new ColorSelector(parent);
	}

}
