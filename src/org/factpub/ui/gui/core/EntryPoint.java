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

package org.factpub.ui.gui.core;

import org.factpub.factify.utility.Utility;
import org.factpub.ui.gui.MainFrame;
import org.factpub.ui.gui.network.AuthMediaWikiIdHTTP;
import org.factpub.ui.gui.network.PostFile;
import org.factpub.ui.gui.utility.FEConstants;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.*;

public class EntryPoint implements FEConstants {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		
		// Initialize %userhome%/factpub folder
		InitTempDir.initTempDir();
		
		if(args.length > 0){
			// CUI mode
			
			//Create Option instance
	        Options options = new Options();

	        //-f Option
	        Option file =
	            OptionBuilder
	                .hasArg(true)					//Does parameter take argument?
	                .withArgName("file")			//Name of the parameter.
	                .isRequired(true)				//Is parameter required?
	                .withDescription("academic papers (pdfs): -f paper1.pdf -f paper2.pdf ...") //Usage
	                .withLongOpt("file")			//Synonym for the option
	                .create("f");					//Create the option
	        
	        Option user =
		            OptionBuilder
		                .hasArg(true)    
		                .withArgName("username")   
		                .isRequired(false)
		                .withDescription("factpub id (requires <password>)")  
		                .withLongOpt("user")
		                .create("u");   
	        
	        Option password =
		            OptionBuilder
		                .hasArg(true)
		                .withArgName("password")
		                .isRequired(false)
		                .withDescription("password (requires <username>)")
		                .withLongOpt("password")
		                .create("p");

	        options.addOption(file);
	        options.addOption(password);
	        options.addOption(user);

	        //Create Parser
	        CommandLineParser parser = new PosixParser();

	        //Analyze
	        CommandLine cmd = null;
	        try {
	            cmd = parser.parse(options, args);
	        } catch (ParseException e) {
	            //Show help and close
	            HelpFormatter help = new HelpFormatter();
	            help.printHelp("java -jar factpub_uploader.jar", options, true);
	            return;
	        }
	        
	        System.out.println("Run in CUI mode");
			
	        if (cmd.hasOption("u") && cmd.hasOption("p")){
	        	System.out.println("User authentication starts.");
	        	try {
					AuthMediaWikiIdHTTP.authMediaWikiAccount(cmd.getOptionValue("u"), cmd.getOptionValue("p"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("login failed");
				}
	        }else if((cmd.hasOption("u") && !cmd.hasOption("p")) || (!cmd.hasOption("u") && cmd.hasOption("p"))){
	        	System.out.println("<username> and <password> must be given together.");
	        	System.out.println("Log in as Anonymouse user.");
	        }
	        
	        //Check result
	        if (cmd.hasOption("f")) {
	            String[] pdfs = cmd.getOptionValues("f");
	            for(String pdf : pdfs){
	            	File pdf_file = new File(pdf);
		            String status = FEWrapper.GUI_Wrapper(pdf_file);
		            
		            //Networking function for CUI
		            if(!status.equals(FE_STATUS_CODE_0)){
		        		
		            	File json = new File(FEConstants.DIR_JSON_OUTPUT + File.separator + Utility.getFileNameMD5(pdf_file));
		        		
		        		try{
		        			
		        			System.out.println("Start uploading " + json.getName() + " to " + FEConstants.SERVER_API);
		        			List<String> res = PostFile.uploadToFactpub(json);    				
		        			System.out.println("PostFile.uploadToFactpub end");
		        			
		        			// If the server returns page title, put it into the array so browser can open the page when user click it.
		        			if(res.get(0).contains(FEConstants.SERVER_RES_TITLE_BEGIN)){
		        				
		        				//Embedding HyperLink
		        				String pageTitle = (String) res.get(0).subSequence(res.get(0).indexOf(FEConstants.SERVER_RES_TITLE_BEGIN) + FEConstants.SERVER_RES_TITLE_BEGIN.length(), res.get(0).indexOf(FEConstants.SERVER_RES_TITLE_END));
		        				pageTitle = pageTitle.replace(" ", "_");
		        				System.out.println(pageTitle);
		        				String uriString = FEConstants.PAGE_CREATED + pageTitle;
		        			
		        				status = "Page is created: " + uriString;
		        				
		        				//change table color        				
		        			}else{
		        				status = "Upload success but page was not created.";      				
		        			}      			
		        			// embed HTML to the label
		        		}catch(Exception e){
		            		status = FEConstants.STATUS_UPLOAD_FAILED;
		        		}
		        		System.out.println(status);
		       		}
		       	}
	        }
	        
		}else{
			// GUI mode
			System.out.println("If you want to run in console, please give me arguments.");
			/*
				usage: factpub_uploader.jar -f <pdf file> [-p <password>] [-u <username>]
				 -f,--file <pdf file>       academic papers (pdfs)
				 -p,--password <password>   password (requires <username>)
				 -u,--user <username>       factpub id (requires <password>)
			 */
			
			System.out.println("Run in GUI mode");
			MainFrame.launchGUI();
		}
		
	}

}
