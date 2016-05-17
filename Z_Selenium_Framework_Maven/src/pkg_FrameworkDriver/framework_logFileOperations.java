/**
 * 
 */
package pkg_FrameworkDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/** framework_logFileOperations
 * ----------------------------------------------------------------------------------------------------
 * @author: Brijendra Singh
 * @Date  : May 03, 2016 
 * @Discription: framework_logFileOperations, Manages the Framework logfile and logging methods
 * -----------------------------------------------------------------------------------------------------
 */
public class framework_logFileOperations {
	
	public static BufferedWriter log_file;
	public static Properties config;
	static Date date;
	
	/** createLogFile
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: createLogFile, Creates the .txt log file for framework operation logging
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static BufferedWriter createLogFile(){
		String LogFileName;
		LogFileName=framework_logFileOperations.config.getProperty("LogFile_Name");
		try {
			log_file = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+"//Files//"+LogFileName+".txt")));
		} catch (IOException e) {
			System.out.println("Error Creating/Loading the Framework LogFile: [" + LogFileName + "]");
			e.printStackTrace();
		}
		return log_file;
	}
	
	/** ConfiGFileSetup
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 05, 2016 
	 * @Discription: ConfiGFileSetup,load and setup the Framework Configuration File
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static Properties ConfiGFileSetup(){
		config=null;
		try {
			File configFile = new File(System.getProperty("user.dir")+"\\Files\\Config.property");
			FileInputStream configFIS = new FileInputStream(configFile);
			config=new Properties();
			config.load(configFIS);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error creating/loading the Properties File " +e.getCause() );
			e.printStackTrace();
		}
		return config;
	}
	
	/** logThis
	 * ----------------------------------------------------------------------------------------------------
	 * @author: Brijendra Singh
	 * @Date  : May 03, 2016 
	 * @Discription: logThis, append the logs in the .txt framework log file
	 * -----------------------------------------------------------------------------------------------------
	 */
	public static void logThis(String Step, String StepDetail, String Status){
		date = new Date();
		if (Step.equalsIgnoreCase("")==false){
			try {
				log_file.newLine();
				log_file.write("Time: "+ date);
				log_file.newLine();
				log_file.write("[STEP:] " + Step + "   ,[DETAIL:] " + StepDetail + "   ,[STATUS:] " + Status );
				log_file.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 }
}
