package com.testPackage;
import org.testng.annotations.Test;

import com.Pages.AddToCartPage;


public class ValidationTest extends BaseTest{

	
	@Test
	public void addItemsToCartTest() {
		AddToCartPage addToCartPage =   loginPage.loginToApplication(); 
		addToCartPage.addItemToCart();
		addToCartPage.validateItemsAddedToCart();
	}
	
	
	
}
