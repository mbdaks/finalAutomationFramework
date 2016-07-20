package finalAutomation;

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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import testCases.WebsiteSpecificData;

/**
 * This class contains supporting methods that perform tasks like<br>
 * -Reading and writing (Excel and POI related)<br>
 * -Sending email<br>
 * -Taking screenshots<br>
 * @author Mehak Bains
 */
public class SupportingFunctions {
	/** 
	 * @param filePath Variable holding the name of the excel file in consideration.
	 * @return Workbook for that specific file path
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Workbook returnWorkBook(String filePath) throws IOException, InvalidFormatException{
		File file = new File(filePath);
		OPCPackage opcPackage = OPCPackage.open(file);
 		Workbook workbook = new XSSFWorkbook(opcPackage);
		return workbook;
	}
	
	/**
	 * @param book Workbook in consideration.
	 * @return Sheet - Worksheet for that specific workbook
	 */
	public static Sheet returnSheet(Workbook book){
		Sheet sheet = book.getSheetAt(0);
		return sheet;
	}
	
	/**
	 * Required for appropriate formatting of the cell values 
	 * read from an excel sheet as Strings.
	 * @return DataFormatter
	 */
	public static DataFormatter getDataFormat(){
		DataFormatter formatting = new DataFormatter();
		return formatting;
	}
	
	/**
	 * Required for appropriate formatting of the cell values 
	 * read from an excel sheet as Strings.
	 * @param book Workbook in consideration
	 * @return Formula Evaluator
	 * @throws IOException
	 */
	public static FormulaEvaluator getFormulaEval(Workbook book) throws IOException{
		FormulaEvaluator foreval = new XSSFFormulaEvaluator((XSSFWorkbook) book);
		return foreval;
	}
	
	/**
	 * Reads the excel sheet one row at a time to get the 
	 * column number corresponding to the input head colValue.
	 * @param colValue Holds the string value for which we need the column number
	 * @param sheet Sheet in consideration
	 * @param book Workbook in consideration
	 * @return int - Column number corresponding to the input head colValue
	 * @throws IOException
	 */
	public static int extractColumnNum(String colValue, Sheet sheet, Workbook book) throws IOException{
		Row row = null;
		Cell cell = null;
		int colNum = 0;
		int firstRow = 0;
		row = sheet.getRow(firstRow);
		for(int icolumn = row.getFirstCellNum(); icolumn < row.getLastCellNum(); icolumn++){
			cell = row.getCell(icolumn);
			getFormulaEval(book).evaluate(cell);
			String cellValue = getDataFormat().formatCellValue(cell,getFormulaEval(book));
			if(cellValue.equals(colValue)){ 
				colNum = icolumn;	
				return colNum;
			}
		}
		book.close();
		return 0;
	}
	
	/**
	 * Reads an excel sheet one row at a time and contains nested if loops
	 * to decide the row number on the basis of two values.
	 * @param rowValue Holds the string value for which we need the row number
	 * @param rowDecider Holds the string value for which decides the number
	 * @param sheet String in consideration
	 * @param book Workbook in consideration
	 * @return int - Row number for a particular set of values
	 * @throws IOException
	 */
	public static int extractRowNum(String rowValue, String rowDecider, Sheet sheet, Workbook book) throws IOException{
		String testData = "", colValue = "Page", colDecider = "Control";
		//iterate over all the rows to get those containing the given pageName
		for(int irow = sheet.getFirstRowNum(); irow <= sheet.getLastRowNum(); irow++){
			int col = extractColumnNum(colValue, sheet, book);
			testData = getValue(col, irow, sheet, book);
			if (testData.equals(rowValue)){
				int col2 = extractColumnNum(colDecider, sheet, book);
				int currentRowNum = irow;
				testData = getValue(col2, currentRowNum, sheet, book);			
				if (testData.equals(rowDecider))
					return currentRowNum;					
			}
		}
		return 0;
	}
	
	/**
	 * Get the string value in a particular cell in the sheet at given
	 * row and column numbers.
	 * @param colNum Column number for the cell in consideration
	 * @param rowNum Row number for the cell in consideration
	 * @param sheet Sheet
	 * @param book Workbook
	 * @return String - CellValue in a particular cell in the sheet
	 * @throws IOException
	 */
	public static String getValue(int colNum, int rowNum, Sheet sheet, Workbook book) throws IOException{
			Row row = sheet.getRow(rowNum);
			Cell cell = row.getCell(colNum);
			getFormulaEval(book).evaluate(cell);
			String cellValue = getDataFormat().formatCellValue(cell, getFormulaEval(book));			
			return cellValue;
	}
	
	/**
	 * Creates a new excel sheet and provides name to the sheet on the run with a time stamp.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @throws IOException
	 */
	public static void createNewSheet(WebsiteSpecificData dataObject) throws IOException{
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		dataObject.resultFile += timeStamp + ".xlsx";
		dataObject.resultWorkBook = new XSSFWorkbook();
		int index = dataObject.resultWorkBook.getSheetIndex("Sheet0");
		if(index == -1)
			dataObject.resultSheet = dataObject.resultWorkBook.createSheet();
		 else
			 dataObject.resultSheet = dataObject.resultWorkBook.getSheetAt(0);
		Row headerRow = dataObject.resultSheet.createRow(0);
		Cell cell;
		cell = headerRow.createCell(0);
		cell.setCellValue("TestCaseID");
		cell = headerRow.createCell(1);
		cell.setCellValue("Outcome");
		cell = headerRow.createCell(2);
		cell.setCellValue("Comments");
		cell = headerRow.createCell(3);
		cell.setCellValue("ScreenshotName");
		FileOutputStream out = new FileOutputStream(new File(dataObject.resultFile));
		dataObject.resultWorkBook.write(out);
		System.out.println("Required excel file finally Created");
		}
		
	/**
	 * Writes test results into the result sheet that was created at run time.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @throws IOException
	 */
	public static void writeInResultSheet(WebsiteSpecificData dataObject) throws IOException{
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
		        }
		    }
	        FileOutputStream outstream = new FileOutputStream(dataObject.resultFile);
	        dataObject.resultWorkBook.write(outstream);
	        System.out.println("Excel written successfully!!!");
	}
	
	/**
	 * Takes a screenshot at any given point of time in gif format 
	 * and provides it a name defined by the time stamp.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @return String - Screenshot's name
	 * @throws Exception
	 */
	public static String getScreenshot(WebsiteSpecificData dataObject) throws Exception{
        File scrFile = ((TakesScreenshot)dataObject.driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String screenShotName = dataObject.screenShotBase + timeStamp + ".gif";
        FileUtils.copyFile(scrFile, new File(screenShotName));
        return screenShotName;
	}
	
	/**
	 * Sends the result sheet as an attachment in an email
	 * when the sender and receiver's email id are provided.
	 * @param dataObject Object of class WebsiteSpecificData used to access configurable parameters
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendEmail(WebsiteSpecificData dataObject) throws AddressException, MessagingException{
		dataObject.subject = dataObject.subject + dataObject.resultFile;
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
	    // creates message part
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