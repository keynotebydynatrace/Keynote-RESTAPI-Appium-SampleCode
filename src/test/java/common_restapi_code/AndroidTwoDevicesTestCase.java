package common_restapi_code;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.http.util.Asserts;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.keynote.REST.KeynoteParallel;
import com.keynote.REST.KeynoteRESTClient;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.runners.Parameterized;
/**
 * Please change the Access_server_URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account PASSWORD
 * Please provide Device MCD(s).
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */

/*un-commenting this, will run the test in parallel on multiple devices. No of Parallel device depends upon the values in KeynoteParallel class*/
@RunWith(KeynoteParallel.class) 

/* un-commenting this, will run the test in serial on multiple devices*/
//@RunWith(Parameterized.class) 

public class AndroidTwoDevicesTestCase {
	
	 public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";
	   
	    private static String appName = "KeynoteDemo";
	    private static String appVersion = "1.3";
	    private static String fileName = "KeynoteDemo-debug.apk";
	    private static String appType = "ANDROID_APK";
	    private static String filepath="KeynoteDemo-debug.apk";
	    private static String ApplicationID;
	    private static String AppUrl="";
	    
	    private static KeynoteRESTClient keynoteClient;
	    
	   
	    
	    @Parameterized.Parameters
	    public static Collection deviceMCDs() {
	       return Arrays.asList(new Object[][] {
	          { 123 }, //Enter device mcd's here
	          {1234},
	          //{},
	         // {},
	         // {},
	          //{},
	         // {}
	          
	         
	       });
	    }
	 

	    String appiumUrl;
	    AppiumDriver driver;
	    String sessionIDEnsem="";
	    int mcd;
	    
	    @BeforeClass
	    public static void loginToMobileTesting()
	    {
	    	 // create the session
	    	
        	try {
				keynoteClient = new KeynoteRESTClient(USER_NAME, PASSWORD, ACCESS_SERVER_URL);
			
				keynoteClient.createSession();
            
            Thread.sleep(2000);
            
            //Upload Application to Keynote Mobile testing repository  and get the URL to pass it to appium setcapability
            
            filepath = AndroidSingleDeviceTestcase.class.getClassLoader().getResource(fileName).getFile();
            ApplicationID=(KeynoteRESTClient.addApplication(ACCESS_SERVER_URL, appName, appType, appVersion,fileName, filepath));
	    	System.out.println("Application uploaded with id "+ ApplicationID);
	    	String parseUrl=KeynoteRESTClient.getURL(ACCESS_SERVER_URL, appName, appType, appVersion);
	    	JSONObject jsonObj = new JSONObject(parseUrl);;
	    	AppUrl = jsonObj.getString("value");
	    	System.out.println("Your Application URL is: " + AppUrl);
	    	
            
        	} catch (Exception e) {
				Asserts.check(false, "Unable to login to Mobile Testing");
			}
	    }
	    

	    ;
	    	
	    	public AndroidTwoDevicesTestCase(int mcd) throws Exception {
	       
           
	    		/*  lock a specific device*/ 
	    		 
	    		sessionIDEnsem=keynoteClient.lockDevice(mcd);
	            System.out.println("Device with mcd " + mcd + " is locked sucessfully" );
	           
	            
	            /* fire up appium on the device*/
	            appiumUrl = keynoteClient.startAppium(mcd);
	            if(appiumUrl.isEmpty())
	            {
	            Asserts.notEmpty(appiumUrl, "Unable to start appium as return url for mcd " + mcd);
	            }
	            
	            System.out.println("Appium Session is started on mcd "+ mcd);
	            this.mcd = mcd;
	            Thread.sleep(5000);

	        }
	        
	    	
	    @SuppressWarnings("rawtypes")
		@Test
	    public void appiumTest() throws InterruptedException, MalformedURLException
	    {
	    	
	    	 
	    	DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("deviceName","Samsung");

	        capabilities.setCapability("platformVersion", "5.0.1");
	        capabilities.setCapability("platformName","Android");
	       
	      //either provide  the URL to download the application as given below or provide the appActivity and appPackage in setcapability.
	      
	        capabilities.setCapability("app", AppUrl);
	     
	       // capabilities.setCapability("appPackage", "com.keynote.keynotedemo");
	       // capabilities.setCapability("appActivity", "com.keynote.keynotedemo.UserInfo");
	        
	        
	      
	    	try {
	    		
	    		
	            driver = new AndroidDriver(new URL(appiumUrl), capabilities);
	            System.out.println("Executing Appium script on mcd " + mcd);
	            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/title"));
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_first_name")).sendKeys("Jack ");
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_last_name")).sendKeys("Turner");
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_phone")).sendKeys("777-777-777");
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/radioMale")).click();
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_email")).sendKeys("Jack@mobiletest.com");
	        	driver.navigate().back();
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/spinner1")).click();
	        	driver.findElement(By.name("Spanish")).click();
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/next_button")).click();
	        	Thread.sleep(2000);
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_recipients")).sendKeys("test@demo.com");
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/edit_message")).sendKeys("Welcome to Keynote Mobile Tetsing demo");
	        	driver.findElement(By.id("com.keynote.keynotedemo:id/send_button")).click();
	            
	        } catch (MalformedURLException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }

	        }
	    
	    @After
	    public void tearDown() throws Exception {
	    	driver.quit();
	    	
	        //keynoteClient.stopAppium(mcd); // This will stop the appium without log
	        keynoteClient.stopappiumwithlog(sessionIDEnsem, mcd);//This will stop the appium and will download the Appium log file at userprofile desktop
	       
	    }
	    
	    @AfterClass
	    public static void logoutSystem()
	   
	    {
	    	keynoteClient.logoutSession();
	    	System.out.println("Logged out from Keynote Mobile Testing Session");
	    }
}

	    