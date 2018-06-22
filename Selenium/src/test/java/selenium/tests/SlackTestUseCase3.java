package selenium.tests;

/*
* References : https://sqa.stackexchange.com/questions/13008/how-to-determine-whether-element-is-clickable-or-not
* https://www.guru99.com/xpath-selenium.html http://selenium-python.readthedocs.io/locating-elements.html
*/


import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public class SlackTestUseCase3 {

	private static WebDriver driver;
	
	@BeforeClass
	public static void setUp() throws Exception 
	{
		//driver = new HtmlUnitDriver();
		ChromeDriverManager.getInstance().setup();
		driver = new ChromeDriver();
		UtilityFunction();
	}
	
	@AfterClass
	public static void  tearDown() throws Exception
	{
		driver.close();
		driver.quit();
	}

	
	/*@Test
	public void googleExists() throws Exception
	{
		driver.get("http://www.google.com");
        assertEquals("Google", driver.getTitle());		
	}*/

	
	@Test
	public void useCase3HappyPath() throws InterruptedException
	{

		// Type something
		WebElement messageBot = driver.findElement(By.id("msg_input"));
		assertNotNull(messageBot);
			
		Actions actions = new Actions(driver);
		actions.moveToElement(messageBot);
		actions.click();
	
		actions.sendKeys("@ProManBot Hey");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(5 * 1000);
		//wait.withTimeout(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		//driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
		//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[@class='message_body' and text() = 'How can I help you?']"))));
		/*WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Good to see you.']"));*/
		WebElement msg = driver.findElement(By.xpath("//span/button[@title='Create weekly summary']"));
		assertNotNull(msg);
		//wait.withTimeout(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		/*actions.sendKeys("Create weekly summary");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();*/
		msg.click();
		/*WebElement msg1 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Create weekly summary']"));
		assertNotNull(msg1);*/
		/*actions.build().perform();
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();*/
		Thread.sleep(5 * 1000);
		WebElement msg2 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Creating weekly summary from 11/13/2017 to 11/19/2017 , would you like to change dates?']"));
		assertNotNull(msg2);
		actions.sendKeys("no");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(10 * 1000);
		WebElement msg3 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Completed cards :']"));
		assertNotNull(msg3);
		Thread.sleep(2 * 1000);
	}
	
	@Test
	public void useCase3AlternativePath() throws InterruptedException
	{

		// Type something
		WebElement messageBot = driver.findElement(By.id("msg_input"));
		assertNotNull(messageBot);
			
		Actions actions = new Actions(driver);
		actions.moveToElement(messageBot);
		actions.click();
	
		actions.sendKeys("@ProManBot Hey");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(5 * 1000);
		//wait.withTimeout(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		//driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
		//wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[@class='message_body' and text() = 'How can I help you?']"))));
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Good to see you.']"));
		assertNotNull(msg);
		//wait.withTimeout(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		actions.sendKeys("Create weekly summary");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(5 * 1000);
		WebElement msg1 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Create weekly summary']"));
		assertNotNull(msg1);
		actions.build().perform();
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(5 * 1000);
		WebElement msg2 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Creating weekly summary from 11/13/2017 to 11/19/2017 , would you like to change dates?']"));
		assertNotNull(msg2);
		actions.sendKeys("Yes from 08/08/2016 to 08/15/2017");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(10 * 1000);
		WebElement msg3 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'No cards found for the given date range!']"));
		assertNotNull(msg3);
		Thread.sleep(2 * 1000);
	}
	
	// Utility method to open the given webpage only once
	public static void UtilityFunction(){
		driver.get("https://seproject-workspace.slack.com/");
		// Wait until page loads and we can see a sign in button.
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signin_btn")));

		// Find email and password fields.
		WebElement email = driver.findElement(By.id("email"));
		WebElement pw = driver.findElement(By.id("password"));

		// Enter our email and password
		// If running this from Eclipse, you should specify these variables in the run configurations.
		email.sendKeys(System.getenv("SLACK_EMAIL_ID"));
		pw.sendKeys(System.getenv("SLACK_PASSWORD"));

		// Click
		WebElement signin = driver.findElement(By.id("signin_btn"));
		signin.click();

		// Wait until we go to general channel.
		wait.until(ExpectedConditions.titleContains("general"));

		// Switch to #selenium-bot channel and wait for it to load.
		driver.get("https://seproject-workspace.slack.com" + "/messages/bots");
		wait.until(ExpectedConditions.titleContains("bots"));
	}

}
