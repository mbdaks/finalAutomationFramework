package finalAutomationFramework;

import java.io.IOException;
import java.util.Calendar;
import javax.mail.MessagingException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class DriverScript extends SeleniumActionFunctions{
	WebsiteSpecificData dataObject;
	
	@BeforeSuite
	 public void createResultSheet() throws IOException, InvalidFormatException{
		dataObject=new WebsiteSpecificData();
		SupportingFunctions.createNewSheet(dataObject);
	}
	
	@BeforeMethod
	public void beforeScript() throws InvalidFormatException, IOException {
		openWebsite(dataObject);		
	}
	
	@Test(invocationCount = 10)
	public void main() throws IOException, InterruptedException, InvalidFormatException{
		long startTime = Calendar.getInstance().getTimeInMillis();
		String pageName = "SearchPage";
		currentRowInDataExcel++;
		clickAction(dataObject, pageName, "TripType");
		sendKeysAction(dataObject, pageName, "Source");
		sendKeysAction(dataObject, pageName, "Destination");
		sendKeysAction(dataObject, pageName, "DepartDate");
		if(dataObject.driver.findElement(By.id(IndependentAutomationFunctions.returnLocatorValue(dataObject, pageName, "ReturnDate"))).isEnabled()){
			sendKeysAction(dataObject, pageName, "ReturnDate");
		}
		selectAction(dataObject, pageName, "AdultPax");
		selectAction(dataObject, pageName, "ChildPax");
		selectAction(dataObject, pageName, "InfantPax");
		clickAction(dataObject, pageName, "TravelOption");
		clickAction(dataObject,pageName, "SearchButton");
		System.out.println("TIME TAKEN: " + (Calendar.getInstance().getTimeInMillis() - startTime));
	}
	
	@AfterMethod
	public void writeResults(ITestResult result) throws Exception{
		int test = result.getStatus();
		String key = String.valueOf(currentRowInDataExcel), screenShot = "";
		String testid = IndependentAutomationFunctions.returnValueToBeSentToWebPage(dataObject, currentRowInDataExcel, "TestCase");
		switch(test){
			case ITestResult.SUCCESS:
				System.out.println("YUREKA");
				dataObject.testresultdata.put(key, new Object[]{testid, "Passed", "No Comments", ""});
				break;
			case ITestResult.FAILURE:
				System.out.println("DISAPPOINTMENT");
				screenShot = SupportingFunctions.getScreenshot(dataObject);
				dataObject.testresultdata.put(key, new Object[]{testid, "Failed", "comment", screenShot});
				break;
			case ITestResult.SKIP:
				System.out.println("ALAS");
				dataObject.testresultdata.put(key, new Object[]{testid, "Skipped", "Comments", ""});
				break;
		}
	}
	
	@AfterTest
	public void afterScript() {
		long startTime = Calendar.getInstance().getTimeInMillis();
		dataObject.driver.quit();
		System.out.println("TIME TAKEN: " + (Calendar.getInstance().getTimeInMillis() - startTime));
	}
	
	@AfterSuite
	 public void setupAfterSuite() throws MessagingException, IOException {
		SupportingFunctions.writeInResultSheet(dataObject); 	
		SupportingFunctions.sendEmail(dataObject);
	}	
}
