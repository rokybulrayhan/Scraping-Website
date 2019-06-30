package io.naztech.jobharvestar.scraper;

import static org.junit.Assert.*;
/**
 * JollyTech job site parser<br>
 * URL: https://www.jollytech.com/company/jobs/positions/index.php
 * 
 * @author rokybul.rayhan
 * @since 2019-05-09
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.naztech.jobharvestar.model.Job;
import lombok.extern.slf4j.Slf4j;
@Slf4j

public class TestJollyTech extends TestAbstractScrapper {
	private static String baseUrl = "https://www.jollytech.com/";
	private static String URL = baseUrl + "company/jobs/positions/index.php";
	private static WebClient client;
	Job job = new Job(baseUrl + URL);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = getChromeClient();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client.close();
	}
	
	
	@Test
	public void testGetjob() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		HtmlPage page = client.getPage(URL);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(TIME_10S*4);
		System.out.println(page.getUrl());
		
		List<HtmlElement> list = page.getByXPath("//div[@class='body-container body-container-bg']/p/a");
		System.out.println(list.size());
		for (HtmlElement e : list) {
			testDetails(e.getAttribute("href"));
		}
		
		 
	}
    int cnt = 0;
	private void testDetails(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Job job = new Job(baseUrl + url);
		System.out.println("**************************************");
		//HtmlPage page = client.getPage(baseUrl + url);
		Document doc = Jsoup.connect(baseUrl + url).get(); //jsoup
		Elements title = doc.select("div.body-container>p");//div[class='body-container']/p
		job.setTitle(title.get(0).text().split("Title")[1].trim());
		log.info(job.getTitle());
		job.setReferenceId(title.get(1).text().split("ID")[1].trim());
		log.info(job.getReferenceId());
		job.setLocation(title.get(2).text().split("Location")[1].trim());
		job.setSpec(title.get(3).text() + "" + title.get(4).text() + "" + title.get(5).text() + "" + title.get(6).text()
				+ "" + title.get(7).text() + "" + title.get(8).text());
		log.info(job.getLocation());
		log.info(job.getSpec());
		cnt++;
		System.out.println(cnt);
		System.out.println("-------------------------------- ");
		
	}
	
	

}
