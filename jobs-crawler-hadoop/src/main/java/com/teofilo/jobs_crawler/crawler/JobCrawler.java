package com.teofilo.jobs_crawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.teofilo.jobs_crawler.Main;

public class JobCrawler implements Runnable{
	private Document doc;
	private String url;
	private String baseUrl;
	
	public JobCrawler(String url) {
		this.url = url;
		this.baseUrl = this.findBaseUrl(url);
	}
	
	public void run () {
		String page = new PageDownloader(url).downlad();
		this.doc = Jsoup.parse(page);
		Elements linkJobsDetail = this.doc.getElementsByClass("link-detalhes-vaga");
		for(Element linkJobDetail : linkJobsDetail) {
			String path = linkJobDetail.attr("href");
			Main.jobLinks.add(this.baseUrl + path);
			System.out.println("Path: " + path);
			
		}
		Main.pagesToCrawler--;
	}
	
	private String findBaseUrl(String url) {
		int index = url.indexOf("/", 8); // ignoring http:// || http://
		return url.substring(0, index);
	}

}
