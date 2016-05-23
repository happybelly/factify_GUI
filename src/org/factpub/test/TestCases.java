package org.factpub.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.factpub.network.*;

public class TestCases {

	// Test Case: whether login function works?
	@Test public void testCase1() throws Exception{
		AuthMediaWikiIdHTTP.authMediaWikiAccount("dummy_id", "dummy_pass");
		
	}
	
	// Test Case: whether JSON is successfully extracted from PDF
	@Test public void testCase2(){
		
		
	}
	
	// Test Case: whether JSON is successfully uploaded to http://factpub.org/public/facts_GUIFactExtractor/
	@Test public void testCase3(){

		
	}

	// Test Case: whether login id is sent as a parameter when user login before drag and drop pdf (by default “Anonymous”)
	@Test public void testCase4(){
		
		
	}
	
	// Test Case: whether page is created: regardless if PDF has DOI or not – this is current setting in serverRequestHandler.go 
	@Test public void testCase5(){
		
		
	}
	// Test Case: whether link is shown
	@Test public void testCase6(){
		
		
	}
	
	// Test Case: whether link works when user click it.
	@Test public void testCase7(){
		
		
	}
	
	// Test Case: Whether link is appropriately associated to the correct page.
	@Test public void testCase8(){
		
		
	}
	
}
