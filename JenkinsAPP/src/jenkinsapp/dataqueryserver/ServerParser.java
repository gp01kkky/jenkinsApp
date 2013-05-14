/* For making connection to server retrieve json object 
 * 
 * Reference from 
 * http://stackoverflow.com/questions/4457492/simple-http-example-in-android 
 *
 * author@Kelvin Khoo 
 */
package jenkinsapp.dataqueryserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.StrictMode;

public class ServerParser{

	static InputStream is = null;
	static JSONObject jsonObj = null;
	static String json = "";

	public ServerParser() {
		
	}

	public JSONObject getJSONFromUrl(String url) {
		
		// For higher version android, this is needed to perform server data connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(url);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        return null;
	      }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }
	    try {
			jsonObj = new JSONObject(builder.toString());
		} catch (JSONException e) {
		}
		return jsonObj;

	}
}
