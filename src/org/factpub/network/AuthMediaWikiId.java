/**
    Copyright (C) 2016, Genome Institute of Singapore, A*STAR
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.factpub.network;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class AuthMediaWikiId {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static String authorisedUser = "Anonymous";
	
	public static void authMediaWikiAccount (String wikiId, String wikiPass) throws Exception  {
	
		String httpsURL = "https://factpub.org/wiki/api.php";

		String query = "format=json&action=login&lgname=" + wikiId + "&lgpassword=" + wikiPass;
		
		URL myurl = new URL(httpsURL);
		HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-length", String.valueOf(query.length())); 
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
		con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
		con.setDoOutput(true); 
		con.setDoInput(true); 

		DataOutputStream output = new DataOutputStream(con.getOutputStream());  

		output.writeBytes(query);
		output.close();

		DataInputStream input = new DataInputStream(con.getInputStream()); 

		for(int c = input.read(); c != -1; c = input.read()) 
		System.out.print( (char)c ); 
		input.close(); 

		System.out.println("Resp Code:"+con .getResponseCode()); 
		System.out.println("Resp Message:"+ con .getResponseMessage()); 
	}
}