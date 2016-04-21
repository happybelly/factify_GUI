package org.factpub.network;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import org.factpub.gui.MainFrame;
import org.factpub.utility.FEConstants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class AuthMediaWikiId {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static String authorisedUser = "Anonymous";
	
	public static void authMediaWikiAccount (String wikiId, String wikiPass) throws Exception  {
		
		AuthMediaWikiId http = new AuthMediaWikiId();
		
		System.out.println("\nAuthMediaWikiId - Send Http POST request");
		
		String returns1 = http.sendPost1(wikiId, wikiPass,"","");
		
		JsonElement jelement1 = new JsonParser().parse(returns1);
	    JsonObject  jobject1 = jelement1.getAsJsonObject();
	    
	    JsonElement token = jobject1.get("token");
	    JsonElement sessionid = jobject1.get("sessionid");
		
	    JsonElement resultAuth1 = jobject1.get("result");
	    
		    if(resultAuth1.toString().replace("\"","").equals("NeedToken")){
		    	
			    // 2nd attempt 
				String returns2 = http.sendPost2(wikiId, wikiPass, sessionid.toString().replace("\"", ""), token.toString().replace("\"", ""));
				
				JsonElement jelement2 = new JsonParser().parse(returns2);
			    JsonObject  jobject2 = jelement2.getAsJsonObject();
			    
			    JsonElement resultAuth2 = jobject2.get("result");

				String auth =  resultAuth2.toString().replace("\"", "");
				System.out.println(auth);
				
				switch(auth){
				case "NoName" :
					System.out.println("NoName");
					authorisedUser = "Anonymous";
					break;
				case "NotExists" :
					System.out.println("NotExists");
					JOptionPane.showMessageDialog(MainFrame.frameMain, "User does not exist.");
					authorisedUser = "Anonymous";
					break;
				case "WrongPass" :
					System.out.println("WrongPass");
					JOptionPane.showMessageDialog(MainFrame.frameMain, "User exists but password is wrong.");
					authorisedUser = "Anonymous";
					break;
				case "Success" :
					System.out.println("Success");
					JOptionPane.showMessageDialog(MainFrame.frameMain, "Authentication Success!");
					
					// get User Name from JSON
					JsonElement username = jobject2.get("lgusername");
				    String authorisedUser = username.toString().replace("\"", "");
				    
				    // replace space with underscore
				    authorisedUser = authorisedUser.replace(" ", "_");
				    
					//MainFrame.txtUserAuth.setText(authorisedUser);
					break;			
				}			
				
		    }
	}

	// HTTP POST request
	private String sendPost1(String lgname, String lgpassword, String cookie, String lgtoken) throws Exception {

		String url = FEConstants.SERVER_API;
			
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				
		String urlParameters = "format=json&action=login&lgname=" + lgname + "&lgpassword=" + lgpassword + "&lgtoken=" + lgtoken;
				
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		System.out.println("Header : " + con.getHeaderFields().toString());
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String resstr = response.toString();
		
		//JSONArray jobj = new JSONArray(str);
		JsonElement jelement = new JsonParser().parse(resstr);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("login");
	    
	  
		//print result
	    System.out.println(jobject.toString() + '\n');
		//System.out.println(token + " " + cookieprefix + " " + sessionid);
		
		
		
		return jobject.toString();

	}
	
	private String sendPost2(String lgname, String lgpassword, String cookie, String lgtoken) throws Exception {

		String url = FEConstants.SERVER_API;
			
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Cookie", "paper_facts_session=" + cookie);
		
		//String urlParameters = "api.php";
		
		String urlParameters = "format=json&action=login&lgname=" + lgname + "&lgpassword=" + lgpassword + "&lgtoken=" + lgtoken;
				
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		System.out.println("Header : " + con.getHeaderFields().toString());
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String resstr = response.toString();
		
		//JSONArray jobj = new JSONArray(str);
		JsonElement jelement = new JsonParser().parse(resstr);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("login");
	    
	   
		
		//print result
	    System.out.println(jobject.toString() + '\n');
		//System.out.println(token + " " + cookieprefix + " " + sessionid);
		
		
		
		return jobject.toString();

	}
	
}