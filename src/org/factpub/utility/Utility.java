package org.factpub.utility;

public class Utility {
		/**
		 * Return file extension from file name.
		 * @param fileName 
		 * @return fileExtension
		 */
		public static String getSuffix(String fileName) {
		    if (fileName == null)
		        return null;
		    int point = fileName.lastIndexOf(".");
		    if (point != -1) {
		        return fileName.substring(point + 1);
		    }
		    return fileName;
		}
}

