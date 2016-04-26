package org.factpub.utility;

import java.io.File;

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
		
		public static String getFileNameMD5(File file){
			utility.utility util = new utility.utility();
			String fileNameMD5 = FEConstants.DIR_JSON_OUTPUT + File.separator + util.MD5(file.getPath()) + "_facts.json";
			return fileNameMD5;
		}
		
}

