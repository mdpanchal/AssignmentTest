package com.Utils;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AbstractComponent {
	
	WebDriver driver;

	public AbstractComponent(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	//#########################
		public void switchToChildWindow(String parentWindowHandle) {

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
		public void scrollInToElement(WebElement elt) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollTo(" + elt.getLocation().x + "," + (elt.getLocation().y - 100) + ");");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// #########################
		public void clickUsingJavascript(WebElement elt) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", elt);
			} catch (Exception e) {
			}
		}

		// #########################
		public void enterValueUsingJavascript(WebElement elt, String valueToEnter) {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = '';", elt);
			elt.sendKeys(valueToEnter);
		}

		// #########################
		public void explicitlyWait(WebElement elt) {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

			wait.until(ExpectedConditions.elementToBeClickable(elt));
		}

}
