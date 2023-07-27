package com.flipcart.selenium;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.failsafe.internal.util.Assert;

public class Test {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException, IOException {
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");
		driver = new ChromeDriver(options);
		// using chromeOptions to avoid ssl certificate error in my macbook
		// if you are using selenium version below 4.8xxx use system.setProperty
		// System.setProperty("webdriver.chrome.driver", "PathToChromeDriver");
		

		//maximize window
		driver.manage().window().maximize();
		
		//implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		
		// launch application/website
		driver.get("https://www.flipkart.com");
		
		//getTitle of the application
		Assert.isTrue(driver.getTitle().equals("Online Shopping Site for Mobiles, Electronics, Furniture, Grocery, Lifestyle, Books & More. Best Offers!"), "Title of the page does not match");

		
		//actions class 
		Actions act = new Actions(driver);
		
		//Locators
		By loginInputField = By.xpath("//span[text()='Enter Email/Mobile number']");
		By requestOTPBtn = By.xpath("//button[text()='Request OTP']");
		By loginBtn = By.xpath("//a[text()='Login']");
		//By verifyButton = By.xpath("//button[@type='submit' and text()='Verify']");
		By signInBtn = By.xpath("//span[text()='Sign in']");
		By mobileSection = By.xpath("//div[text()='Mobiles']/parent::a[contains(href,'')]");
		By mobileCategoryFromLeftPane = By.xpath("//a[text()='Mobiles']");
		By productsListAvailableInPage = By.xpath("//a[@class='_1fQZEK']");
		By nameOfProductAddingToCart = By.xpath("//span[@class='B_NuCI']");
		By addToCartBtn = By.xpath("//button[text()='Add to cart']");
		By cartBasket = By.xpath("//span[text()='Cart']/parent::a");
		By productNamesAddedToCart = By.xpath("//a[contains(@class,'gBNbID')]");
		By noOfCartItems = By.xpath("//a[@class='_2hJxpa']/child::div[contains(text(),'Flipkart')]");

		WebElement eltUserName=null;
		try {
			eltUserName = driver.findElement(loginInputField);
		} catch (Exception e) {
			act.click(driver.findElement(signInBtn)).build().perform();
			eltUserName = driver.findElement(loginInputField);
		}
		
		if (!eltUserName.isDisplayed()) {
				driver.findElement(loginBtn).click();
		}else {
			//logs
			System.out.println("Login user name is displayed");
		}
		
		//Click on Usrername and enter phoneNumber/email address
		act.click(eltUserName).sendKeys("9579176170").sendKeys(Keys.TAB).build().perform();
		
		
		//adding thread.slpeep to avoid human verification
		Thread.sleep(4000);
		
		WebElement eltRequestOTPBtn = driver.findElement(requestOTPBtn);
		clickUsingJavascript(eltRequestOTPBtn);
		//act.click(eltRequestOTPBtn).build().perform();

		//Adding Thread.sleep to enter OTP Manually
		//Thread.sleep(20000);
		explicitlyWait(driver.findElement(mobileSection));
		// click on Mobile Section
		driver.findElement(mobileSection).click();
		
		//wait for mobile in left pane
		explicitlyWait(driver.findElement(mobileCategoryFromLeftPane));
		//Click on Mobile from left pane to filter all mobiles
		driver.findElement(mobileCategoryFromLeftPane).click();

		//get Parentwindow handle
		String parentWindowHandle = driver.getWindowHandle();
		
		//get webelement list of all mobiles present
		List<WebElement> eltList = driver.findElements(productsListAvailableInPage);

		//initialize list to store items we are adding to cart
		List<String> listOfItemsToAdd = new ArrayList<>();
		
		int noOfItemsToAdd = 2;
		
		//loop to iterate and add items to cart
		for (int i = 0; i < noOfItemsToAdd; i++) {
			//if element is not visible scroll down till element
			if (!eltList.get(i).isDisplayed()) {
				scrollInToElement(eltList.get(i));
			}
			//click on product 
			eltList.get(i).click();
			//switch to child tab
			switchToChildWindow(parentWindowHandle);
			
			//explicit wait
			explicitlyWait(driver.findElement(nameOfProductAddingToCart));

			//get Name of the Item which we are adding to cart and store in List
			listOfItemsToAdd.add((driver.findElement(nameOfProductAddingToCart).getText().split("  ")[0].trim()));
			//click and add product to cart
			driver.findElement(addToCartBtn).click();
			//close child tab
			driver.close();
			// switch to parent window
			driver.switchTo().window(parentWindowHandle);
		}
		
		//click on cart
		driver.findElement(cartBasket).click();
	
		
		//explicit wait
		explicitlyWait(driver.findElement(noOfCartItems));
		//we should avoid this after creating some reusable method to wait for page load
		//adding extra wait time as it takes lot of time to load/
		Thread.sleep(10000);
		
		//Validate cart Number
		int cartNumber = Integer.parseInt(driver.findElement(noOfCartItems).getText());
		Assert.isTrue(noOfItemsToAdd==cartNumber, "No of Items We wanted to add is not matchin with no of cart items");
		
		
		//list to store products added in cart
		List<String> cartItems = new ArrayList<>();
		//elements of products added in cart
		List<WebElement> eltListCartProducts = driver.findElements(productNamesAddedToCart);
		
		for (WebElement elt : eltListCartProducts) {
			cartItems.add(elt.getText().trim());
		}
		
		//compare if products we tried to add and products added are same
		Assert.isTrue(listOfItemsToAdd.containsAll(cartItems), "Items to add and Cart Items are not matching");
		
		
		//close browser
		driver.close();
		

	}

	// #########################
	public static void switchToChildWindow(String parentWindowHandle) {

		try {
			Set<String> winHandles = driver.getWindowHandles();
			Iterator<String> it = winHandles.iterator();
			while (it.hasNext()) {
				String handle = it.next();
				if (!handle.equals(parentWindowHandle)) {
					driver.switchTo().window(handle);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// #########################
	public static void scrollInToElement(WebElement elt) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(" + elt.getLocation().x + "," + (elt.getLocation().y - 100) + ");");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// #########################
	public static void clickUsingJavascript(WebElement elt) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", elt);
		} catch (Exception e) {
		}
	}
	
	// #########################
	public static void enterValueUsingJavascript(WebElement elt, String valueToEnter) {
		
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = '';", elt);
			elt.sendKeys(valueToEnter);
	}
	
	// #########################
	public static void explicitlyWait(WebElement elt) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	
		wait.until(ExpectedConditions.elementToBeClickable(elt));
	}


}
