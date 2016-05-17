package main.java.brij;

import java.io.IOException;

import pkg_FrameworkDriver.framework_DriverClass;

public class init_Class {

	public static void main(String[] args) throws IOException{
		framework_DriverClass init = new framework_DriverClass();
		System.out.println("Selenium Test Started");
		init.init_FW();
		System.out.println("Selenium Test Finished");
	}
}
