package common_restapi_code;
import com.keynote.REST.KeynoteRESTClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
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

    public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";

    static int mcd = 0000;
    static String appiumUrl;
    private AppiumDriver driver;
    private static KeynoteRESTClient keynoteClient;

    @BeforeClass
    /* First setup Keynote Connection */
    public static void setUp() throws Exception {
        try {
            // create the session
            keynoteClient = new KeynoteRESTClient(USER_NAME, PASSWORD, ACCESS_SERVER_URL);
            keynoteClient.createSession();

            // lock a specific device
            keynoteClient.lockDevice(mcd);

            // fire up appium on the device
            appiumUrl = keynoteClient.startAppium(mcd);

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
        capabilities.setCapability("deviceName","Samsung");

        capabilities.setCapability("platformVersion", "5.0.1");
        capabilities.setCapability("platformName","Android");
       
      //either provide  the URL to download the application as given below or provide the appActivity and appPackage in set capability.
      
        capabilities.setCapability("app", "http://dademo111.deviceanywhere.com/app/534050.apk");
        
        
        //capabilities.setCapability("appPackage", "com.expensemanager");
        //capabilities.setCapability("appActivity", "com.expensemanager.ExpenseManager");
        
        
        try {
            driver = new AndroidDriver(new URL(appiumUrl), capabilities);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try
        {

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

        finally
        {

            driver.quit();
            
            
        }


    }



    @After
    public void tearDown() throws Exception {

        //driver.closeApp();
        driver.quit();
        
        keynoteClient.stopAppium(mcd);
        keynoteClient.logoutSession();

    }

}
 