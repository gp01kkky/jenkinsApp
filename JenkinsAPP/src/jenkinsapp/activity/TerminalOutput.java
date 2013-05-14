/* 	TerminalOutput.java
 * 	Displays the console output from tbe build 
 * 	@author Kelvin Khoo & Edward James
 */

package jenkinsapp.activity;

import jenkinsapp.dataqueryserver.ServerPlainTextParser;
import kkky.jenkinsapp.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		urlOriginal = mode.getString("urlOriginal");

    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terminal_output);

		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		fetchConsole fetchConsole = new fetchConsole();
		fetchConsole.execute(url);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.build_status, menu);
		return true;
	}
	
	public void onClickBuildHistory (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildStatus.class);
		intent.putExtra("url",urlOriginal);
    	startActivity(intent);
    	finish();
    }
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildHistory.class);
		intent.putExtra("url",urlOriginal);
    	startActivity(intent);
    	finish();
    }
	 public class fetchConsole extends AsyncTask<String, Void, String>
	    {
		 @Override
		 protected String doInBackground(String... args){
			String consoleOutput ="" ;
			 try {
				    consoleOutput=fetchBuildConsole(url);
				
			 } catch (Exception e){
				 runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
						}
					});
			 }			 
		     return consoleOutput;
		    }
		 
		 
		 protected void onPreExecute(){
			 super.onPreExecute();
			 pd.show();
		 }
		 protected void onPostExecute(String result){
			 
			 if (TerminalOutput.this.pd != null)
			 {
				TerminalOutput.this.pd.dismiss();					
				TextView mTextView = (TextView) findViewById(R.id.TerminalOutput);
				mTextView.setText(result);	 
			 }
		 }
		 }
	 
	 public String fetchBuildConsole(String url)
	    {
		 String buildUrl = url.replaceFirst("http", "https")+"consoleText";
		 String consoleOutput ="";
			try {
				ServerPlainTextParser http = new ServerPlainTextParser();
	            consoleOutput= http.getPlainTextFromUrl(buildUrl);
			} catch (Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();

					}
				});
			}		 
	    return consoleOutput;
	    }
	
}
