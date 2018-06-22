package selenium.tests;

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

public class SlackTestUseCase1 {

	private static WebDriver driver;
	private static final String cardName = "Handle click icon over popup";
	private static final String itemName = "enable touchscreen mode";
	private static final String itemNotPresent ="Enable using gesture recognition";
	private static final String cardisNotPresent ="Updated Website";
	public static boolean flag = false;
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

	
    @Test
	public void useCase1HappyPath() throws InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);

		// Test to add a checklist item in a card
		
		Actions actions = new Actions(driver);
		
		// List all items
		startConvo(actions);
		listItems(actions);
			
		// Add a todo item
		startConvo(actions);
		addChecklistItem(actions);

		
		//Mark a todo item
		startConvo(actions);
		markChecklistItem(actions);

		//Remove a todo item
		startConvo(actions);
		removeChecklistItem(actions);
		
		
		
		
	}
    
    
    @Test
    public void useCase1AlternativePath() throws InterruptedException
	{
		
    	
    	Actions actions = new Actions(driver);
    	// Case when the user-specified card is not present in the story board
    	cardNotPresent(actions);

		//Case when the checklist item (identified by the item name specified by user) to be marked is not present in the card
		startConvo(actions);
		markChecklistItemAlternate(actions);

		//Case when the checklist item (identified by the item name specified by user) to be removed is not present in the card
		startConvo(actions);
		removeChecklistItemAlternate(actions);
		
	
		
	}
    
    public static void startConvo(Actions actions) throws InterruptedException{
    	WebElement messageBot = driver.findElement(By.id("msg_input"));
		WebDriverWait wait = new WebDriverWait(driver, 30);
		assertNotNull(messageBot);
    	actions.moveToElement(messageBot);
		actions.click();
		actions.sendKeys("@ProManBot hi");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back with a greeting
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Good to see you.']")));
		if(flag == false) {
			WebElement msg = driver.findElement(By.xpath("//span/button[@title='Open a card']"));
			assertNotNull(msg);
			msg.click();
			flag = true;
		}else {
			actions.sendKeys("open a card");
			actions.sendKeys(Keys.RETURN);
			actions.build().perform();
		}
		
		
		Thread.sleep(5000);
		
		//ProManBot asks the name of the card which needs to be opened
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'What is the card name?']")));

		actions.sendKeys(cardName);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(5000);

		WebElement msg3 = driver.findElement(By.xpath("//span/div[@class='attachment_pretext for_attachment_group' and text() = 'I found that card']"));
		assertNotNull(msg3);
    }
    
    //Selenium Test function to list all checklist items of a card
    public static void listItems(Actions actions) throws InterruptedException
	{
    	WebDriverWait wait = new WebDriverWait(driver, 30);
    	WebElement msg = driver.findElement(By.xpath("//span/button[@title='List checklist items']"));
		assertNotNull(msg);
		msg.click();
		Thread.sleep(6000);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'capture mouse click action | incomplete | 17106']")));

		WebElement msg1 = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'capture mouse click action | incomplete | 17106']"));

		assertNotNull(msg1);
	}
    
    //Selenium Test function to add a new checklist item in a card	
    public static void addChecklistItem(Actions actions) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        
       actions.sendKeys("Add todo item");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot asks about the name of the new checklist item which needs to be added in the card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Enter the name of the checklist item you want to add:']")));
		
		actions.sendKeys(itemName);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back stating that it has added the checklist item 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'I have added the checklist item "+itemName+"']")));
		

		
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'I have added the checklist item "+itemName+"']"));

		assertNotNull(msg);
    }
    
	//Selenium Test function to mark a checklist item of a card	
    public static void markChecklistItem(Actions actions) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
    	
        actions.sendKeys("Mark a todo item");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot asks about the name of the checklist item which needs to be marked in a card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Enter the name of the checklist item you want to mark:']")));
		
		actions.sendKeys(itemName);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back stating that it has marked the checklist item
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'I have marked the checklist item "+itemName+"']")));
        
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'I have marked the checklist item "+itemName+"']"));
		assertNotNull(msg);
    }


	//Selenium Test function to remove a checklist item from a card	
    public static void removeChecklistItem(Actions actions) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
    	
        actions.sendKeys("remove a todo item");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot asks about the name of the checklist item which needs to be removed from a card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Enter the name of the checklist item you want to delete:']")));
		
		actions.sendKeys(itemName);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back stating that it has removed the checklist item 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'I have deleted the checklist item "+itemName+"']")));
        
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'I have deleted the checklist item "+itemName+"']"));
		assertNotNull(msg);
    }



    //Selenium test function when card is not present in the storyboard
    public static void cardNotPresent(Actions actions) throws InterruptedException {
    	
    	
    	WebElement messageBot = driver.findElement(By.id("msg_input"));
		WebDriverWait wait = new WebDriverWait(driver, 30);
		assertNotNull(messageBot);
    	actions.moveToElement(messageBot);
		actions.click();
		actions.sendKeys("@ProManBot hi");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back with a greeting
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Good to see you.']")));
		
		actions.sendKeys("open a card");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(3000);
		
		//ProManBot asks the name of the card which needs to be opened
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'What is the card name?']")));
    	actions.sendKeys(cardisNotPresent);
    	actions.sendKeys(Keys.RETURN);
    	actions.build().perform();
    	Thread.sleep(4000);

    	//ProManBot responds back stating that the card is not present in the current storyboard
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = \"I couldn't find the card name \'"+cardisNotPresent+"\' in your storyboard\"]")));
    	wait.withTimeout(10, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
    	
    	WebElement msg1 = driver.findElement(
			By.xpath("//span[@class='message_body' and text() = \"I couldn't find the card name \'"+cardisNotPresent+"\' in your storyboard\"]"));
    	assertNotNull(msg1);
    
    }


	//Selenium Test function to mark a checklist item of a card(Alternate Path)	
    public static void markChecklistItemAlternate(Actions actions) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
    	
        actions.sendKeys("Mark a todo item");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot asks about the name of the checklist item which needs to be marked in a card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Enter the name of the checklist item you want to mark:']")));
		
		actions.sendKeys(itemNotPresent);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back stating that it cannot mark the user-specified checklist item as it is not present in the card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Item "+itemNotPresent+" is not present. Verify that you have entered the correct item name and also verify that the checklist item is present in the specified card.']")));
        
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Item "+itemNotPresent+" is not present. Verify that you have entered the correct item name and also verify that the checklist item is present in the specified card.']"));
		assertNotNull(msg);
    }


