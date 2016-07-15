package finalAutomationFramework;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import org.openqa.selenium.support.ui.Select;

public class SeleniumActionFunctions {
	
	public static int currentRowInDataExcel=1;

	public void selectAction(Data dataObject, String pageName, String controlHead) throws IOException{
		String locatorValue=IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName,controlHead);
		String sendValue=IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject,currentRowInDataExcel,controlHead);
		Select adult = new Select(dataObject.driver.findElement(By.id(locatorValue)));
		adult.selectByVisibleText(sendValue);
		System.out.println(locatorValue+"\t"+sendValue);
	
	}

	public void sendKeysAction(Data dataObject,String pageName,String controlHead) throws IOException{
		String locatorValue=IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName,controlHead);
		String sendValue=IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject,currentRowInDataExcel,controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).clear();
		dataObject.driver.findElement(By.id(locatorValue)).sendKeys(sendValue);
		dataObject.kb.pressKey(Keys.RETURN);

	}

	public void clickAction(Data dataObject,String pageName,String controlHead) throws IOException{
		String locatorValue=IndependentAutomationFunctions.returnLocatorValue(dataObject,pageName,controlHead);
		dataObject.driver.findElement(By.id(locatorValue)).click();

	}
	
}
