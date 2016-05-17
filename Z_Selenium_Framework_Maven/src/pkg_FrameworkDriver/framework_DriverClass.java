/**
 * 
 */
package pkg_FrameworkDriver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import pkg_AUT_BusinessLibraries.aut_BusinessLibrariesClass;

/** framework_DriverClass
 * ----------------------------------------------------------------------------------------------------
 * @author: Brijendra Singh
 * @Date  : May 03, 2016 
 * @Discription: "main" method of the framework, setup the framework dependability and trigger the 
 * 				 execution
 * -----------------------------------------------------------------------------------------------------
 */
public class framework_DriverClass {
	
	//Object declaration 
	static WebDriver driver;
	static BufferedWriter log_file;
	static Workbook RunManager;
	static Workbook TestData;
	static Properties config;
	static aut_BusinessLibrariesClass B_lib=null;
	
	ExtentReports extent;
	ExtentTest test;
	static ExtentTest ChiledTest;
		
	/** main
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: "main" method of the framework, setup the framework dependability and trigger the execution
	 * -----------------------------------------------------------------------------------------------------
	 */
	public void init_FW() throws IOException{

		String RunManagerName,TestDataName,MainSheetName;
		
		//configuration file operation
		config=framework_logFileOperations.ConfiGFileSetup();
		System.out.println("Framework CONFIGURATION FILE loaded");
		//log file management
		log_file=framework_logFileOperations.createLogFile();
		framework_logFileOperations.logThis("Config & Log file", "Setup", "Completed");
		System.out.println("Framework LOG FILE loaded");
		//extent reporter
		framework_ReportClass.StartReporter();
		
		framework_logFileOperations.logThis("Config file setup", "", "Done");
		RunManagerName=config.getProperty("RunManagerFile");
		TestDataName=config.getProperty("TestDataFile");
		MainSheetName=config.getProperty("MainSheet");
		
		//RunManager and TestData file Objects		
		RunManager =  framework_ExcelFilesSetup.connectExcelFiles(RunManagerName+".xlsx");
		TestData = framework_ExcelFilesSetup.connectExcelFiles(TestDataName+".xlsx");
		
		//get Main sheet of run manager
		Sheet mainSheet = RunManager.getSheet(MainSheetName);
		framework_logFileOperations.logThis("Excel Files", "", "Connected");
		
		//-----read modules to execute form main sheet
		int MS_usedrow;    
		MS_usedrow = mainSheet.getLastRowNum()-mainSheet.getFirstRowNum();
		for (int MS_rc=1;MS_rc<MS_usedrow;MS_rc++){
			Row MS_row = mainSheet.getRow(MS_rc);
			if (MS_row.getCell(1).toString().equalsIgnoreCase("true")){
				Sheet module = RunManager.getSheet(MS_row.getCell(0).toString());
				framework_logFileOperations.logThis("Connected Module", module.getSheetName(), "In Execution");
				System.out.println("----- " + module.getSheetName()+ " -----");
				//------read test cases to execute from modules sheet
				int mod_usedrow;
				mod_usedrow=module.getLastRowNum()-module.getFirstRowNum();
				for (int mod_rc=1;mod_rc<mod_usedrow;mod_rc++){
					Row mod_row = module.getRow(mod_rc);
					if (mod_row.getCell(1).toString().equalsIgnoreCase("true")){
						framework_logFileOperations.logThis("Connected TestCaase", mod_row.getCell(0).toString(), "In Execution");
						System.out.println("TC@ - "+mod_row.getCell(0).toString());
						
						//----start reporter test and log TC details
						//@ Start Reporter
						framework_ReportClass.StartReporterTest(mod_row.getCell(0).toString(),mod_row.getCell(2).toString());
						framework_ReportClass.setMODULE(module.getSheetName());
						//framework_ReportClass.logTCName("Test Case Name: ", mod_row.getCell(0).toString());
						
						//----read keywords to execute
						int mod_usedcol = mod_row.getLastCellNum()-mod_row.getFirstCellNum();
						for (int mod_cc=3;mod_cc<mod_usedcol;mod_cc++){
							if (mod_row.getCell(mod_cc).toString().equalsIgnoreCase("")){
								break;
							}else{
								//----test Data row setup
								Row td_row = TestData.getSheet(MS_row.getCell(0).toString()).getRow(mod_rc);
								framework_ExcelSupportMethods.setRow(td_row,TestData.getSheet(MS_row.getCell(0).toString()).getRow(0));								

								//----launch the code
								String keyword = mod_row.getCell(mod_cc).toString();
								framework_logFileOperations.logThis("Keyword", keyword, "In Execution");
								System.out.println(keyword + " is Executing.." );
								
								//@@Child Test-Keyword
								ChiledTest=framework_ReportClass.StartChiled_ReporterTest(keyword);
								
								//keyword details in reporter
								//framework_ReportClass.logINFO_Keyword("[= IN : =] ", keyword);
								
								if (keyword.equalsIgnoreCase("LaunchBrowser")){
									driver=framework_BrowserSetup.LaunchBrowser();
									B_lib =  new aut_BusinessLibrariesClass(driver);
								}else{
									Method method;
									try {
										//launch keyword
										method = aut_BusinessLibrariesClass.class.getDeclaredMethod(keyword,null);
										method.invoke(B_lib, null);	
										if (framework_logFileOperations.config.getProperty("TerminateTC").equalsIgnoreCase("yes")){
											framework_ReportClass.logFATAL("Terminating This TC Execution", "FATAL ERROR");
											framework_logFileOperations.config.setProperty("TerminateTC", "NO");
											driver.close();
											driver.quit();
											break;
										}
									} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
										System.out.println("Problem execution/Invoking the Keyword " + keyword + ", Casue - " + e.getCause()+ " , Message: " + e.getStackTrace());
										framework_ReportClass.logUNKNOWN("Problem execution/Invoking the Keyword " + keyword , ", Casue - " + e.getCause() + " | Message- "+ e.getMessage());
										framework_logFileOperations.logThis("Problem execution/Invoking the Keyword", keyword, e.getMessage());
									}
								}
								//framework_ReportClass.logINFO_Keyword("[= OUT: =] ", keyword);
								framework_ReportClass.Append_ChildTest(ChiledTest);
								framework_logFileOperations.logThis("Keyword", keyword, "Execution OUT");
							}
						}
						framework_logFileOperations.logThis("Connected TestCase", mod_row.getCell(0).toString(), "Execution OUT");
						//framework_ReportClass.logTCName("TC Execution End: ", mod_row.getCell(0).toString());
						framework_ReportClass.endTest();
					}
				}
			}
		}		
		framework_ReportClass.flushReporter();
		framework_logFileOperations.logThis("Final Result", "TestReport", "OPEN UP");
		framework_ReportClass.OpenResult();
		log_file.close();
	}
}
