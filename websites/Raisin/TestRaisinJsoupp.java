package io.naztech.jobharvestar.scraper;

/**
 * Raisin job site parser<br>
 * URL: https://www.raisin.com/careers/
 * 
 * @author rokybul.rayhan
 * @since 2019-05-15
 */

import java.io.IOException;

import org.junit.Test;


import io.naztech.jobharvestar.model.Job;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
@Slf4j
public class TestRaisinJsoupp extends TestAbstractScrapper {
	@Test
	public void testGetJobList() throws IOException {
		Document doc = Jsoup.connect("https://www.raisin.com/careers/").get();
		Elements jobR = doc.select("a[class=job-item]");
		System.out.println(jobR.size());
		for(int i=0;i<jobR.size();i++) {
		    testGetJobdetails(jobR.get(i).attr("href"));
			
			
		}
	}
	public void testGetJobdetails(String url) throws IOException 
	{	
		 Job job = new Job(url);
		 Document doc1 = Jsoup.connect(url).get();
		 System.out.println(url);
		 Element job1 = doc1.selectFirst("div[class= col-sm-10 job-detail-desc]").selectFirst("h1");
		 System.out.println(job1.text());
		 Element job2 = doc1.selectFirst("div[class = col-md-6 pull-right]");
		     job2 = doc1.selectFirst("div[class= col-sm-10 job-detail-desc]").selectFirst("p");
			 log.info(job2.text().split("\u00B7")[0].trim());
			 log.info(job2.text().split("\u00B7")[1].trim());
			 job2 = doc1.selectFirst("div[class=inner]");
			 log.info("jobPrerecuisit: " + job2.text());
			 job2 = doc1.selectFirst("div[class=col-md-6 pull-right]");
			 log.info("Jobspec: " + job2.text());
			 job2 = doc1.selectFirst("div[class=col-md-6]");
			 log.info("JobSpec: " + job2.text());
						
		
	}
	
}