//Selenium Test function to remove a checklist item from a card(Alternate Path)
    public static void removeChecklistItemAlternate(Actions actions) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
    	
        actions.sendKeys("remove a todo item");
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot asks about the name of the checklist item which needs to be removed from a card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Enter the name of the checklist item you want to delete:']")));
		
		actions.sendKeys(itemNotPresent);
		actions.sendKeys(Keys.RETURN);
		actions.build().perform();
		Thread.sleep(4000);
		
		//ProManBot responds back stating that it cannot remove the user-specified checklist item as it is not present in the card
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='message_body' and text() = 'Item "+itemNotPresent+" is not present. Verify that you have entered the correct item name and also verify that the checklist item is present in the specified card.']")));
        
		WebElement msg = driver.findElement(
				By.xpath("//span[@class='message_body' and text() = 'Item "+itemNotPresent+" is not present. Verify that you have entered the correct item name and also verify that the checklist item is present in the specified card.']"));
		assertNotNull(msg);
    }


// Utility method to open the given webpage only once
	public static void UtilityFunction(){
		driver.get("https://seproject-workspace.slack.com/");
		
        // Wait until page loads and we can see a sign in button.
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signin_btn")));

		// Find email and password fields for Slack login page
		WebElement email = driver.findElement(By.id("email"));
		WebElement pw = driver.findElement(By.id("password"));

		//type in slack's email address and password on the slack login page
		email.sendKeys(System.getenv("SLACK_EMAIL_ID"));
		pw.sendKeys(System.getenv("SLACK_PASSWORD"));

		// Click
		WebElement signin = driver.findElement(By.id("signin_btn"));
		signin.click();

		// Wait until we go to general channel.
		wait.until(ExpectedConditions.titleContains("general"));

		// Switch to #bots channel and wait for it to load.
		driver.get("https://seproject-workspace.slack.com" + "/messages/bots");
		wait.until(ExpectedConditions.titleContains("bots"));
	}

}

