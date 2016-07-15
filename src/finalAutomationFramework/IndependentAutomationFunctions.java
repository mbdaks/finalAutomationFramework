package finalAutomationFramework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class IndependentAutomationFunctions {	
	
	//function to get the property value i.e., the id value of a specific control
	public static String returnLocatorValue(Data object,String pageName, String controlHead) throws IOException{
	
		String controlType="";
		if(controlHead=="TripType"|| controlHead=="TravelOption")
			controlType=CommonBasicFunctions.getValue(CommonBasicFunctions.extractColumnNum(controlHead,object.dataSheet,object.dataWorkBook),SeleniumActionFunctions.currentRowInDataExcel,object.dataSheet,object.dataWorkBook);
		else
			controlType=controlHead;
		
		System.out.println(controlType);
		int propertyValueRowNum=CommonBasicFunctions.extractRowNum(pageName, controlType, object.propertySheet, object.propertyWorkBook);
		int propertyValueColumnNum=CommonBasicFunctions.extractColumnNum("PropertyValue",object.propertySheet,object.propertyWorkBook);		

		String locatorValue=CommonBasicFunctions.getValue(propertyValueColumnNum,propertyValueRowNum,object.propertySheet,object.propertyWorkBook);
		return locatorValue ;
	}
	
	//function to get the value to be sent to the webPage
	public static String returnValueToBeSentToWebPage(Data object,int currentRowInDataExcel,String controlType) throws IOException{
		
		int column=CommonBasicFunctions.extractColumnNum(controlType, object.dataSheet,object.dataWorkBook);
		String valueToBeSentToWebpage=CommonBasicFunctions.getValue(column,currentRowInDataExcel,object.dataSheet,object.dataWorkBook);
		return valueToBeSentToWebpage;
	}
	
	//function to create the result sheet at run time
	public static  void createResultSheet(Data dataObject) throws IOException{
		dataObject.resultWorkBook=new XSSFWorkbook();
		int index = dataObject.resultWorkBook.getSheetIndex("Sheet0");
		if(index==-1){
			dataObject.resultSheet=dataObject.resultWorkBook.createSheet();
		 }
		 else{
			 dataObject.resultSheet=dataObject.resultWorkBook.getSheetAt(0);
		 }

		Row headerRow = dataObject.resultSheet.createRow(0);
		Cell cell;
		cell=headerRow.createCell(0);
		cell.setCellValue("TestCaseID");
		cell=headerRow.createCell(1);
		cell.setCellValue("Outcome");
		cell=headerRow.createCell(2);
		cell.setCellValue("Comments");
		cell=headerRow.createCell(3);
		cell.setCellValue("ScreenshotName");
		System.out.println("Cell Values Set");
		FileOutputStream out=new FileOutputStream(new File(dataObject.resultFile));
		dataObject.resultWorkBook.write(out);
		System.out.println("Required excel file finally Created");
	}
	
	//function to write results in the result sheet after test execution
	public static void writeInResultSheet(Data dataObject) throws IOException{
		
		 Set<String> keyset = dataObject.testresultdata.keySet();
		    int rownum = 1;
		    for (String key : keyset) {
		        Row row = dataObject.resultSheet.createRow(rownum++);
		        Object [] objArr = dataObject.testresultdata.get(key);
		        int cellnum = 0;
		        for (Object obj : objArr) {
		            Cell cell = row.createCell(cellnum++);
		            if(obj instanceof Date) 
		                cell.setCellValue((Date)obj);
		            else if(obj instanceof Boolean)
		                cell.setCellValue((Boolean)obj);
		            else if(obj instanceof String)
		                cell.setCellValue((String)obj);
		            else if(obj instanceof Double)
		                cell.setCellValue((Double)obj);
		            String value=cell.getStringCellValue();
		            System.out.println(value);
		        }
		    }
	        FileOutputStream outstream=new FileOutputStream(dataObject.resultFile);
	        dataObject.resultWorkBook.write(outstream);
	        System.out.println("Excel written successfully..");
	}
	
	//function to get screeenshots at a particular time
	public static String getScreenshot(Data dataObject) throws Exception{
        File scrFile = ((TakesScreenshot)dataObject.driver).getScreenshotAs(OutputType.FILE);
     //The below method will save the screen shot in d drive with name "screenshot.png"
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String screenShotName=dataObject.screenShotBase+timeStamp+".gif";
        FileUtils.copyFile(scrFile, new File(screenShotName));
        return screenShotName;
	}
	
	//function to send the result sheet as an attachment in the email
	public static void sendEmail(Data dataObject) throws AddressException, MessagingException{
		
		dataObject.subject=dataObject.subject+dataObject.resultFile;
		
		Properties properties = new Properties();
	    properties.put("mail.smtp.host", dataObject.host);
	    properties.put("mail.smtp.port", dataObject.port);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.user", dataObject.mailFrom);
	    properties.put("mail.password", dataObject.password);
	
	    // creates a new session with an authenticator
	    Authenticator auth = new Authenticator() {
	        public PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(dataObject.mailFrom, dataObject.password);
	        }
	    };
	    Session session = Session.getInstance(properties, auth);
	
	    // creates a new e-mail message
	    Message msg = new MimeMessage(session);
	
	    msg.setFrom(new InternetAddress(dataObject.mailFrom));
	    InternetAddress[] toAddresses = { new InternetAddress(dataObject.mailTo) };
	    msg.setRecipients(Message.RecipientType.TO, toAddresses);
	    msg.setSubject(dataObject.subject);
	    msg.setSentDate(new Date());
	
	    // creates message part*
	    MimeBodyPart part1 = new MimeBodyPart();
	    part1.setContent(dataObject.message, "text/html");
	
	    MimeBodyPart part2 = new MimeBodyPart();
	    
	    // attach the file to the message
	     FileDataSource fds = new FileDataSource(dataObject.resultFile);
	     part2.setDataHandler(new DataHandler(fds));
	     part2.setFileName(fds.getName());
	
			// creates multi-part
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(part1);  
	    multipart.addBodyPart(part2);
	   
	    // sets the multi-part as e-mail's content
	    msg.setContent(multipart);
	    msg.setSentDate(new Date());
	    // sends the e-mail
	    Transport.send(msg);

	}

}