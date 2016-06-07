package com.keynote.REST;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Assert;

import com.google.common.base.Throwables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.MediaType;



public class KeynoteRESTClient {

    private String email;
    private String pass;
    private URL accessServerUrl;
    private static String sessionID;
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
                System.out.println("Keynote Mobile Testing Session is created successfully with SessionID: " + sessionID);
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
    
    public static String addApplication( String URL, String appName, String appType, String appVersion, String fileName, String filePath)
    {
        try {
            JSONObject jsonObject = new JSONObject();
            StringWriter writer = new StringWriter();
            jsonObject.put("isSignApp", "true");
            jsonObject.put("isEnableApp", "true");
            jsonObject.put("appType", appType);
            jsonObject.put("appName", appName);
            jsonObject.put("appVersion", appVersion);
            jsonObject.put("fileName", fileName);
            File file = new File(filePath);
            FileInputStream stream = new FileInputStream(file);
            byte[] fileContent = new byte[8192];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int length = 0;
            while((length = stream.read(fileContent)) > 0) {
                byteArrayOutputStream.write(fileContent, 0, length);
            }
            Base64 base64 = new Base64();
            String fileString = base64.encodeAsString(byteArrayOutputStream.toByteArray());
            jsonObject.put("fileContent", fileString);
            jsonObject.write(writer);
            InputStream inputstream[] =
            		RESTUtils.restRequest(URL + "/device/" + sessionID + "/" + "add-application",
                        "POST", "application/json", "text/plain", writer.toString());
            if(stream != null) {
                return BufferUtils.getStringBuffer(inputstream[0]).toString();
            }
        } catch(Exception e1) {
           e1.printStackTrace();
        }
        return null;
    }

public static byte[] downloadData(String downloadURL)
{
    InputStream inputstream[] =
    		RESTUtils.restRequest(downloadURL,
                    "GET", "application/text", "application/octet-stream", null);
    try {
        if (inputstream != null) {
            byte[] data = new byte[8192];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int length = 0;
            while ((length = inputstream[0].read(data, 0, data.length)) > 0) {
                stream.write(data, 0, length);
            }
            return stream.toByteArray();
        }
    } catch(Exception e1) {
        e1.printStackTrace();
    }
    return null;
}

public static String getURL(String URL,String appName, String appType, String appVersion)
{
    try {
        JSONObject jsonObject = new JSONObject();
        StringWriter writer = new StringWriter();
        jsonObject.put("appType", appType);
        jsonObject.put("appName", appName);
        jsonObject.put("appVersion", appVersion);
        jsonObject.write(writer);
        InputStream inputstream[] =
        		RESTUtils.restRequest(URL + "/applications/" + sessionID + "/" + "get-application-url",
                        "POST", "application/json", "application/json", writer.toString());
        if(inputstream!= null) {
            return BufferUtils.getStringBuffer(inputstream[0]).toString();
        }
    } catch(Exception e1) {
        e1.printStackTrace();
    }
    return null;

}

public static String installApplication(String ensembleURL, int applicationID)
{
    JSONObject jsonObject = new JSONObject();
    StringWriter writer = new StringWriter();
    jsonObject.put("applicationID", applicationID);
   // jsonObject.put("launchApplication", true);
    //jsonObject.put("removeApplication", true);
    jsonObject.write(writer);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try {
        InputStream[] streams = RESTUtils.restRequest(ensembleURL + "/install-application", "POST" , MediaType.APPLICATION_JSON, MediaType.WILDCARD, writer.toString());
        if(streams != null && streams.length > 0) {
            byte[] data = new byte[8 * 1024];
            outputStream.reset();
            while (true) {
                int p = streams[0].read(data);
                if(p < 0)
                    break;
                else
                    outputStream.write(data, 0, p);
            }

            return new String(outputStream.toByteArray(), "UTF-8");
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    return null;
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
