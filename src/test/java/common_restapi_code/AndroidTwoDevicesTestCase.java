package common_restapi_code;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.apache.http.util.Asserts;
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
 * Please change the Access server URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account Password
 * Please provide Device MCD(s).
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */

@RunWith(KeynoteParallel.class) //un-commenting this will run the test in parallel on multiple devices 
//@RunWith(Parameterized.class) // un-commenting this will run the test in serial on multiple devices

public class AndroidTwoDevicesTestCase {
	    private static final String ACCESS_SERVER_URL = "https://dadaccess12qasm.keynote.com:6232/resource";
	    private static final String USER_NAME = "aditya@mc.com";
	    private static final String PASSWORD = "Harmony1";
	    
	    private static KeynoteRESTClient keynoteClient;
	    
	   
	    
	    @Parameterized.Parameters
	    public static Collection deviceMCDs() {
	       return Arrays.asList(new Object[][] {
	          { 9233 }, //Enter device mcd's here
	          {9021}
	         /*{ 8896 },
	          {8853},
	          */
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
        	} catch (Exception e) {
				Asserts.check(false, "Unable to login to Mobile Testing");
			}
	    }

	    
	    	/* First setup Keynote Connection */
	    	public AndroidTwoDevicesTestCase(int mcd) throws Exception {
	       
//	            
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
	            this.mcd = mcd;
	            Thread.sleep(5000);

	        }
	        
	    	

	    	
	    @Test
	    public void appiumTest() throws InterruptedException, MalformedURLException
	    {
	    	
	    	System.out.println("Executing Appium script on mcd " + mcd);
	    	
	    	DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("deviceName","Samsung");

	        capabilities.setCapability("platformVersion", "5.0.1");
	        capabilities.setCapability("platformName","Android");
	       
	      //either provide  the URL to download the application as given below or provide the appActivity and appPackage in setcapability.
	      
	        capabilities.setCapability("app", "http://tcportal21qasm.win.keynote.com/app/6480.apk");
	     
	        capabilities.setCapability("appPackage", "com.expensemanager");
	        capabilities.setCapability("appActivity", "com.expensemanager.ExpenseManager");
	        
	        
	      
	    	try {
	    		
	    		
	            driver = new AndroidDriver(new URL(appiumUrl), capabilities);
	            System.out.println("Executing Appium script on " + mcd);
	            //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	            Thread.sleep(2000);
	            driver.findElement(By.name("Add New Expense")).click();
	            Thread.sleep(3000);
	            driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.expensemanager:id/expenseAmountInput']")).sendKeys("80");
	            Thread.sleep(2000);

	            driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.expensemanager:id/payee']")).sendKeys("BOFA");
	            driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id='com.expensemanager:id/editCategory']")).click();
	           // driver.findElement(By.name("OK")).click();
	           /* driver.findElement(By.name("Loans")).click();
	            driver.findElement(By.name("Auto")).click();
	            driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id='com.expensemanager:id/editPaymentMethod']")).click();
	            driver.findElement(By.name("Credit Card")).click();
	            driver.findElement(By.name("OK")).click();
	            driver.findElement(By.name("Today Expense:")).click();
	            driver.findElement(By.name("Loans:Auto")).click();
	            driver.findElement(By.name("Delete")).click();
	            driver.findElement(By.name("OK")).click();*/
	            driver.navigate().back();
	            
	        } catch (MalformedURLException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }

	        }
	    
	    @After
	    public void tearDown() throws Exception {
	    	driver.quit();
	        //keynoteClient.stopAppium(mcd); // This will stop the Appium without log
	        keynoteClient.stopappiumwithlog(sessionIDEnsem, mcd);//This will stop the Appium and will download the Appium log file at userprofile desktop
	       
	    }
	    
	    @AfterClass
	    public static void logoutSystem()
	   
	    {
	    	keynoteClient.logoutSession();
	    	System.out.println("Logged out from Keynote Mobile Testing Session");
	    }
}

	    