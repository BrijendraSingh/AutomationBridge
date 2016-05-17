/**
 * 
 */
package pkg_FrameworkDriver;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/** framework_ExcelSupportMethods
 * ----------------------------------------------------------------------------------------------------
 * @author: Brijendra Singh
 * @Date  : May 03, 2016 
 * @Discription: framework_ExcelSupportMethods, manages the excel operations such as retrieving the test
 * 				 cases its keywords and getTest data and setTestData
 * -----------------------------------------------------------------------------------------------------
 */
public class framework_ExcelSupportMethods {
	static Row td_row, first_row;
	
	/** setRow
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: setRow, set the row for currently executed test case row of the RunManager and TestData
	 * 				 Sheet 
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void setRow(Row rtd_row, Row rfirst_row) {
		// TODO Auto-generated constructor stub
		td_row=rtd_row;
		first_row=rfirst_row;
	}
	
	/** set_TestData, String TestData
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: set_TestData, Take the runtime testData from the script and set it to the TestData sheet
	 * 				 under provided testData Parameter as a future step verification
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void set_TestData(String key , String testData){
		int used_col = first_row.getLastCellNum()-first_row.getFirstCellNum();
		for ( int fr_cc=2; fr_cc<used_col;fr_cc++){
			if (first_row.getCell(fr_cc).toString().equalsIgnoreCase(key) ){
				td_row.createCell(fr_cc).setCellValue(testData.toString());
				framework_ReportClass.logINFO("Set_TestData: DataParameter- ["+key+"]", " is set to- ["+testData + "]");
				//System.out.println("Set row value for Data Parameter - ("+key+ ") is set to: " + td_row.getCell(fr_cc));
				break;
			}
		}
	}
	
	/** set_TestData, Integer TestData
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: set_TestData, Take the runtime testData from the script and set it to the TestData sheet
	 * 				 under provided testData Parameter as a future step verification
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void set_TestData(String key, int testData){
		int used_col = first_row.getLastCellNum()-first_row.getFirstCellNum();
		for ( int fr_cc=2; fr_cc<used_col;fr_cc++){
			if (first_row.getCell(fr_cc).toString().equalsIgnoreCase(key) ){
				td_row.createCell(fr_cc).setCellValue(testData);
				break;
			}
		}
	}
	
	/** get_TestData
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: get_TestData, Take the testData from the testData sheet for corresponding parameter
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static String get_TestData(String key){
		String keyVal="";
		String commonDataIdentifier, CommonDataSheetName;
				
		int used_col = first_row.getLastCellNum()-first_row.getFirstCellNum();
		for ( int fr_cc=2; fr_cc<used_col;fr_cc++){
			if (first_row.getCell(fr_cc).toString().equalsIgnoreCase(key) ){
				keyVal=td_row.getCell(fr_cc).getStringCellValue();
				break;
			}
		}	
		
		commonDataIdentifier=framework_logFileOperations.config.getProperty("CommonDataIdentifier");
		CommonDataSheetName=framework_logFileOperations.config.getProperty("CommonnDataSheet_Name");
		if (keyVal.indexOf(commonDataIdentifier)==0){
			Sheet CDSheet = framework_DriverClass.TestData.getSheet(CommonDataSheetName);
			String CD_name = keyVal.substring(1).toString();
			
			int CD_usedRow, CD_usedCol;
			CD_usedRow=CDSheet.getLastRowNum()-CDSheet.getFirstRowNum();
			CD_usedCol=CDSheet.getRow(0).getLastCellNum()-CDSheet.getRow(0).getFirstCellNum();
			for(int loopR=1;loopR<CD_usedRow;loopR++){
				Row CD_row = CDSheet.getRow(loopR);
				if (CD_row.getCell(0).toString().equalsIgnoreCase(CD_name)){
					//find out the coulumn
					for (int loopC=1;loopC<CD_usedCol;loopC++){
						if (CDSheet.getRow(0).getCell(loopC).toString().equalsIgnoreCase(key)){
							keyVal=CDSheet.getRow(loopR).getCell(loopC).toString();
							System.out.println(key + " is(CommonData) " + keyVal);
							break;
						}
					}
				}
			}
		}
		
		return keyVal;		
	}
	
}
