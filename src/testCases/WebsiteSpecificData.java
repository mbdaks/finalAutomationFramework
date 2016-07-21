package testCases;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.RemoteWebDriver;
import finalAutomation.SupportingFunctions;
/**
 * This class contains initializations for all the configurable parameters of the framework
 * which have been defined in the config.properties file and can be changed any time
 * as the requirements maybe.
 * @author Mehak Bains
 */
public class WebsiteSpecificData {
	/**
	 * URL for the website the automation framework is applied to
	 */
	public String baseUrl;
	/**
	 * Variable holding the name of the excel file containing the test data
	 */
	public String dataFile;
	/**
	 * Variable holding the name of the excel file containing the various control types and the property values
	 */
	public String propertyFile;
	/**
	 * Variable holding the name of the excel file containing the test results
	 */
	public String resultFile;
	/**
	 * Workbook for dataFile
	 */
	public Workbook dataWorkBook;
	/**
	 * Workbook for propertyFile
	 */
	public Workbook propertyWorkBook;
	/**
	 * Workbook for resultFile
	 */
	public Workbook resultWorkBook;
	/**
	 * Sheet for dataFile
	 */
	public Sheet dataSheet;
	/**
	 * Sheet for propertyFile
	 */
	public Sheet propertySheet;
	/**
	 * Sheet for resultFile
	 */
	public Sheet resultSheet;
	/**
	 * Variable holding the base name for every screenshot that will taken
	 * (defining factor will be the time stamp)
	 */
	public String screenShotBase;
	/**
	 * Variable holding parameter required in sending email
	 */
	public String host,port,mailFrom,password,mailTo,subject,message;
	/**
	 * LinkedHashMap for storing test results before they are written in the result sheet
	 */
	public HashMap<String, Object[]> testresultdata;
	/**
	 * Webdriver to open the browser and perform selenium related actions
	 */
	public WebDriver driver;
	/**
	 * Used to automate keyboard keys like enter and tab and backspace
	 */
	public Keyboard kb;
	
	/**
	 * Constructor to initialize some configurable parameters
	 * and loads and reads the config.properties file to assign values to the parameters.
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
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
		kb = ((RemoteWebDriver) driver).getKeyboard();
	
	}
}
