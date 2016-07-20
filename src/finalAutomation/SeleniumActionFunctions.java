package finalAutomation;

import java.io.IOException;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
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
	public void selectAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		String sendValue = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		Select adult = new Select(dataObject.driver.findElement(By.id(locatorValue)));
		adult.selectByVisibleText(sendValue);	
	}

	/**
	 * Method to send the values to a text box.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @throws IOException 
	 */
	public void sendKeysAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		String sendValue = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).clear();
		dataObject.driver.findElement(By.id(locatorValue)).sendKeys(sendValue);
		dataObject.kb.pressKey(Keys.RETURN);
	}

	/**
	 * Method to perform a click action on a button.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @throws IOException 
	 */
	public void clickAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).click();
	}
}
