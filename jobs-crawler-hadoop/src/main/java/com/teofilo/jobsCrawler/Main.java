package com.teofilo.jobsCrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teofilo.jobsCrawler.crawler.DetailCrawler;
import com.teofilo.jobsCrawler.crawler.JobCrawler;
import com.teofilo.jobsCrawler.entities.JobKey;
import com.teofilo.jobsCrawler.util.Semaphore;

public class Main {
	
	public static final List <String> jobLinks = new ArrayList<String>();
	public static final Map<JobKey, Float> jobSalaryMap = new HashMap<JobKey, Float>();
	public static final List<JobKey> jobs = new ArrayList<JobKey>();
	public static int pagesToCrawler = 10;
	private static int detailId = 0;

	public static void main(String[] args) throws InterruptedException {	
		Semaphore semaphore = new Semaphore();
		System.out.println("Jobs Crawler was initialized!");	
		
		for (int i=0; i < pagesToCrawler; i++) {
			JobCrawler crawler = new JobCrawler("https://www.vagas.com.br/vagas-de-tecnologia?&q=tecnologia&pagina="+ i +"&_=1592448052168");
			new Thread(crawler, "JobCrawler - Thread: " + i).start();
			crawler = new JobCrawler("https://www.vagas.com.br/vagas-de-ti?&q=ti&pagina="+ i +"&_=1592447809212");
			new Thread(crawler, "JobCrawler - Thread: " + i).start();
			//Thread.sleep(800);
		}
		
		while (pagesToCrawler>0 || !jobLinks.isEmpty()) {
			System.out.println(".");
			if(!jobLinks.isEmpty()) {
				DetailCrawler crawler = new DetailCrawler(jobLinks.remove(0), semaphore);		
				new Thread(crawler, "DetailCrawler" + detailId++).start();
				Thread.sleep(150);
			}
		}	
	}
}
