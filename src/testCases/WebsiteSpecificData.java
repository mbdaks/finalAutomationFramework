package testCases;

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
import finalAutomation.SupportingFunctions;

public class WebsiteSpecificData {
	public String baseUrl;
	public String propertyFile,dataFile;
	public String resultFile;
	public int testDataNum;
	public Workbook dataWorkBook,propertyWorkBook;
	public Workbook resultWorkBook;
	public Sheet dataSheet,propertySheet,resultSheet;
	public String screenShotBase;
	public String host,port,mailFrom,password,mailTo,subject,message;
	public HashMap<String, Object[]> testresultdata;
	public WebDriver driver;
	public JavascriptExecutor jse;
	public Keyboard kb;

	WebsiteSpecificData() throws IOException, InvalidFormatException{
	    baseUrl = "http://uat.jetstar.com/SkySalesTest/Search.aspx";
		propertyFile = "C:\\Users\\HP\\flightBookings\\resources\\Input\\Properties.xlsx";
		dataFile = "C:\\Users\\HP\\flightBookings\\resources\\Input\\Data.xlsx";
		testDataNum = 0;
		screenShotBase = "C:\\Users\\HP\\flightBookings\\resources\\Output\\ScreenShots\\SS_";
		host = "smtp.gmail.com";
	    port = "25";
	    mailFrom = "ykwsnape321@gmail.com";
	    password = "DAWNDYNASTYDIARIES";
	    mailTo = "ykw132snape@gmail.com";
	    subject = "Execution Results : ";
	    message = "Attached : excel sheet containing result";
	    dataWorkBook = SupportingFunctions.returnWorkBook(dataFile);
		dataSheet = SupportingFunctions.returnSheet(dataWorkBook);
		propertyWorkBook = SupportingFunctions.returnWorkBook(propertyFile);
		propertySheet = SupportingFunctions.returnSheet(propertyWorkBook);
		testresultdata = new HashMap<String, Object[]>();
	    driver = new FirefoxDriver();
		jse = (JavascriptExecutor)driver;
		kb = ((RemoteWebDriver) driver).getKeyboard();
	
	}
}
