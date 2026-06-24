package org.zenframework.z8.pde;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.zenframework.z8.pde.preferences.PreferencePageConsts;


public class ColorProvider {
	public enum Colors {
		MULTI_LINE_COMMENT(PreferencePageConsts.ATTR_COLOR_MULTI_LINE_COMMENT, new RGB(128, 0, 0)),
		SINGLE_LINE_COMMENT(PreferencePageConsts.ATTR_COLOR_SINGLE_LINE_COMMENT, new RGB(128, 128, 0), true),
		KEYWORD(PreferencePageConsts.ATTR_COLOR_KEYWORD, new RGB(0, 0, 255), true),
		ATTRIBUTE(PreferencePageConsts.ATTR_COLOR_ATTRIBUTE, new RGB(50, 128, 128), true),
		TYPE(PreferencePageConsts.ATTR_COLOR_TYPE, new RGB(0, 0, 255), true),
		STRING(PreferencePageConsts.ATTR_COLOR_STRING, new RGB(0, 128, 0), true),
		DEFAULT(PreferencePageConsts.ATTR_COLOR_DEFAULT, new RGB(0, 0, 0), true),
		DOC_KEYWORD(PreferencePageConsts.ATTR_COLOR_DOC_KEYWORD, new RGB(0, 128, 0), true),
		DOC_TAG(PreferencePageConsts.ATTR_COLOR_DOC_TAG, new RGB(128, 128, 128)),
		DOC_LINK(PreferencePageConsts.ATTR_COLOR_DOC_LINK, new RGB(128, 128, 128)),
		DOC_DEFAULT(PreferencePageConsts.ATTR_COLOR_DOC_DEFAULT, new RGB(0, 128, 128));

		private String setting = null;
		private RGB defaultRGB = null;
		private boolean restartRequired = false;
		private Color color = null;

		private Colors(String setting, RGB defaultRGB) {
			this.setting = setting;
			this.defaultRGB = defaultRGB;
			PreferenceConverter.setDefault(doGetPreferenceStore(), setting, defaultRGB);
		}

		private Colors(String setting, RGB defaultRGB, boolean restartRequired) {
			this(setting, defaultRGB);
			this.restartRequired = restartRequired;
		}

		public RGB getRGB() {
			return PreferenceConverter.getColor(doGetPreferenceStore(), setting);
		}

		public RGB getRGBDefault() {
			return defaultRGB;
		}

		public Color getColor() {
			Color color = this.color;
			if (color == null)
				this.color = color = new Color(Display.getCurrent(), getRGB());
			return color;
		}

		public void dispose() {
			if (this.color != null)
				this.color.dispose();
		}

		public void setColor(RGB rgb) {
			if (getRGB().equals(rgb))
				return;
			PreferenceConverter.setValue(doGetPreferenceStore(), setting, rgb);
			color = new Color(Display.getCurrent(), rgb);

			if (restartRequired)
				ColorRestartManager.markRestartRequired();
		}

		static private IPreferenceStore doGetPreferenceStore() {
			return Plugin.getDefault().getPreferenceStore();
		}
	}

	public void dispose() {
		for(Colors color : Colors.values())
			color.dispose();
	}
}
