package finalAutomation;

import java.io.IOException;
import testCases.WebsiteSpecificData;

/** 
 * This class contains methods to extract PropertyValue from property sheet
 * and test data from the data sheet. It calls various methods from the class 
 * SupportingFunctions to perform these tasks.
 * @author Mehak Bains
 */
public class IndependentAutomationFunctions {	
	/**
	 * Method to get locator Value from the property sheet for a particular control type
	 * (It requires special handling for radio buttons).
	 * @param object Object of class WebsiteSpecificData used to access configurable parameters
	 * @param pageName Variable that holds the column name defining the webpage in the booking flow
	 * @param controlHead Variable that holds the column name defining the element on that webpage
	 * @return String - Locator Value from the property sheet for a particular control type
	 * @throws IOException
	 */
	public static String returnLocatorValue(WebsiteSpecificData object, String pageName, String controlHead) throws IOException{
		String controlType = "";
		//Special handling for radio buttons
		if(controlHead == "TripType" || controlHead == "TravelOption"){
			int currentColumnInDataExcel=SupportingFunctions.extractColumnNum(controlHead, object.dataSheet, object.dataWorkBook);
			controlType = SupportingFunctions.getValue(currentColumnInDataExcel, SeleniumActionFunctions.currentRowInDataExcel, object.dataSheet, object.dataWorkBook);
		}
		else
			controlType = controlHead;		
		int propertyValueRowNum = SupportingFunctions.extractRowNum(pageName, controlType, object.propertySheet, object.propertyWorkBook);
		int propertyValueColumnNum = SupportingFunctions.extractColumnNum("PropertyValue", object.propertySheet, object.propertyWorkBook);		
		String locatorValue = SupportingFunctions.getValue(propertyValueColumnNum, propertyValueRowNum, object.propertySheet, object.propertyWorkBook);
		return locatorValue;
	}
	
	/**
	 *  Method to get test data to be entered on the web page.
	 * @param object Object of class WebsiteSpecificData used to access configurable parameters
	 * @param currentRowInDataExcel Variable that holds the current test case data number
	 * @param controlType Variable that holds the column name defining the element on a webpage
	 * @return String - Test data to be entered on the web page 
	 * @throws IOException
	 */
	public static String returnValueToBeSentToWebPage(WebsiteSpecificData object, int currentRowInDataExcel, String controlType) throws IOException{		
		int column = SupportingFunctions.extractColumnNum(controlType, object.dataSheet, object.dataWorkBook);
		String valueToBeSentToWebpage = SupportingFunctions.getValue(column, currentRowInDataExcel, object.dataSheet, object.dataWorkBook);
		return valueToBeSentToWebpage;
	}
}