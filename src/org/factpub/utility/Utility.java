/**
 *  Author: Sun SAGONG
 *  Copyright (C) 2016, Genome Institute of Singapore, A*STAR
 *   
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.factpub.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
		
		public static String getFileNameMD5(String file_path) {
			File file = new File(file_path);
			String fileNameMD5 = getFileNameMD5(file);
			return fileNameMD5;
		}
		
		public static String getFileNameMD5(File file){
			utility.utility util = new utility.utility();
			String fileNameMD5 = util.MD5(file.getPath()) + "_facts.json";
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

