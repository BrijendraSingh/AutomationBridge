/**
 * 
 */
package pkg_FrameworkDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;

import pkg_AUT_BusinessLibraries.aut_BusinessLibrariesClass;

/** framework_ReportClass
 * ----------------------------------------------------------------------------------------------------
 * @author: Brijendra Singh
 * @Date  : May 03, 2016 
 * @Discription: framework_ReportClass, Create and Manages the Reporting of execution
 * -----------------------------------------------------------------------------------------------------
 */
public class framework_ReportClass {
	
	static ExtentReports extent;
	static ExtentTest test,ChildTest;
	static Date date;
	static String ResultFolderName;
	static WebDriver driver;
	static File scrFile;
	//static Properties config;

	
	/** StartReporter
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: StartReporter, initialize the reporter
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void StartReporter(){	
		//Result folder creation
		date= new Date();
		ResultFolderName=date.toString().replace(" ", "_").replace(":", "_");
		File file = new File(System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Screenshots");
        if (!file.exists()) {
            if (file.mkdirs()) {
                //System.out.println("Run_"+ResultFolderName+" :Directory is created!");
                framework_logFileOperations.logThis("Extent Reporter", "Initialization", "Done");
                //start extent report
                extent = new ExtentReports(System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Report.html", true, NetworkMode.OFFLINE);
                extent.loadConfig(new File(System.getProperty("user.dir")+"\\Files\\extent-config.xml"));
                //config= framework_logFileOperations.ConfiGFileSetup();
                //extent.addSystemInfo("Selenium Version", config.getProperty("Selenium Version"));
                //extent.addSystemInfo("Environment", config.getProperty("Environment"));
            } else {
                System.out.println("Run_"+ResultFolderName+" :Failed to create directory!");
            }
        }        
	}
	
	/** StartReporterTest
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: StartReporterTest, Start the reporter for individual Test Cases
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void StartReporterTest(String testName, String TestDescription){
		test = extent.startTest(testName, TestDescription);
		test.assignAuthor("Brijendra Singh");
		//test.setDescription(TestDescription);
	}
	
	/** StartChiled_ReporterTest
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: StartChiled_ReporterTest, Start the Child test for Keyword level
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static ExtentTest StartChiled_ReporterTest(String testName){
		ChildTest=null;
		return ChildTest = extent.startTest(testName);
	}
	
	/** Append_ChildTest
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: Append_ChildTest, Start the Child test for Keyword level
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void Append_ChildTest(ExtentTest ChildTest){
		test.appendChild(ChildTest);
	}
	
	
	/** flushReporter
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: flushReporter,flush down the reporter 
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void flushReporter(){
		extent.flush();
	}
	
	/** flushReporter
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: flushReporter,flush down the reporter 
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void endTest(){
		extent.endTest(test);
	}
	
	/** setMODULE
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: setMODULE,set the modules to a test
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void setMODULE(String ModuleName){
		test.assignCategory(ModuleName);
	}
	
	/**1. logPASS , step and details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logPASS, logs the passed steps to the Reporter with stepName and detail parameters
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logPASS(String stepName,String details){
		if(framework_logFileOperations.config.getProperty("ScreenShot_CapturePASS").toString().equalsIgnoreCase("true")){
			date = new Date();
			driver =aut_BusinessLibrariesClass.getdriver();
			scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String path = System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Screenshots\\"+date.toString().replace(" ", "_").replace(":", "_")+".png";
			// Now you can do whatever you need to do with it, for example copy somewhere
			try {
				FileUtils.copyFile(scrFile, new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//test.log(LogStatus.FAIL, details + test.addScreenCapture(path));
			ChildTest.log(LogStatus.PASS, "[STEP:] " + stepName + ",      [DETAILS:] "+ details + ChildTest.addScreenCapture(path));	
		}else{
			ChildTest.log(LogStatus.PASS, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
		}
	}
	
	/**2. logFAIL , details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logFAIL, logs the failed steps to the Reporter with detail parameters and also takes
	 * 				 the screenshots
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logFAIL(String stepName,String details){
		if(framework_logFileOperations.config.getProperty("ScreenShot_CaptureFAIL").toString().equalsIgnoreCase("true")){
			date = new Date();
			driver =aut_BusinessLibrariesClass.getdriver();
			scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String path = System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Screenshots\\"+date.toString().replace(" ", "_").replace(":", "_")+".png";
			// Now you can do whatever you need to do with it, for example copy somewhere
			try {
				FileUtils.copyFile(scrFile, new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ChildTest.log(LogStatus.FAIL, "[STEP:] " + stepName + ",      [DETAILS:] "+ details + ChildTest.addScreenCapture(path));	
		}else{
			ChildTest.log(LogStatus.FAIL, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);	
		}

	}
	
	
	/**3. logERROR , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logERROR, logs the WARNING steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logERROR(String stepName, String details){
		ChildTest.log(LogStatus.ERROR, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**4. logFATAL , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logFATAL, logs the FATAL steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logFATAL(String stepName, String details){
		if(framework_logFileOperations.config.getProperty("ScreenShot_CaptureFAIL").toString().equalsIgnoreCase("true")){
			date = new Date();
			driver =aut_BusinessLibrariesClass.getdriver();
			scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String path = System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Screenshots\\"+date.toString().replace(" ", "_").replace(":", "_")+".png";
			// Now you can do whatever you need to do with it, for example copy somewhere
			try {
				FileUtils.copyFile(scrFile, new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//test.log(LogStatus.FAIL, details + test.addScreenCapture(path));
			ChildTest.log(LogStatus.FATAL, "[STEP:] " + stepName + ",      [DETAILS:] "+ details + ChildTest.addScreenCapture(path));	
		}else{
			ChildTest.log(LogStatus.FATAL, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
		}
	}
	
	/**5. logSKIP , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logSKIP, logs the SKIP steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logSKIP(String stepName, String details){
		ChildTest.log(LogStatus.SKIP, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**6. logUNKNOWN , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logUNKNOWN, logs the UNKNOWN steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logUNKNOWN(String stepName, String details){
		ChildTest.log(LogStatus.UNKNOWN, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**7. logWARNING , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logWARNING, logs the WARNING steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logWARNING(String stepName, String details){
		ChildTest.log(LogStatus.WARNING, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**8. logINFO , details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logINFO, logs the information steps to the Reporter with detail parameter only
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logINFO(String stepName, String details){
		ChildTest.log(LogStatus.INFO, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**9. logINFO_Keyword , details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logINFO_Keyword, logs the information for keyword level
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logINFO_Keyword(String stepName, String details){
		ChildTest.log(LogStatus.INFO, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/**6. logTCName , Steps, details
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logTCName, logs the TC name in starting
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logTCName(String stepName, String details){
		ChildTest.log(LogStatus.UNKNOWN, "[STEP:] " + stepName + ",      [DETAILS:] "+ details);
	}
	
	/** OpenResult
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: OpenResult, open up the final report of the execution at the end of the execution
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void OpenResult(){
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile ffprofile = profile.getProfile(framework_logFileOperations.config.getProperty("Firfox_Profile"));
		WebDriver result = new FirefoxDriver(ffprofile);
		result.manage().window().maximize();
		result.get(System.getProperty("user.dir")+"\\Results\\Run_"+ResultFolderName+"\\Report.html");
	}
	
	/** getResultFolder
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: getResultFolder, return the result folder directory
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static String getResultFolder(){
		return ResultFolderName;
	}
	
	/** getDate
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: getDate, retrun the date object
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static Date getDate(){
		return date;
	}
	
	/** getTestObject
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: getTestObject, retrun the date object
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static ExtentTest getExtentTest(){
		return test;
	}
	
	
}
