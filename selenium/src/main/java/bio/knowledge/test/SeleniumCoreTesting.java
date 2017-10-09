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
 */package bio.knowledge.test;

 
//import java.io.File;
//import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxProfile;

public class SeleniumCoreTesting {

	/** The url of the website you wish to interact with */
	private static final String url = "http://knowledge.bio/";
	//private static String chromeDriverLocation;
	public static void main(String[] args) throws InterruptedException {
		//WebDriver driver = new FirefoxDriver();
		//runChromeTests();
		runFireFoxTests();	
	}

	/*
	private static void configureChromeDriver() {
		//Linux 64 version
		//chromeDriverLocation = Paths.get("src/test/resources/chromedriver_linux").toAbsolutePath().toString();
		//Mac version
		chromeDriverLocation = Paths.get("src/test/resources/chromedriver_mac").toAbsolutePath().toString();
		//Windows version
		//chromeDriverLocation = Paths.get("src/test/resources/chromedriver.exe").toAbsolutePath().toString();
		//Setting up ChromeDriver
		System.setProperty("webdriver.chrome.driver", 
				chromeDriverLocation);
	}
	*/

	/*
	private static void runChromeTests() throws InterruptedException {
		configureChromeDriver();
		WebDriver driver = new ChromeDriver();
		WebTests chromeTest = new WebTests(driver, url);
		
		chromeTest.TestEnterWebsite();
		chromeTest.TestDatabaseMapping();
		chromeTest.TestRelations();
		chromeTest.TestAboutPage();
		chromeTest.TestHelpPage();
		chromeTest.TestContactPage();
	}
	*/
	
	private static void runFireFoxTests() throws InterruptedException {
		WebDriver driver = new FirefoxDriver();
				
		WebTests firefoxTest = new WebTests(driver, url);
		
		firefoxTest.TestEnterWebsite();
		firefoxTest.TestDatabaseMapping();
		firefoxTest.TestContactPage();
	}
}
