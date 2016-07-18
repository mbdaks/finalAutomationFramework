package finalAutomationFramework;

import java.io.IOException;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

public class SeleniumActionFunctions {
	public static int currentRowInDataExcel=0;
	
	public static void openWebsite(WebsiteSpecificData dataObject){
		long startTime = Calendar.getInstance().getTimeInMillis();
		dataObject.driver.manage().deleteAllCookies();
		dataObject.driver.get(dataObject.baseUrl);
		dataObject.driver.manage().window().maximize();
		System.out.println("TIME TAKEN: " + (Calendar.getInstance().getTimeInMillis() - startTime));
	}
	
	public void selectAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		String sendValue = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		Select adult = new Select(dataObject.driver.findElement(By.id(locatorValue)));
		adult.selectByVisibleText(sendValue);	
	}

	public void sendKeysAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue=IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		String sendValue=IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).clear();
		dataObject.driver.findElement(By.id(locatorValue)).sendKeys(sendValue);
		dataObject.kb.pressKey(Keys.RETURN);
	}

	public void clickAction(WebsiteSpecificData dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue = IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).click();
	}
}
