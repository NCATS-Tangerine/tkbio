/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class WebTests {
	
	private WebDriver driver;
	private String url;
	
	public WebTests(WebDriver driver, String url) {
		this.driver = driver;
		this.url = url;
	}
	
	public void TestEnterWebsite() throws InterruptedException {
		driver.get(url);
		TestGetRidOfPopUp();
		
	}
	public void TestDatabaseMapping() throws InterruptedException {
		//Grab search element
		WebElement search = driver.findElement(By.id("concept_search_string"));
		
		Thread.sleep(1000);
		search.sendKeys("diabetes");
		Thread.sleep(1000);
		search.submit();
		Thread.sleep(1000);
		driver.findElement(By.id("EC147938")).click();
		Thread.sleep(1000);
	}
	
	/**
	 * This currently does not work due to Selenium unable to find the element
	 * Although I have tried to do the same with xpath (which allows you to select
	 * elements without needing to have an id), it still does not work.
	 * It may have to do with how the q-tip content is being called. This might
	 * also be an issue with Vaadin since I have read that Vaadin generates some
	 * of the elements asynchronously. 
	 * @throws InterruptedException
	 */
	public void TestRelations() throws InterruptedException {
		WebElement bipolarDisorderLink = driver.findElement(By
				.id("MAIN_ER_relation_12221825_subject_C0005586"));
		
		Thread.sleep(1000);
		bipolarDisorderLink.click();
		Thread.sleep(2000);
		WebElement subjectInfo = driver.findElement(By.className("qtip-content"));
		//WebElement subjectInfo = driver
		//.findElement(By.xpath("//button[contains(text(), 'Show Relations')"));
		System.out.println(subjectInfo.getText());
		List<WebElement> buttons = subjectInfo.findElements(By.tagName("buttons"));
		
		System.out.println(buttons.size());
		
	}
	
	private void TestGetRidOfPopUp() throws InterruptedException {
		Thread.sleep(2000);
		//Grab search element
		WebElement search = driver
				.findElement(By.xpath("//a[contains(@title, 'Knowledge.Bio')]"));
		/* This is to simulate clicking on the search button.
		   This is more for making the initial welcome pop-up; 
		   the sendKeys used later can be done without simulating 
		   a mouse click											*/ 
		Actions clickOutside = new Actions(driver);
		Thread.sleep(1000);
		clickOutside.moveToElement(search);
		clickOutside.click().perform();
		Thread.sleep(1000);
	}
	
	public void TestAboutPage() throws InterruptedException {
		Thread.sleep(1000);
		WebElement about = driver.findElement(By.id("about_page"));
		about.click();
		TestGetRidOfPopUp();	
	}
	
	public void TestHelpPage() throws InterruptedException {
		Thread.sleep(1000);
		WebElement about = driver.findElement(By.id("help_page"));
		about.click();
		TestGetRidOfPopUp();
	}
	
	public void TestContactPage() throws InterruptedException {
		Thread.sleep(1000);
		WebElement about = driver.findElement(By.id("contactus_page"));
		about.click();
		TestGetRidOfPopUp();
	}
}
