package org.factpub.utility;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

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
		
		public static String getAnnouncement(){
			
			// Announcement check
			ArrayList<String> contentList = null;
			String content = null;
			// Check if Announcement is available.
			try {
				URL url = new URL(FEConstants.SERVER_ANNOUNCEMENT);
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String line;
				contentList = new ArrayList<String>();
				while ((line = in.readLine()) != null) {
					contentList.add(line);
				}
				in.close();
				content = StringUtils.join(contentList, " ");
			} catch (MalformedURLException e) {
				content = "NA";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				content = "Cannot connect to the factpub server.";
			}

			return content;		
		}
		
}

