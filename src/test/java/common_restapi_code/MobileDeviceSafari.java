package common_restapi_code;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.http.util.Asserts;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.keynote.REST.KeynoteRESTClient;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * Please change the Access server URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account Password
 * Please provide Device MCD(s).
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */
public class MobileDeviceSafari {
	/*  public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";*/
    
    public static final String ACCESS_SERVER_URL = "https://dadaccess12qasm.keynote.com:6232/resource";
    public static final String USER_NAME = "aditya@mc.com";
    public static final String PASSWORD = "Harmony1";
	
	

    static int mcd = 8501; //Please provide the mcd number here.
    static String appiumUrl;
    static AppiumDriver driver = null;
    static String sessionIDEnsem="";
    
    private static KeynoteRESTClient keynoteClient;

    @BeforeClass
    /* First setup Keynote Connection */
    public static void setUp() throws Exception {
        try {
            // create the session
        	 keynoteClient = new KeynoteRESTClient(USER_NAME, PASSWORD, ACCESS_SERVER_URL);
             keynoteClient.createSession();

             // lock a specific device
             sessionIDEnsem=keynoteClient.lockDevice(mcd);
	            System.out.println("Device with mcd " + mcd + " is locked sucessfully" );

             // fire up appium on the device
             appiumUrl = keynoteClient.startAppium(mcd);
             if(appiumUrl.isEmpty())
             {
             Asserts.notEmpty(appiumUrl, "Unable to start appium as return url for mcd " + mcd);
             }
             
             System.out.println("Appium Session is started on mcd "+ mcd);

             Thread.sleep(5000);

     
         
        }catch (Exception e){
            System.out.println("Unable to create Keynote REST api connection. Exiting");
            keynoteClient.logoutSession();
            System.exit(1);
        }


    }

    @Test
    public void appiumTest() throws InterruptedException
    {

    	DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "");
		capabilities.setCapability("platformVersion", "9.3");
        capabilities.setCapability("platformName","iOS");
		
		try 
		{
			
			driver = new IOSDriver(new URL(appiumUrl), capabilities);
			driver.get("http://www.google.com");
			driver.get("http://www.yahoo.com");
			driver.get("http://www.bing.com");
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

    @After
    public void tearDown() throws Exception {
    	driver.quit();
        //keynoteClient.stopAppium(mcd); // This will stop the Appium without log
        keynoteClient.stopappiumwithlog(sessionIDEnsem, mcd);// This will stop the Appium and will download the Appium log file at userprofile desktop
       
         
    }
    @AfterClass
    public static void logoutSystem()
   
    {
    	keynoteClient.logoutSession();
    	System.out.println("Logged out from Keynote Mobile Testing Session");
    }

}
 