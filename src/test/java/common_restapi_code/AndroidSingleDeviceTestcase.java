package common_restapi_code;
import com.keynote.REST.KeynoteRESTClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.apache.http.util.Asserts;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

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

  /*  public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";*/
    
    public static final String ACCESS_SERVER_URL = "https://dadaccess12qasm.keynote.com:6232/resource";
    public static final String USER_NAME = "aditya@mc.com";
    public static final String PASSWORD = "Harmony1";

    static int mcd = 9233;
    static String appiumUrl;
    private static AppiumDriver driver;
    private static KeynoteRESTClient keynoteClient;
    static String sessionIDEnsem="";

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

        }
        
        catch (Exception e){
            System.out.println("Unable to create Keynote REST api connection. Exiting");
            keynoteClient.logoutSession();
            System.exit(1);
        }


    }

    @Test
    public void appiumTest() throws InterruptedException
    {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","Samsung");

        capabilities.setCapability("platformVersion", "5.0.1");
        capabilities.setCapability("platformName","Android");
       
      //either provide  the URL to download the application as given below or provide the appActivity and appPackage in set capability.
      
        //capabilities.setCapability("app", "http://dademo111.deviceanywhere.com/app/534050.apk");
        
        
        capabilities.setCapability("appPackage", "com.expensemanager");
        capabilities.setCapability("appActivity", "com.expensemanager.ExpenseManager");
        
        
        try {
            driver = new AndroidDriver(new URL(appiumUrl), capabilities);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try
        {
        	System.out.println("Executing Appium script on " + mcd);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            Thread.sleep(2000);
            driver.findElement(By.name("Add New Expense")).click();
            Thread.sleep(3000);
            driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.expensemanager:id/expenseAmountInput']")).sendKeys("80");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//android.widget.EditText[@resource-id='com.expensemanager:id/payee']")).sendKeys("BOFA");
            driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id='com.expensemanager:id/editCategory']")).click();
            driver.findElement(By.name("OK")).click();
            driver.findElement(By.name("Loans")).click();
            driver.findElement(By.name("Auto")).click();
            driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id='com.expensemanager:id/editPaymentMethod']")).click();
            driver.findElement(By.name("Credit Card")).click();
            driver.findElement(By.name("OK")).click();
            driver.findElement(By.name("Today Expense:")).click();
            driver.findElement(By.name("Loans:Auto")).click();
            driver.findElement(By.name("Delete")).click();
            driver.findElement(By.name("OK")).click();
            driver.navigate().back();



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


 