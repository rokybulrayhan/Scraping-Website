package io.naztech.jobharvestar.scraper;
/**
 * Hannoverre job site parser<br>
 * URL: https://jobs.hannover-re.com/?ac=search_result&_csrf_token
 * 
 * @author rokybul.rayhan
 * @since 2019-05-09
 */
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.naztech.jobharvestar.model.Job;
import lombok.extern.slf4j.Slf4j;
@Slf4j

public class TestHannoverreHtmlUnit extends TestAbstractScrapper {
	private static String baseUrl = "https://jobs.hannover-re.com/";
	private static String URL = baseUrl + "?ac=search_result&_csrf_token";
	private static WebClient client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = getChromeClient();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client.close();
	}
	List<String> loc =new ArrayList<String>();
	List<String> field =new ArrayList<String>();
	@Test
	public void testGetJobList() throws IOException, InterruptedException {

		HtmlPage page = client.getPage(URL);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(TIME_10S*4);
		System.out.println(page.getUrl());
		HtmlElement el = page.getBody().getOneHtmlElementByAttribute("option", "value", "All");
		page=el.click();
		
		System.out.println(page);
		
		 List<HtmlElement> joblink = page.getByXPath("//td[@class='hidden-xs jobad-title']/a");
		 List<HtmlElement> location = page.getByXPath("//td[@class='hidden-xs']");
		
		System.out.println(joblink.size());
		int cnt = 0;
		for (HtmlElement lf : location) {
			if(cnt%2==0) {
			loc.add(lf.asText());
			}
			else {
				field.add(lf.asText());
			}
			cnt++;	
	    }
		
		for (HtmlElement e : joblink) {
			testDetails(e.getAttribute("href"));
		}
		
		int i;
		HtmlPage newPage;
	}
	int cnt = 0;
	private void testDetails(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Job job = new Job(baseUrl + url);
		HtmlPage page = client.getPage(baseUrl + url);
		Document doc = Jsoup.connect(baseUrl + url).get(); 
		Element title = doc.selectFirst("span.breadcrumb-notactive");
		Elements discription = doc.select("div#jobad_content>p"); //#mane id
		job.setSpec(discription.text());
		log.info(title.text());
		log.info("Location : "+loc.get(cnt));
		log.info("Field : "+field.get(cnt));
		log.info(job.getSpec());
		//log.info(cnt);
		//cnt++;
			
	}
}
