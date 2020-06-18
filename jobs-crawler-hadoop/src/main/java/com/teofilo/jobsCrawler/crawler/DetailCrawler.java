package com.teofilo.jobsCrawler.crawler;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.teofilo.jobsCrawler.Main;
import com.teofilo.jobsCrawler.entities.JobDetail;
import com.teofilo.jobsCrawler.entities.JobKey;
import com.teofilo.jobsCrawler.rabbitmq.Produtor;
import com.teofilo.jobsCrawler.util.Semaphore;


public class DetailCrawler implements Runnable{

	private String url;
	private Semaphore semaphore;
	
	public DetailCrawler(String url, Semaphore semaphore) {
		this.url = url;
		this.semaphore = semaphore;
	}
	
	public void run () {
		try {
			String page = new PageDownloader(url).downlad();
			Document doc = Jsoup.parse(page);
			
			Elements locationElement = doc.getElementsByClass("info-localizacao");
			String location = locationElement.get(0).text();
			
			Elements salaryElement = doc.getElementsByClass("icone-salario");
			String salaryText = salaryElement.get(0).nextElementSibling().child(1).text(); //Return R$ 1000.00
			//TODO REFACT
			
			Float salary = parseSalary(salaryText);
			
			if(salary == null) {
				return;
			}
			
			Elements roleElement = doc.getElementsByClass("job-hierarchylist__item job-hierarchylist__item--level");
			String role = roleElement.text();
			
			System.out.printf("[%s - %s] %s%.2f\n",location, role,"|",salary);
			
			Produtor producer = new Produtor("1", "123", "1231");
			producer.publish(new JobDetail(location, salary, role));
			
						
		}catch(Exception e) {
			System.out.println("Erro Parsing Page!");
		}
		
	}
	

	private static Float parseSalary(String text) {
		Float result = null;
		Pattern p = Pattern.compile("([0-9]*([,.][0-9]*))");
		Matcher m = p.matcher(text);
		
		while(m.find()) {
			return Float.parseFloat(m.group().replace(".", "").replace(",", ""));
		}
		return result;
	}
	
}
