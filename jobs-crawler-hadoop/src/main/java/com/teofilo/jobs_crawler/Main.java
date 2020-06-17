package com.teofilo.jobs_crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teofilo.jobs_crawler.crawler.DetailCrawler;
import com.teofilo.jobs_crawler.crawler.JobCrawler;
import com.teofilo.jobs_crawler.entities.JobKey;
import com.teofilo.jobs_crawler.util.Semaphore;

public class Main {
	
	public static final List <String> jobLinks = new ArrayList<String>();
	public static final Map<JobKey, Float> jobSalaryMap = new HashMap<JobKey, Float>();
	public static final List<JobKey> jobs = new ArrayList<JobKey>();
	public static int pagesToCrawler = 4;
	private static int detailId = 0;

	public static void main(String[] args) {	
		Semaphore semaphore = new Semaphore();
		System.out.println("Jobs Crawler was initialized!");	
		
		for (int i=0; i < pagesToCrawler; i++) {
			JobCrawler crawler = new JobCrawler("https://www.vagas.com.br/vagas-de-tecnologia?&q=tecnologia&pagina=" + i + "&_=1591921673291");
			new Thread(crawler, "JobCrawler - Thread: " + i).start();
		}
		
		while (pagesToCrawler>0 || !jobLinks.isEmpty()) {
			System.out.println(".");
			if(!jobLinks.isEmpty()) {
				DetailCrawler crawler = new DetailCrawler(jobLinks.remove(0), semaphore);		
				new Thread(crawler, "DetailCrawler" + detailId++).start();
			}
		}	
	}
}
