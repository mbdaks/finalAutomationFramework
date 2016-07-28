package finalAutomation;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 * This class contains the function required to read 
 * the itinerary which is available in the PDF format
 * @author Mehak Bains
 */
public class PDFReadingFunction {
	/**
	 * Method to read and print the itinerary within particular limits 
	 * which are defined by two passed string parameters 
	 * @param filePath Path for the PDF file which is meant to be read
	 * @param startSearch String from which we need to read the PDF(lower bound)
	 * @param endSearch String before which we need to read the PDF(upper bound)
	 * @throws IOException
	 */
	public void readPDF(String filePath, String startSearch, String endSearch) throws IOException{
		PDDocument pdDoc = PDDocument.load(new File(filePath));
		PDFTextStripper pdfStripper = new PDFTextStripper();
        String parsedText = pdfStripper.getText(pdDoc);
        char[] array=parsedText.toCharArray();
        //specifically, in case of itinerary, we will have 
        //startSearch="Charges";
        //endSearch="Payments";
        int startIndex=parsedText.indexOf(startSearch);
        int endIndex=parsedText.indexOf(endSearch,startIndex);
        int i=0;
        while(i<startIndex){
        	i++;
        } 
        while(i<endIndex){
        	System.out.print(array[i]); 
        	i++;
        }
	}
}
