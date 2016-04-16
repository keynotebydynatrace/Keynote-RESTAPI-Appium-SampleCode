package common_restapi_code;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.util.Asserts;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.keynote.REST.KeynoteParallel;
import com.keynote.REST.KeynoteRESTClient;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.runners.Parameterized;

/**
 * Please change the Access server URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account Password
 * Please provide Device MCD(s).
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */

@RunWith(KeynoteParallel.class)
//@RunWith(Parameterized.class)
public class iOSTwodevicesTestCase {
	    private static final String ACCESS_SERVER_URL = "https://dadaccess12qasm.keynote.com:6232/resource";
	   private static final String USER_NAME = "aditya@mc.com";
	   // private static final String USER_NAME = "kapeel@qatest.com";
	    private static final String PASSWORD = "Harmony1";
	    
	    private static KeynoteRESTClient keynoteClient;
	    
	    @Parameterized.Parameters
	    public static Collection deviceMCDs() {
	       return Arrays.asList(new Object[][] {
	          {8501 },  //Enter device mcd's here
	          { 9217 }
	          //{6984}
	       });
	    }
	 

	    String appiumUrl;
	    AppiumDriver driver;
	    String sessionIDEnsem="";
	    int mcd;
	    
	    /* First setup Keynote Connection */
	    @BeforeClass
	    public static void loginToMobileTesting()
	    {

        	try {
				keynoteClient = new KeynoteRESTClient(USER_NAME, PASSWORD, ACCESS_SERVER_URL);
			
				keynoteClient.createSession();
            
            Thread.sleep(2000);
        	} catch (Exception e) {
				Asserts.check(false, "Unable to create Keynote REST api connection. Exiting");
				keynoteClient.logoutSession();
	            System.exit(1);
			}
	    }
	    

	    /*  lock device and start appium */
	    public iOSTwodevicesTestCase(int mcd) throws Exception {
	        
	            // lock a specific device
	            sessionIDEnsem=keynoteClient.lockDevice(mcd);
	            System.out.println("Device with mcd " + mcd + " is locked sucessfully" );
	            
	            // fire up appium on the device
	            appiumUrl = keynoteClient.startAppium(mcd);
	            if(appiumUrl.isEmpty())
	            {
	            Asserts.notEmpty(appiumUrl, "Unable to start appium as return url for mcd " + mcd);
	            }
	            
	            System.out.println("Appium is strated on mcd "+ mcd);
	            this.mcd = mcd;
	            Thread.sleep(5000);

	        }
	        

	    @Test
	    public void appiumTest() throws InterruptedException, MalformedURLException
	    {

	    	DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformVersion", "9.2");
			capabilities.setCapability("deviceName", "iPhon6");
			capabilities.setCapability("platformName", "iOS");
			// capabilities.setCapability("browserName", "safari");
			capabilities.setCapability("app", "http://tcportal21qasm.win.keynote.com/app/5446.ipa");
			
			try {
				System.out.println("Executing Appium script on " + mcd);
				driver = new IOSDriver(new URL(appiumUrl), capabilities);
				
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIATableView[1]/UIATableCell[2]/UIATextField[1]")).sendKeys("Hello");
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIATableView[1]/UIATableCell[3]/UIATextField[1]")).sendKeys("Mytesting");
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIATableView[1]/UIATableCell[4]/UIATextField[1]")).sendKeys("777 Mariners island Blvd San Mateo");
			    	
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				
				e.printStackTrace();
			}
			
	    }
	    
	    @After
	    public void tearDown() throws Exception {

	        
	        driver.quit();
	        keynoteClient.stopAppium(mcd);
	      

	    }
	    
	    @AfterClass
	    public static void logoutSystem()
	   
	    {
	    	keynoteClient.logoutSession();
	    	System.out.println("Logged out from Keynote Mobile Testing Session");
	    }
}
