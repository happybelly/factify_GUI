package org.factpub.network;

import java.io.File;

import org.factpub.setting.FEConstants;

public class Announcement {
	public static String getNewAnnounce() {
		// Create a Directory under user home 
		File fileAnnounce = new File(FEConstants.DIR_RULE_INPUT);
		if(!fileAnnounce.exists()){
			fileAnnounce.mkdir();
		}
		return fileAnnounce.getAbsolutePath();
	}
}
