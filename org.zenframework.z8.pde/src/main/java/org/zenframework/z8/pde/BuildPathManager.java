package org.zenframework.z8.pde;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.zenframework.z8.pde.build.Z8ProjectBuilder;

public class BuildPathManager {

	public final static String SOURCE_PATHS_DEFAULT = "src/bl;src/main/bl";
	public final static String OUTPUT_PATH_DEFAULT = "./.java";
	//public final static String JAVA_CLASS_DEFAULT_FOLDER = "classes";
	//public final static String DOCS_DEFAULT_FOLDER = "docs";

	public final static String SOURCE_PATHS_KEY = "sources";
	public final static String OUTPUT_PATH_KEY = "output";
	//public final static String CLASS_OUTPUT_PATH_KEY = "JavaClasses";
	//public final static String DOCS_OUTPUT_PATH_KEY = "Docs";

	static public IPath[] getSourcePaths(IProject project) {
		return getPaths(project, SOURCE_PATHS_KEY, SOURCE_PATHS_DEFAULT);
	}

	static public IPath getOutputPath(IProject project) {
		return getPath(project, OUTPUT_PATH_KEY, OUTPUT_PATH_DEFAULT);
	}

	/*static public IPath getClassOutputPath(IProject project) {
		return getPath(project, CLASS_OUTPUT_PATH_KEY, JAVA_CLASS_DEFAULT_FOLDER);
	}*/

	/*static public IPath getDocsOutputPath(IProject project) {
		return getPath(project, DOCS_OUTPUT_PATH_KEY, DOCS_DEFAULT_FOLDER);
	}*/

	/*
	 * static public IPath getWebInfPath(IProject project) { return
	 * getPath(project, WEBINF_PATH_KEY, null); }
	 */

	static protected IPath[] getPaths(IProject project, String key, String defaultValue) {
		String[] strs = getProperty(project, key, defaultValue).split("\\s*[,;]\\s*");
		IPath[] paths = new IPath[strs.length];
		for (int i = 0; i < paths.length; i++) {
			IPath path = new Path(strs[i]);
			paths[i] = path.isAbsolute() ? path : project.getLocation().append(path);
		}
		return paths;
	}

	static protected IPath getPath(IProject project, String key, String defaultValue) {
		IPath path = new Path(getProperty(project, key, defaultValue));
		return path.isAbsolute() ? path : project.getLocation().append(path);
	}

	static protected String getProperty(IProject project, String key, String defaultValue) {
		try {
			IProjectDescription desc = project.getDescription();
			List<ICommand> commands = Arrays.asList(desc.getBuildSpec());
			String value = null;

			for (ICommand command : commands) {
				if (Z8ProjectBuilder.Id.equals(command.getBuilderName())) {
					Map<String, String> arguments = command.getArguments();
					value = arguments != null ? arguments.get(key) : null;
					break;
				}
			}

			if (value != null)
				return value;
		} catch(CoreException e) {
			Plugin.log(e);
		}
		return defaultValue;
	}

}
