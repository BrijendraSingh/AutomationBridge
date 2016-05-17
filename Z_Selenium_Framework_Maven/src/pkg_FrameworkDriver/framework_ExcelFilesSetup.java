/**
 * 
 */
package pkg_FrameworkDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/** ----------------------------------------------------------------------------------------------------
 * @author: Brijendra Singh
 * @Date  : May 03, 2016 
 * @Discription: framework_ExcelFilesSetup, manages the RunManager and TestData Excel sheets 
 * -----------------------------------------------------------------------------------------------------
 */
public class framework_ExcelFilesSetup {
	
	static Workbook xlFile;
	
	/** connectExcelFiles(String fileName)
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: connectExcelFiles, create and connect with the excel objects
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static Workbook connectExcelFiles(String fileName){
		File xFile = new File(System.getProperty("user.dir")+"\\Files\\" +fileName);
		try {
			FileInputStream fis = new FileInputStream(xFile);
			xlFile = new XSSFWorkbook(fis);
			return xlFile;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("erroor 1");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("erroor 2");
			e.printStackTrace();
		}
		return xlFile;
	}
}
