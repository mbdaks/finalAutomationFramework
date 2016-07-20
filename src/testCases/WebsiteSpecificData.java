package testCases;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
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
	public String propertyFile,dataFile,resultFile;
	public int testDataNum;
	public Workbook dataWorkBook,propertyWorkBook,resultWorkBook;
	public Sheet dataSheet,propertySheet,resultSheet;
	public String screenShotBase;
	public String host,port,mailFrom,password,mailTo,subject,message;
	public HashMap<String, Object[]> testresultdata;
	public WebDriver driver;
	public JavascriptExecutor jse;
	public Keyboard kb;

	WebsiteSpecificData() throws IOException, InvalidFormatException{
	    
		Properties properties = new Properties();
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		InputStream propertiesStream = contextClassLoader.getResourceAsStream("config.properties");
		if (propertiesStream != null) {
		  properties.load(propertiesStream);
		} else {
		  System.out.println("Properties file not found!");
		}
		baseUrl = properties.getProperty("baseUrl");
		propertyFile = properties.getProperty("propertyFile");
		dataFile = properties.getProperty("dataFile");
		resultFile = properties.getProperty("resultFile");
		testDataNum = new Integer(properties.getProperty("testDataNum"));
		screenShotBase = properties.getProperty("screenShotBase");
		host = properties.getProperty("host");
	    port = properties.getProperty("port");
	    mailFrom = properties.getProperty("mailFrom");
	    password = properties.getProperty("password");
	    mailTo = properties.getProperty("mailTo");
	    subject = properties.getProperty("subject");
	    message = properties.getProperty("message");
		
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
