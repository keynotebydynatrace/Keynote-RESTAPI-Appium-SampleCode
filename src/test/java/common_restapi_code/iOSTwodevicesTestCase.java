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
 * @author  Kapeel Dev Maheshwari.
 * 
 */

@RunWith(KeynoteParallel.class)
//@RunWith(Parameterized.class)
public class iOSTwodevicesTestCase {
	public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
	public static final String USER_NAME = "admin@tce.com";
	public static final String PASSWORD = "Password1";;
	    private static String appName = "KitchenSinkApp";
		private static String appVersion = "1.3";
		private static String fileName = "KitchenSinkApp.ipa";
		private static String appType = "iPhone";
		private static String filepath = "C:/Users/kmaheshw.WIN/Desktop/8.1 Sprint/iOSOfflineAutomation/KitchenSinkApp.ipa";
		private static int ApplicationID ;
	    
	    private static KeynoteRESTClient keynoteClient;
	    
	    @Parameterized.Parameters
	    public static Collection deviceMCDs() {
	       return Arrays.asList(new Object[][] {
	          {30053 },  //Enter device mcd's here
	          { 30056 }
	          //{6984}
	       });
	    }
	 

	    String appiumUrl;
	    AppiumDriver driver;
	    static String sessionIDEnsem="";
	    int mcd;
	    
	    /* First setup Keynote Connection */
	    @BeforeClass
	    public static void loginToMobileTesting()
	    {

        	try {
				keynoteClient = new KeynoteRESTClient(USER_NAME, PASSWORD, ACCESS_SERVER_URL);
			
				keynoteClient.createSession();
            
            Thread.sleep(2000);
            
         // Upload Application to Keynote repository and get the URL to pass it to appium setcapability
         			ApplicationID = Integer.valueOf((KeynoteRESTClient.addApplication(ACCESS_SERVER_URL, appName, appType, appVersion, fileName,filepath)));
         			System.out.println("Application " + appName + " is uploaded with id " + ApplicationID);
         			
         			
         			
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
	            
	            String App=KeynoteRESTClient.installApplication(sessionIDEnsem, ApplicationID);
	            
	            // fire up appium on the device
	            appiumUrl = keynoteClient.startAppium(mcd);
	            if(appiumUrl.isEmpty())
	            {
	            Asserts.notEmpty(appiumUrl, "Unable to start appium as return url for mcd " + mcd);
	            }
	            
	            System.out.println("Appium Session is started on mcd "+ mcd);
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
			/*
			 * either provide the URL to download the application as given below or
			 * provide the bundleId in set capability if application is already
			 * installed on the phone.
			 */

			// capabilities.setCapability("app",AppUrl);

			capabilities.setCapability("bundleId", "com.kone.KitchenSink");

			// capabilities.setCapability("bundleId", "XYZ");
			
			try {
				System.out.println("Executing Appium script on mcd " + mcd);
				driver = new IOSDriver(new URL(appiumUrl), capabilities);
				
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAButton[2]")).click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]"))
						.click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]"))
						.click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]"))
						.click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[1]/UIAStaticText[1]"))
						.click();
				driver.findElement(
						By.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAScrollView[1]/UIATextField[1]"))
						.click();
				driver.findElement(
						By.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAScrollView[1]/UIATextField[1]"))
						.sendKeys("Keynote Systems");
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAScrollView[2]/UIASecureTextField[1]"))
						.sendKeys("Keynote Systems");
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[2]/UIAStaticText[1]"))
						.click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By
						.xpath("//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIATableView[1]/UIATableCell[3]/UIAStaticText[1]"))
						.click();
				driver.findElement(By.name("Accelerometer")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
				driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[2]/UIANavigationBar[1]/UIAButton[1]")).click();
			    	
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
