/*
 * for making connection to server and the data as plain text from the server
 * author@Kelvin Khoo
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

import jenkinsapp.dataqueryserver.ServerParser.PreemptiveAuth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.StrictMode;
import android.util.Base64;

public class ServerPlainTextParser{	
	
	final int CONNECTION_TIMEOUT = 10000;
	final int WAIT_RESPONSE_TIMEOUT =180000;
	
	public ServerPlainTextParser() {
		
	}

	public StringBuilder getPlainTextFromUrl(String url, String userName, String token) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		StringBuilder builder = new StringBuilder();
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    
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
		return builder;

	}


	
}
