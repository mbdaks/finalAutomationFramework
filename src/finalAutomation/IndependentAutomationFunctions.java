package finalAutomation;

import java.io.IOException;
import testCases.WebsiteSpecificData;

public class IndependentAutomationFunctions {	
	//function to get the property value i.e., the id value of a specific control
	public static String returnLocatorValue(WebsiteSpecificData object, String pageName, String controlHead) throws IOException{
		String controlType = "";
		if(controlHead == "TripType" || controlHead == "TravelOption")
			controlType = SupportingFunctions.getValue(SupportingFunctions.extractColumnNum(controlHead, object.dataSheet, object.dataWorkBook), SeleniumActionFunctions.currentRowInDataExcel, object.dataSheet, object.dataWorkBook);
		else
			controlType = controlHead;		
		int propertyValueRowNum = SupportingFunctions.extractRowNum(pageName, controlType, object.propertySheet, object.propertyWorkBook);
		int propertyValueColumnNum = SupportingFunctions.extractColumnNum("PropertyValue", object.propertySheet, object.propertyWorkBook);		
		String locatorValue = SupportingFunctions.getValue(propertyValueColumnNum, propertyValueRowNum, object.propertySheet, object.propertyWorkBook);
		return locatorValue;
	}
	
	//function to get the value to be sent to the webPage
	public static String returnValueToBeSentToWebPage(WebsiteSpecificData object, int currentRowInDataExcel, String controlType) throws IOException{		
		int column = SupportingFunctions.extractColumnNum(controlType, object.dataSheet, object.dataWorkBook);
		String valueToBeSentToWebpage = SupportingFunctions.getValue(column, currentRowInDataExcel, object.dataSheet, object.dataWorkBook);
		return valueToBeSentToWebpage;
	}
}