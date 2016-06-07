package common_restapi_code;
import com.keynote.REST.KeynoteRESTClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.Assert;

import org.apache.http.util.Asserts;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Please change the Access server URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account Password
 * Please provide Device MCD.
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */

public class AndroidSingleDeviceTestcase {

   public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";
    private static int mcd = 1234;
    static String appiumUrl;
    private static AppiumDriver driver;
    private static KeynoteRESTClient keynoteClient;
    private static String sessionIDEnsem="";
    private static String appName = "KeynoteDemo";
    private static String appVersion = "1.3";
    private static String fileName = "KeynoteDemo-debug.apk";
    private static String appType = "ANDROID_APK";
    private static String filepath="KeynoteDemo-debug.apk";
    private static String ApplicationID;
    private static String AppUrl="";

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
            
            
          //Upload Application to Keynote mobile testing repository and get the URL to pass it to appium setcapability
      
            
            filepath = AndroidSingleDeviceTestcase.class.getClassLoader().getResource(fileName).getFile();
            ApplicationID=(KeynoteRESTClient.addApplication(ACCESS_SERVER_URL, appName, appType, appVersion,fileName, filepath));
	    	System.out.println("Application uploaded with id "+ ApplicationID);
	    	String parseUrl=KeynoteRESTClient.getURL(ACCESS_SERVER_URL, appName, appType, appVersion);
	    	JSONObject jsonObj = new JSONObject(parseUrl);;
	    	AppUrl = jsonObj.getString("value");
	    	System.out.println("Your Application URL is: " + AppUrl);
	    	
    
            // Fire up appium on the device
            appiumUrl = keynoteClient.startAppium(mcd);
            if(appiumUrl.isEmpty())
            {
            Asserts.notEmpty(appiumUrl, "Unable to start appium as return url for mcd " + mcd);
            }
            
            System.out.println("Appium Session is started on mcd "+ mcd);

            Thread.sleep(5000);

        }
        
        catch (Exception e){
            System.out.println(e);
            keynoteClient.logoutSession();
            System.exit(1);
        }


    }

    @SuppressWarnings("rawtypes")
	@Test
    public void appiumTest() throws InterruptedException
    {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Samsung");

        capabilities.setCapability("platformVersion", "5.0.1");
        capabilities.setCapability("platformName","Android");
       
      /*either provide  the URL to download the application as given below or
       provide the appActivity and appPackage in set capability if app is already installed on the phone.*/
      
        capabilities.setCapability("app", AppUrl);
        
        
       // capabilities.setCapability("appPackage", "com.keynote.keynotedemo");
        //capabilities.setCapability("appActivity", "com.keynote.keynotedemo.UserInfo");
        
        
        try {
            driver = new AndroidDriver(new URL(appiumUrl), capabilities);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try
        {
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
        	
        	

        }
        catch (InterruptedException e)
        {
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
    public static void logoutSystem() {

        keynoteClient.logoutSession();
    	System.out.println("Logged out from Keynote Mobile Testing Session");
    }

    }


 