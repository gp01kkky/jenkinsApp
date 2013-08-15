/**
 * This is for making a http and get a json result
 * 
 * @author kelvinkhoo
 */
package jenkinsapp.dataqueryserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.StrictMode;
import android.util.Base64;
import org.apache.http.client.methods.*;

public class ServerParser{
	final int CONNECTION_TIMEOUT = 10000;
	final int WAIT_RESPONSE_TIMEOUT =180000;
	static InputStream is = null;
	static JSONObject jsonObj = null;
	static String json = "";

	public ServerParser() {
		
	}
	/**
	 * This function make a httpget to the URL and request for a Jsonobject reply
	 * @param url 
	 * @param userName
	 * @param token
	 * @return JSONObject
	 * 
	 * @author kelvinkhoo
	 */
	public JSONObject getJSONFromUrl(String url, String userName, String token) {
		
		// For higher version android, this is needed to perform server data connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		StringBuilder builder = new StringBuilder();
	    //HttpClient client = new DefaultHttpClient();
	    
	    
	    
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    
	    /**
	     * This function setup the retry times for a http connection
	     */
	    HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
	        public boolean retryRequest(
	                IOException exception, 
	                int executionCount,
	                HttpContext context) {
	            if (executionCount >= 5) {
	                // Do not retry if over max retry count
	                return false;
	            }
	            if (exception instanceof InterruptedIOException) {
	                // Timeout
	                return false;
	            }
	            if (exception instanceof UnknownHostException) {
	                // Unknown host
	                return false;
	            }
	            if (exception instanceof ConnectException) {
	                // Connection refused
	                return false;
	            }
	            if (exception instanceof SSLException) {
	                // SSL handshake exception
	                return false;
	            }
	            HttpRequest request = (HttpRequest) context.getAttribute(
	                    ExecutionContext.HTTP_REQUEST);
	            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest); 
	            if (idempotent) {
	                // Retry if the request is considered idempotent 
	                return true;
	            }
	            return false;
	        }

	    };

	    
	    httpclient.setHttpRequestRetryHandler(myRetryHandler);
	    HttpParams httpParameters = httpclient.getParams();

	    HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	    HttpConnectionParams.setSoTimeout(httpParameters, WAIT_RESPONSE_TIMEOUT);
	    HttpConnectionParams.setTcpNoDelay(httpParameters, true);
	    
		httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials(userName, token));
		BasicScheme basicAuth = new BasicScheme();
		BasicHttpContext context = new BasicHttpContext();
		context.setAttribute("preemptive-auth", basicAuth);
		httpclient.addRequestInterceptor(new PreemptiveAuth(), 0);
	    HttpGet httpGet = new HttpGet(url);
	    if(!userName.equals("")&&!token.equals(""))
	    {
	    	httpGet.addHeader("Authorization", "Basic " + Base64.encodeToString((userName+":"+ token).getBytes(),Base64.NO_WRAP));
	    }	    
	    
	    try {
	      HttpResponse response = httpclient.execute(httpGet);
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
	
	static class PreemptiveAuth implements HttpRequestInterceptor {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.HttpRequest,
		 * org.apache.http.protocol.HttpContext)
		 */
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			// Get the AuthState
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

			// If no auth scheme available yet, try to initialize it preemptively
			if (authState.getAuthScheme() == null) {
				AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null) {
					Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost
							.getPort()));
					if (creds == null) {
						throw new HttpException("No credentials for preemptive authentication");
					}
					authState.setAuthScheme(authScheme);
					authState.setCredentials(creds);
				}
			}

		}

	}
}
