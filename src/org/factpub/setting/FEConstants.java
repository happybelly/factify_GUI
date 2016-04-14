package org.factpub.setting;

import java.io.File;

public interface FEConstants {
	
	static final String WINDOW_TITLE = "Fact Pub (ver0.8)";
	static final int TABLE_COLUMN_NUM = 2;
	static final int TABLE_COLUMN_FILE = 0;
	static final int TABLE_COLUMN_STATUS = 1;
	
	static final String[] TABLE_COLUMN_HEADINGS = {"File", "Status"};
	
	static final int MAX_THREADS = 2;
	
	static final String STATUS_UPLOADING = "Uploading...";
	static final String STATUS_UPLOAD_DONE = "Upload Success!";
	static final String STATUS_UPLOAD_FAILED = "Failed to upload.";
	
	// below constants must be the same to serverRequestHandler.go
	static final String SERVER_RES_TITLE_BEGIN = "BEGINOFPAGETITLE:";
	static final String SERVER_RES_TITLE_END = ":ENDOFPAGETITLE";
	
	static final String DIR_FE_HOME = System.getProperty("java.io.tmpdir") + "factpub";
	//static final String DIR_FE_HOME = System.getProperty("user.home") + File.separator + "factpub";
	static final String DIR_RULE_INPUT =  DIR_FE_HOME + File.separator + "Rule_INPUT";
	static final String DIR_JSON_OUTPUT =  DIR_FE_HOME + File.separator + "JSON";
	
	static final String FILE_RULE_MATCHER = DIR_RULE_INPUT + File.separator + "RuleMatcher.json";
	static final String FILE_ANNOUNCEMENT = DIR_FE_HOME + File.separator + "announcement.txt";
	static final String FILE_LOG = DIR_FE_HOME + File.separator + "log.txt";
	
	static final String IP_ADDRESS = "factpub.org";
	
	static final String IMAGE_DND = "Drop-Academic-Papers(PDF)-Here.png";
	
	static final String SERVER_ANNOUNCEMENT = "http://" + IP_ADDRESS + "/public/announcement.txt";
	static final String SERVER_TOP = "http://" + IP_ADDRESS + "/wiki/";
	static final String SERVER_PUBLIC = "http://" + IP_ADDRESS + "/public";
	static final String SERVER_JSON_SAVE_DIR = "http://" + IP_ADDRESS + "/public/facts_GUIFactExtractor";
	
	static final String SERVER_POST_HANDLER = "http://" + IP_ADDRESS + ":8080/uploadGUIFactExtractor";
	static final String SERVER_API = "http://" + IP_ADDRESS + "/wiki/api.php?";
	static final String SERVER_UPLOAD_FILE_NAME = "uploadfile";
	
	static final String PAGE_CREATED = "http://" + IP_ADDRESS + "/wiki/index.php/";
	static final String PAGE_REGISTER = "http:/" + IP_ADDRESS + "/wiki/index.php?title=Special:UserLogin&returnto=FactPub&type=signup";
	
	static final String FE_STATUS_CODE_MINUS_1 = "Input parameter error";
	static final String FE_STATUS_CODE_0 = "Input file not exist";
	static final String FE_STATUS_CODE_1 = "Uploading...";
	static final String FE_STATUS_CODE_2 = "PDF Converter Failed";
	static final String FE_STATUS_CODE_3 = "PDF Converter Succeeded, but no body text (or section heading";
	static final String FE_STATUS_CODE_4 = "Facts Exists";
	
	static final String[] FILES_RULE_INPUT = {
			"comparative_1.txt",
			"comparative_1_old.txt",	// added on 1-APR
			"comparative_keywords_from_Liu.txt", 
			"invalid_words_for_ngram.txt",
			"negation_bioscope.txt",
			"negative-words.txt",
			"operator_1.txt",
			"opinions_1.txt",	// added on 1-APR
			"rel_1.txt",
			"Rule_POSTag_Comparatives.txt",
			"Rule_POSTag_Nouns.txt",
			"Rule_RegExp.txt",
			"RuleMatcher.json",
			"signal_1.txt",
			"uncertainty_bioscope.txt",
			"uncertainty-words.txt"
	};
	
}
