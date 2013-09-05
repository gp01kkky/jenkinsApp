/* 	TerminalOutput.java
 * 	Displays the console output from tbe build 
 * 	@author Kelvin Khoo & Edward James
 */

package jenkinsapp.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import jenkinsapp.dataqueryserver.ServerPlainTextParser;
import jenkinsapp.server.database.BuildData;
import kkky.jenkinsapp.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TerminalOutput extends Activity {
	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	public static String url ="";
	public static String urlOriginal = "";
	public static String username = "";
	public static String token = "";
	public static String jobName = "";
	private String isHttps = "";
	private StringBuilder data;
	private BuildData build;
	private ArrayList<String> strChange ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
		Intent intent = getIntent();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		jobName = mode.getString("jobName");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");
    		
    		build = (BuildData) intent.getParcelableExtra("buildData");

    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terminal_output);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);		
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		fetchConsole fetchConsole = new fetchConsole();
		fetchConsole.execute(url);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.build_status, menu);
		return true;
	}
	
	 public void onClickHomeButton(View Button){
		 Intent intent = new Intent();
		 intent.setClass(TerminalOutput.this, MainMenu.class);
		 startActivity(intent);
	 }
	 
	 public void onClickRefreshButton(View view){
		 pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		fetchConsole fetchConsole = new fetchConsole();
		fetchConsole.execute(url);	
	 }
	
	public void onClickBuildHistory (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildStatus.class);
		intent.putExtra("url",urlOriginal);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
    	startActivity(intent);
    	finish();
    }
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildHistory.class);
		intent.putExtra("url",urlOriginal);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
    	startActivity(intent);
    	finish();
    }
	
	public void onClickBuildOverview (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, CopyOfBuildOverview.class);
		intent.putExtra("url",urlOriginal);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
    	startActivity(intent);
    	finish();
    }
	 public class fetchConsole extends AsyncTask<String, Void, StringBuilder>
	    {
		 @Override
		 protected StringBuilder doInBackground(String... args){
			StringBuilder consoleOutput = new StringBuilder() ;
			 
			    consoleOutput=fetchBuildConsole(url);

		     return consoleOutput;
		    }
		 
		 
		 protected void onPreExecute(){
			 super.onPreExecute();
			 pd.show();
		 }
		 protected void onPostExecute(StringBuilder result){
			 
			 data = new StringBuilder();
			 if (TerminalOutput.this.pd != null)
			 {
				TerminalOutput.this.pd.dismiss();	
				 data = result;
				TextView aTextView =  (TextView) findViewById(R.id.TerminalOutput1);
				TextView changes = (TextView) findViewById(R.id.terminal_changes);
				TextView tookText = (TextView) findViewById(R.id.tooktext);
				TextView buildNumber = (TextView) findViewById(R.id.build_number);
				aTextView.setText(readLogFile());	 
				
				strChange = new ArrayList<String>();
				strChange = build.getChanges();
				
				StringBuilder textChanges = new StringBuilder();
			    buildNumber.setText("#"+Integer.toString(build.getNumber())+ " (" + build.getDate() +", " + build.getTime() +")");
	        	tookText.setText("Took " + build.getDuration() + " on " + build.getBuiltOn() + "");

		        if(strChange.size()>0)
		        {
		           //textChanges.append("Recent Changes:\n");

		        	for(int i=0;i<strChange.size();i++)
		        	{
		        		textChanges.append((i+1)+". " + strChange.get(i)+"\n");
		        	}
		        	

		        }
		        else
		        {
			        textChanges.append("No recent Changes\n");
		        }
		        
		        changes.setText(textChanges.toString());

			 }
			 else {
				 runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
						}
					});
			 }
			 //System.out.println();
		 }
		 }
	 
	 public StringBuilder fetchBuildConsole(String url)
	    {
		 //String buildUrl = url.replaceFirst("http", "https")+"consoleText";
		String buildUrl;
		
		if(isHttps.equals("TRUE"))
	 	{
		 	if(!(url.substring(0, 5).equalsIgnoreCase("https")))
		 	{
	 		buildUrl = url.replaceFirst("http", "https")+"consoleText";
		 	}
		 	else
		 	{
		 		buildUrl = url + "consoleText";
		 	}
	 	}
	 	else
	 		buildUrl = url + "consoleText";
	 

		 StringBuilder consoleOutput = new StringBuilder();
			try {
				ServerPlainTextParser http = new ServerPlainTextParser();
	            consoleOutput= http.getPlainTextFromUrl(buildUrl,username,token);
	            createLogFile(consoleOutput);
			} catch (Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
					}
				});
			}		 
	    return consoleOutput;
	    }
	 
	 
	 public void createLogFile(StringBuilder log)
	 { 
		 try {
				OutputStreamWriter out = new OutputStreamWriter(openFileOutput("log.txt", 0));
				out.write(log.toString());
				out.close();
				
			}catch(Throwable t) {
					Toast.makeText(getApplicationContext(), "Error generating a file", Toast.LENGTH_SHORT).show();
			}
	 }
	 
	 public String readLogFile()
	 {
		 try {
				FileInputStream in = openFileInput("log.txt");
				if (in != null){
					InputStreamReader tmp = new InputStreamReader(in);
					BufferedReader reader = new BufferedReader(tmp);
					StringBuilder buf = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null){
						buf.append(line);
					}
					
					in.close();
					
					String buffer = buf.toString();
					return buffer;
				}
				
			}catch(Throwable t) {
				
					Toast.makeText(getApplicationContext(), "Error generating a file", Toast.LENGTH_SHORT).show();
			}
			return null;

	 }
	
}
