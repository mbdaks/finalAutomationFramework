package finalAutomationFramework;

import java.io.File;
import java.io.IOException;

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

public class CommonBasicFunctions {
	
	//get the workbook
	public static Workbook returnWorkBook(String filePath) throws IOException, InvalidFormatException{
		File file = new File(filePath);
		OPCPackage opcPackage = OPCPackage.open(file);
 		Workbook workbook=new XSSFWorkbook(opcPackage);
 		
		return workbook;
	}
	
	//get the sheet
	public static Sheet returnSheet(Workbook book){
		Sheet sheet=book.getSheetAt(0);
		
		return sheet;
	}
	
	//cell value evaluating supporting functions
	public static DataFormatter getDataFormat(){
		DataFormatter formatting=new DataFormatter();
		
		return formatting;
	}
	
	public static FormulaEvaluator getFormulaEval(Workbook book) throws IOException{
		FormulaEvaluator foreval=new XSSFFormulaEvaluator((XSSFWorkbook) book);
		
		return foreval;
	}
	
	//to get the correct column number for the given value
	public static int extractColumnNum(String colValue,Sheet sheet,Workbook book) throws IOException{
		Row row=null;
		Cell cell=null;
		int colNum=0;
		int firstRow=0;
		row=sheet.getRow(firstRow);
		for(int icolumn=row.getFirstCellNum();icolumn<row.getLastCellNum();icolumn++){
			cell=row.getCell(icolumn);
			getFormulaEval(book).evaluate(cell);
			String cellValue=getDataFormat().formatCellValue(cell,getFormulaEval(book));
			if(cellValue.equals(colValue)){ 
				colNum=icolumn;	
				
				return colNum;
			}
		}
		book.close();
		
		return 0;
	}
	
	//to get correct row number for a values
	public static int extractRowNum(String rowValue,String rowDecider, Sheet sheet, Workbook book) throws IOException{
		
		String testData="";
		//iterate over all the rows to get those containing the given pageName
		for(int irow=sheet.getFirstRowNum();irow<=sheet.getLastRowNum();irow++){
			int col=extractColumnNum("Page", sheet, book);
			testData=getValue(col,irow,sheet,book);
			if (testData.equals(rowValue)){
				int col2=extractColumnNum("Control", sheet, book);
				int currentRowNum=irow;
				testData=getValue(col2,currentRowNum,sheet,book);			
				if (testData.equals(rowDecider))
		        {
					return currentRowNum;
					
	            }
			}
		}
		return 0;
	}
	
	//given row number and column, evaluate and return the cell value when reading from an excel sheet
	public static String getValue(int columnHead,int rowNum,Sheet sheet,Workbook book) throws IOException{
			
			Row row=sheet.getRow(rowNum);
			Cell cell=row.getCell(columnHead);
			getFormulaEval(book).evaluate(cell);
			String cellValue=getDataFormat().formatCellValue(cell,getFormulaEval(book));
			
			return cellValue;
	}
}
