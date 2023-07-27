package com.Pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.Utils.AbstractComponent;

import dev.failsafe.internal.util.Assert;

public class AddToCartPage extends AbstractComponent {
	WebDriver driver;
	
	public AddToCartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//actions class 
	Actions act = new Actions(driver);
	
	//Locators

	By mobileSection = By.xpath("//div[text()='Mobiles']/parent::a[contains(href,'')]");
	By mobileCategoryFromLeftPane = By.xpath("//a[text()='Mobiles']");
	By productsListAvailableInPage = By.xpath("//a[@class='_1fQZEK']");
	By nameOfProductAddingToCart = By.xpath("//span[@class='B_NuCI']");
	By addToCartBtn = By.xpath("//button[text()='Add to cart']");
	By cartBasket = By.xpath("//span[text()='Cart']/parent::a");
	By productNamesAddedToCart = By.xpath("//a[contains(@class,'gBNbID')]");
	By noOfCartItems = By.xpath("//a[@class='_2hJxpa']/child::div[contains(text(),'Flipkart')]");

			//initialize list to store items we are adding to cart
			List<String> listOfItemsToAdd = new ArrayList<>();
			int noOfItemsToAdd = 2;
			
			//list to store products added in cart
			List<String> cartItems = new ArrayList<>();
		
	public void addItemToCart() {
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
	}
	
	public void validateItemsAddedToCart()  {
		//click on cart
				driver.findElement(cartBasket).click();
				//explicit wait
				explicitlyWait(driver.findElement(noOfCartItems));
				//we should avoid this after creating some reusable method to wait for page load
				//adding extra wait time as it takes lot of time to load/
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Validate cart Number
				int cartNumber = Integer.parseInt(driver.findElement(noOfCartItems).getText());
				Assert.isTrue(noOfItemsToAdd==cartNumber, "No of Items We wanted to add is not matchin with no of cart items");

				//elements of products added in cart
				List<WebElement> eltListCartProducts = driver.findElements(productNamesAddedToCart);
				
				for (WebElement elt : eltListCartProducts) {
					cartItems.add(elt.getText().trim());
				}
				//compare if products we tried to add and products added are same
				Assert.isTrue(listOfItemsToAdd.containsAll(cartItems), "Items to add and Cart Items are not matching");
	}

}
