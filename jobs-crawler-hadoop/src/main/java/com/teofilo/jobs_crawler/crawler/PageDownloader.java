package com.teofilo.jobs_crawler.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageDownloader {

	private String url;

	public PageDownloader (String url) {
		this.url = url;
	}

	public String downlad () {
		URL url;
		try {
			url = new URL(this.url);

			HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			
			///System.out.println("Request sent! Status code: " +  connection.getResponseCode());
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = input.readLine()) != null) {
				response.append(inputLine);
			}
			
			input.close();
			//System.out.println(response.toString());
			return response.toString();
		} catch (Exception e) {
			System.out.println("Error downloading page: " + e.getMessage());
			return "";
		}

	} 
}
