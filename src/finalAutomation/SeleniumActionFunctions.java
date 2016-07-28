package finalAutomation;

import java.io.IOException;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testCases.WebsiteSpecificData;
/**
 * This class contains functions to perform all the Selenium actions.
 * These actions include opening a URL in a browser, clicking elements, 
 * selecting an element, and sending values to textboxes.
 * @author Mehak Bains
 */
public class SeleniumActionFunctions {
	/**
	 * Variable to hold the current test case data number.
	 */
	public static int currentRowInDataExcel = 0;
	
	/**
	 * Method to open the website in the browser and then maximizes the browser.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 */
	public void openWebsite(WebsiteSpecificData dataObject){
		long startTime = Calendar.getInstance().getTimeInMillis();
		dataObject.driver.manage().deleteAllCookies();
		dataObject.driver.get(dataObject.baseUrl);
		dataObject.driver.manage().window().maximize();
		System.out.println("TIME TAKEN: " + (Calendar.getInstance().getTimeInMillis() - startTime));
	}
	
	/**
	 * Method to perform a select action from a dropdown.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @throws IOException 
	 */
	public void selectAction(WebsiteSpecificData dataObject, String locatorType, String pageName, String controlHead) throws IOException{
		String sendValue = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		Select adult = new Select(chooseLocator(dataObject, locatorType, pageName, controlHead));
		adult.selectByVisibleText(sendValue);	
		System.out.println(controlHead);
	}

	/**
	 * Method to send the values to a text box.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @throws IOException 
	 */
	public void sendKeysAction(WebsiteSpecificData dataObject, String locatorType, String pageName, String controlHead) throws IOException{
		String sendValue = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		chooseLocator(dataObject, locatorType, pageName, controlHead).clear();
		chooseLocator(dataObject, locatorType, pageName, controlHead).sendKeys(sendValue);
		dataObject.kb.pressKey(Keys.RETURN);
	}
	/**
	 * 
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param locatorType Variable to hold the type of locator used to identify the web element(id, cssLocator, xpath etc)
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @return WebElement
	 * @throws IOException
	 */
	public WebElement chooseLocator(WebsiteSpecificData dataObject, String locatorType, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		System.out.println(locatorValue);
		WebElement element;
		switch(locatorType){
		case "id":
			element= dataObject.driver.findElement(By.id(locatorValue));
			return element;
		case "cssSelector":
			element=dataObject.driver.findElement(By.cssSelector(locatorValue));
			return element;
		case "xpath":
			element= dataObject.driver.findElement(By.xpath(locatorValue));
			return element;
		}
		return null;
	}
	/**
	 * Method to perform a click action on a button and provide synchronization(wait) 
	 * with particular element that acquires values on the basis of the value of another element.
	 * The click is performed even when the element is not in the visible screen portion.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @throws IOException 
	 */
	public void clickAction(WebsiteSpecificData dataObject, String locatorType, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		System.out.println(locatorValue);
		JavascriptExecutor je = (JavascriptExecutor) dataObject.driver;
		WebElement element;
		switch(locatorType){
		case "id":
			element= dataObject.driver.findElement(By.id(locatorValue));
			je.executeScript("arguments[0].click();", element);
			break;
		case "cssSelectoor":
			element = dataObject.driver.findElement(By.cssSelector(locatorValue));
			je.executeScript("arguments[0].click();", element);
			break;
		case "xpath":
			element= dataObject.driver.findElement(By.xpath(locatorValue));
			je.executeScript("arguments[0].click();", element);
			break;
		}
	}

	
	/**
	 * Method to provide synchronization. It waits for the page to load completely
	 * and hence avoids having to provide implicit wait conditions every time with different time.
	 * It has an upper limit to wait of 60 seconds.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 */
	public void waitForLoadSynchronization(WebsiteSpecificData dataObject) {
		WebDriverWait wait = new WebDriverWait(dataObject.driver, 60);
		wait.until((ExpectedCondition<Boolean>) wd ->
	            ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
	}
}
