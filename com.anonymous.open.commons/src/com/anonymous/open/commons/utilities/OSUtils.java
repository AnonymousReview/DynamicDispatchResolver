package com.anonymous.open.commons.utilities;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

/**
 * A set of helper utilities for determining the operating system, 
 * host name, and dealing with relative file paths
 * 
 * @author Ben Holland
 */
public class OSUtils {

	private OSUtils() {}
	
	private static String OS = null;
	private static boolean isWindows = false;
	private static boolean isMac = false;
	private static boolean isUnix = false;

	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		if (OS.startsWith("Windows")) {
			isWindows = true;
		} else if (OS.contains("OS X")) {
			isMac = true;
		} else {
			isUnix = true;
		}
		return OS;
	}

	public static boolean isWindows() {
		if (OS == null)
			getOsName();
		return isWindows;
	}

	public static boolean isMac() {
		if (OS == null)
			getOsName();
		return isMac;
	}

	public static boolean isUnix() {
		if (OS == null)
			getOsName();
		return isUnix;
	}

	public static String getSystemName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
	
	/**
	 * A class for getting relative file paths
	 * Source modified from original version at  
	 * http://stackoverflow.com/questions/204784/how-to-construct-a-relative-path-in-java-from-two-absolute-paths-or-urls
	 */
	public static class ResourceUtils {
		/**
		 * Get the relative path from one file to another, specifying the
		 * directory separator. If one of the provided resources does not exist,
		 * it is assumed to be a file unless it ends with '/' or '\'.
		 * 
		 * @param targetPath targetPath is calculated to this file
		 * @param basePath basePath is calculated from this file
		 * @param pathSeparator directory separator. The platform default is not assumed
		 *        so that we can test Unix behavior when running on Windows (for example)
		 * @return
		 */
		public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

			// normalize the paths
			String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
			String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

			// undo the changes to the separators made by normalization
			if (pathSeparator.equals("/")) {
				normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
				normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);

			} else if (pathSeparator.equals("\\")) {
				normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
				normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);

			} else {
				throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
			}

			String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
			String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

			// first get all the common elements. Store them as a string,
			// and also count how many of them there are.
			StringBuffer common = new StringBuffer();

			int commonIndex = 0;
			while (commonIndex < target.length && commonIndex < base.length && target[commonIndex].equals(base[commonIndex])) {
				common.append(target[commonIndex] + pathSeparator);
				commonIndex++;
			}

			if (commonIndex == 0) {
				// no single common path element. This most
				// likely indicates differing drive letters, like C: and D:.
				// These paths cannot be relativized.
				throw new PathResolutionException("No common path element found for '" + 
						normalizedTargetPath + "' and '" + normalizedBasePath + "'");
			}

			// the number of directories we have to backtrack depends on whether
			// the base is a file or a dir
			// for example, the relative path from
			//
			// /foo/bar/baz/gg/ff to /foo/bar/baz
			//
			// ".." if ff is a file
			// "../.." if ff is a directory
			//
			// the following is a heuristic to figure out if the base refers to
			// a file or dir. It's not perfect, because
			// the resource referred to by this path may not actually exist, but
			// it's the best I can do
			boolean baseIsFile = true;

			File baseResource = new File(normalizedBasePath);

			if (baseResource.exists()) {
				baseIsFile = baseResource.isFile();

			} else if (basePath.endsWith(pathSeparator)) {
				baseIsFile = false;
			}

			StringBuffer relative = new StringBuffer();

			if (base.length != commonIndex) {
				int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

				for (int i = 0; i < numDirsUp; i++) {
					relative.append(".." + pathSeparator);
				}
			}
			relative.append(normalizedTargetPath.substring(common.length()));
			return relative.toString();
		}

		static class PathResolutionException extends RuntimeException {
			private static final long serialVersionUID = 1L;

			PathResolutionException(String msg) {
				super(msg);
			}
		}
	}
}
