package common_restapi_code;
import com.keynote.REST.KeynoteRESTClient;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Please change the Access server URL based on your environment.This script is pointing it to Keynote Mobile Testing Shared environment.
 * Please provide Your Keynote Mobile Test Automation account User_Name
 * Please provide Your Keynote Mobile Test Automation account Password
 * Please provide Device MCD.
 *
 * @author  Kapeel Dev Maheshwari
 * 
 */

public class iOSSingleDeviceTestCase {

    public static final String ACCESS_SERVER_URL = "https://tceaccess.deviceanywhere.com:6232/resource";
    public static final String USER_NAME = "******";
    public static final String PASSWORD = "******";
	
	

    static int mcd = 000; //Please provide the mcd number here.
    static String appiumUrl;
    static AppiumDriver driver = null;
    
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
		capabilities.setCapability("platformVersion", "9.2");
		capabilities.setCapability("deviceName", "iPhon6");
		capabilities.setCapability("platformName", "iOS");
		
		//either provide  the URL to download the app as given below or provide the appActivity and appPackage in set capability.
		
		capabilities.setCapability("app", " ");
		
		//capabilities.setCapability("appPackage", "com.expensemanager");
        //capabilities.setCapability("appActivity", "com.expensemanager.ExpenseManager");
		
		
		try {
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

        //driver.closeApp();
    	if (driver != null)
        	driver.quit();
        keynoteClient.stopAppium(mcd);
        keynoteClient.logoutSession();

    }

}
 