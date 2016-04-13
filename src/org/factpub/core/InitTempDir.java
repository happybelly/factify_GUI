package org.factpub.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.factpub.gui.MainFrame;
import org.factpub.setting.FEConstants;

public class InitTempDir implements FEConstants {

	public static String makeTempDir() {
		// Create a Directory under user home 
		File dirTemp = new File(DIR_FE_HOME);
		if(!dirTemp.exists()){
			dirTemp.mkdirs();
		}
		return dirTemp.getAbsolutePath();
	}
	
	public static String makeJsonDir() {
		// Create a Directory under user home 
		File dirJSON = new File(DIR_JSON_OUTPUT);
		if(!dirJSON.exists()){
			dirJSON.mkdirs();
		}
		return dirJSON.getAbsolutePath();
	}

	public static String makeRuleINPUTDir() {
		// Create a Directory under user home 
		File dirRuleINPUT = new File(DIR_RULE_INPUT);
		if(!dirRuleINPUT.exists()){
			dirRuleINPUT.mkdirs();
		}
		return dirRuleINPUT.getAbsolutePath();
	}
	
	public static void copyRuleInputFiles() {
		final String[] filesRuleInput = FILES_RULE_INPUT;
		
		// Copy the contents of Rule_INPUT
		for(int i = 0; i < filesRuleInput.length ;i++){
			try{
				String outFile = DIR_RULE_INPUT;
		        File file = new File(outFile, filesRuleInput[i]);				
		        if(!file.exists()){

			        // Set up input files
			        InputStream is = MainFrame.class.getClassLoader().getResourceAsStream(filesRuleInput[i]);
			        
			        //BufferedReader reader = getJarReader("Rule_INPUT/RuleMatcher.json");
			        System.out.println(file.toPath());
			        try {
			            Files.copy(is, file.toPath());
			        } catch (IOException e) {
			            e.printStackTrace();
			            System.out.println("File copy error");
			        }
		        }
		        System.out.println(file.getName() + " already exists.");
		        
			}catch(Exception e){
				System.out.println("resource access error");
			}
		}
	}
	
}
