package com.keynote.REST;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Assert;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;



public class KeynoteRESTClient {

    private String email;
    private String pass;
    private URL accessServerUrl;
    private String sessionID;
    private  OutputStream outputStream = null;


    public KeynoteRESTClient(String email, String pass, String url) throws Exception{
        this.email = email;
        this.pass = pass;
        try {
            this.accessServerUrl = new URL(url);
        }catch(MalformedURLException e){
            // non recoverable error
            throw new Exception("Invalid Access URL given.");
        }
    }

    // disconnect on garbage collection
    protected void finalize() {
        logoutSession();
    }


    public Boolean createSession() throws Exception
    {
        // session id already exists
        if (sessionID != null) {
            throw new Exception("Session already exists. Please instantiate a new KeynoteRESTClient object");
        }

        try {
            InputStream[] stream = createSessionParams();
            JSONObject jsonObject;
            if(stream != null) {
                jsonObject = JSONUtils.getJsonObject(stream[0]);
                String status = (String)jsonObject.get("status");


                System.out.println("Status: " +  status);
                // wasn't able to create session
                
                
                if(!status.equals("SUCCESS")){
                    throw new Exception("Failed to create session");
                }

                sessionID = (String)jsonObject.get("sessionID");
                System.out.println("Keynote Mobile Testing Session created successfully with SessionID: " + sessionID);
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected InputStream[] createSessionParams() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", pass);
        StringWriter writer = new StringWriter();
        jsonObject.write(writer);
        return RESTUtils.restRequest(accessServerUrl + "/portal/establish-api-session",
                "POST", "application/json", "application/json", writer.toString());
    }

    public String lockDevice(int mcd)
    {
        try {
            StringWriter writer = createLockDeviceParams();
            InputStream stream[] = RESTUtils.restRequest(accessServerUrl + "/device/lock-device/" + mcd,
                    "POST", "application/json", "application/json", writer.toString());
            if (stream != null) {
				JSONObject jsonObject = JSONUtils.getJsonObject(stream[0]);	
				
				Boolean status = (Boolean) jsonObject.get("success");
				Assert.assertEquals(jsonObject.toString(),
						new Boolean("true"), status);
				String ensembleURL = (String) jsonObject.get("ensembleServerURL");
				return ensembleURL;

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "";
	}

    protected  StringWriter createLockDeviceParams() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionID", sessionID);
        StringWriter writer = new StringWriter();
        jsonObject.write(writer);
        return writer;
    }
    
    public String startAppium(int mcd)
    {
    	
        try {
        	
            InputStream stream[] =
                    RESTUtils.restRequest(accessServerUrl + "/device/" + sessionID + "/start-appium/" + mcd,
                            "GET", "application/json", "text/plain", null);
            if(stream != null) {
            	
            	return BufferUtils.getStringBuffer(stream[0]).toString();
            	
            }
        } catch(Exception e) {
            e.printStackTrace();
        } 
        
        
        return null;
    }
    
    public String stopAppium(int mcd) {
    	try {
            InputStream stream[] =
                    RESTUtils.restRequest(accessServerUrl + "/device/" + sessionID + "/stop-appium/" + mcd,
                            "GET", "application/json", "text/plain", null);
            if(stream != null) {
                return BufferUtils.getStringBuffer(stream[0]).toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void stopappiumwithlog(String sessionIDEnsem,int mcd) {
    
    InputStream[] stopappiumwithlog = RESTUtils.restRequest(sessionIDEnsem
			+ "/stop-appium-with-log", "GET", "application/json",
			"application/octet-stream", null);

	System.out.println("Stopping-Appium with log file for mcd " + mcd);

	try {
		
		outputStream = new FileOutputStream(new File(
				System.getProperty("user.home") + "/Desktop/appiumlog" + mcd + ".zip")); 

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = stopappiumwithlog[0].read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
			outputStream.flush();
		}

		System.out.println("Appium log is downloaded for mcd " +mcd + " at " + System.getProperty("user.home") + "/Desktop");

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outputStream != null) {
			try {
				// outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
    }
    
    

    public String logoutSession()
    {
        try {
            StringWriter writer = createLockDeviceParams();
            InputStream stream[] =
                    RESTUtils.restRequest(accessServerUrl + "/portal/logout-api-session",
                            "POST", "application/json", "application/json", writer.toString());
            if(stream != null) {
                return BufferUtils.getStringBuffer(stream[0]).toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;


    }
}
