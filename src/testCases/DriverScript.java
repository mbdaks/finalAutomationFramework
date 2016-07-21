package testCases;

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
import finalAutomation.IndependentAutomationFunctions;
import finalAutomation.SeleniumActionFunctions;
import finalAutomation.SupportingFunctions;

/**
 * <p>This is the class that contains the main test method which is to be run.
 * Various TestNG annotations are included to perform different tasks.
 * It includes all the Test data specific operations that must be performed.
 * We can have all the TestCases in this very class by using different
 * @Test annotation and assigning them different priorities
 * </p>
 * The flow of the script is such that:
 * 1. Configurable parameters are initialized and result sheet for the test is created.<br>
 * 2. Before every test, a particular URL is opened in the browser.<br>
 * 3. Test is then run which contains Selenium Actions.<br>
 * 4. For every test that is run, test results are recorded.<br>
 * 5. Driver is quit.<br>
 * 6. Test results are written into the result sheet for all the tests which is mailed to the required email id.<br>
 * It inherits methods from the class SeleniumActionFunctions.
 * @author Mehak Bains
 */
public class DriverScript extends SeleniumActionFunctions{
	/**
	 * An object of type WebsiteSpecificData to access all the configurable parameters.
	 */
	WebsiteSpecificData dataObject;
	/**
	 * Runs only once before all the tests 
	 * to initiate all parameters specific to the website in question
	 * by calling the constructor of class WebsiteSpecificData
	 * and to create an excelSheet to store test results.
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@BeforeSuite
	 public void createResultSheet() throws IOException, InvalidFormatException{
		dataObject = new WebsiteSpecificData();
		SupportingFunctions.createNewSheet(dataObject);
	}
	
	/**
	 * Runs every time before a test and opens the website.
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@BeforeMethod
	public void beforeScript() throws InvalidFormatException, IOException {
		openWebsite(dataObject);		
	}
	
	/**
	 * The main test method which includes various Selenium Actions to be performed
	 * and is run as many times as the invocationCount.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidFormatException
	 */
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
	
	/**
	 * Runs after every other test and records the test results in a LinkedHashMap.
	 * @param result The result(Success, Failure or Skipped) of the test executed
	 * @throws Exception
	 */
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
	
	/**
	 * Runs once after all the tests have been executed and quits from the driver.
	 */
	@AfterTest
	public void afterScript() {
		long startTime = Calendar.getInstance().getTimeInMillis();
		dataObject.driver.quit();
		System.out.println("TIME TAKEN: " + (Calendar.getInstance().getTimeInMillis() - startTime));
	}
	
	/**
	 * Writes recorded test results in the test result excel sheet
	 * and sends this excel sheet as an attachment in an email as required.
	 * @throws MessagingException
	 * @throws IOException
	 */
	@AfterSuite
	 public void setupAfterSuite() throws MessagingException, IOException {
		SupportingFunctions.writeInResultSheet(dataObject); 	
		SupportingFunctions.sendEmail(dataObject);
	}	
}
