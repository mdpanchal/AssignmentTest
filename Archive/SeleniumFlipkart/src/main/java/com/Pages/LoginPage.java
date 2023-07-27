package com.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.Utils.AbstractComponent;

public class LoginPage extends AbstractComponent {
	protected WebDriver driver;

	public LoginPage(WebDriver driver) {
		super(driver);
		// initialization
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//Locators
	By loginInputField = By.xpath("//span[text()='Enter Email/Mobile number']");
	By requestOTPBtn = By.xpath("//button[text()='Request OTP']");
	By loginBtn = By.xpath("//a[text()='Login']");
	//By verifyButton = By.xpath("//button[@type='submit' and text()='Verify']");
	By signInBtn = By.xpath("//span[text()='Sign in']");
	
	
	
	public void launchURL()
	{
		driver.get("https://www.flipkart.com");
		System.out.println(driver.getTitle());
	}
	
	//actions class 
			Actions act = new Actions(driver);
		
	public AddToCartPage loginToApplication()  {
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

		//thread slepp to enter OTP manually
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WebElement eltRequestOTPBtn = driver.findElement(requestOTPBtn);
		clickUsingJavascript(eltRequestOTPBtn);
		
		AddToCartPage addToCartPage = new AddToCartPage(driver);
		
		return addToCartPage;
	}
	
}
