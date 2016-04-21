package org.factpub.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.factpub.gui.MainFrame;
import org.factpub.utility.FEConstants;

public class InitTempDir implements FEConstants {

	public static String makeTempDir() {
		// Create a Directory under user home 
		File dirTemp = new File(DIR_FE_HOME);
		delete(dirTemp);
		if(!dirTemp.exists()){
			dirTemp.mkdirs();
		}
		return dirTemp.getAbsolutePath();
	}
	
	public static String makeJsonDir() {
		// Create a Directory under user home 
		File dirJSON = new File(DIR_JSON_OUTPUT);
		delete(dirJSON);
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
	
	public static String makeLogFile() {
		// Create a Directory under user home 
		File fileLog = new File(FILE_LOG);
		if(!fileLog.exists()){
			try {
				fileLog.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("cannot create log file.");
				e.printStackTrace();
			}
		}
		return fileLog.getAbsolutePath();
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
		        System.out.println(file.getName() + " is created.");
		        
			}catch(Exception e){
				System.out.println("resource access error.");
			}
		}
	}
	
    /*
     * Delete file or directory
     */
    private static void delete(File f){
        /*
         * Don't do anything if file or directory does not exist
         */
        if(f.exists() == false) {
            return;
        }

        if(f.isFile()) {
            /*
             * if it's file, delete it.
             */
            f.delete();

        } else if(f.isDirectory()){
            /*
             * if it's directory, delete all the contents'
             */

            /*
             * get the contents
             */
            File[] files = f.listFiles();

            /*
             * delete all files and directory
             */
            for(int i=0; i<files.length; i++) {
                /*
                 * use recursion
                 */
                delete( files[i] );
            }
            /*
             * delete itself
             */
            f.delete();
        }
    }
	
}
