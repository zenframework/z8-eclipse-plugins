package org.zenframework.z8.pde;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ColorRestartManager {
	private static boolean restartRequired = false;

	public static void markRestartRequired() {
		restartRequired = true;
	}

	public static void applyRestartIfNeeded(Shell shell) {
		if (!restartRequired) return;

		restartRequired = false;

		boolean restart = MessageDialog.openQuestion(
			shell,
			"Color Changed",
			"Changing the mandatory color requires an IDE restart to apply the changes. Restart now?"
		);
		

		if (restart) {
			PlatformUI.getWorkbench().restart();
		}
	}
}