package finalAutomationFramework;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Data {
	String baseUrl;
	String propertyFile,dataFile,resultFile;
	int testDataNum;
	Workbook dataWorkBook,propertyWorkBook,resultWorkBook;
	Sheet dataSheet,propertySheet,resultSheet;
	String screenShotBase;
	String host,port,mailFrom,password,mailTo,subject,message;
	HashMap<String, Object[]> testresultdata;
	WebDriver driver;
	JavascriptExecutor jse;
	Keyboard kb;

	
	Data() throws IOException, InvalidFormatException
	{
		baseUrl = "http://uat.jetstar.com/SkySalesTest/Search.aspx";
		
		propertyFile="F:\\GIT\\finalAutomationFramework\\resources\\input\\Properties.xlsx";
		dataFile="F:\\GIT\\finalAutomationFramework\\resources\\input\\Data.xlsx";
		resultFile="F:\\GIT\\finalAutomationFramework\\resources\\output\\results\\ResultSheet_";
		
		testDataNum=0;
		
		dataWorkBook=CommonBasicFunctions.returnWorkBook(dataFile);
		dataSheet=CommonBasicFunctions.returnSheet(dataWorkBook);
		propertyWorkBook=CommonBasicFunctions.returnWorkBook(propertyFile);
		propertySheet=CommonBasicFunctions.returnSheet(propertyWorkBook);
		
		screenShotBase="F:\\GIT\\finalAutomationFramework\\resources\\output\\ScreenShots\\SS_";
		
		host = "smtp.gmail.com";
	    port = "25";
	    mailFrom = "ykwsnape321@gmail.com";
	    password = "DAWNDYNASTYDIARIES";
	    mailTo = "ykw132snape@gmail.com";
	    subject = "Execution Results : ";
	    message = "Attached : excel sheet containing result";
	    
	    testresultdata=new HashMap<String, Object[]>();
	    
	    driver = new FirefoxDriver();
		jse = (JavascriptExecutor)driver;
		kb = ((RemoteWebDriver) driver).getKeyboard();
	}

}
