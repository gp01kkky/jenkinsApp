/* BuildStatus.java
 * Displays a list of build informations in a job
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.util.ArrayList;

import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.BuildData;
import jenkinsapp.uihelper.BuildListArrayAdapter;
import kkky.jenkinsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BuildHistory extends Activity {
	
	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	private BuildListArrayAdapter adapter;
	private ArrayList<BuildData> data;
	private static String url;
	private String jobName ="";
	private ArrayList<BuildData> buildListout = new ArrayList<BuildData>();
	private ArrayList<String> buildUrlListout = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		jobName = mode.getString("jobName");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_build_history);
		TextView textview =(TextView) findViewById(R.id.Project_title);
	    textview.setText(jobName);
		//display a pop up loading window
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		fetchAllBuild fetchAllBuild = new fetchAllBuild();
		fetchAllBuild.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.build_status, menu);
		return true;
	}
	// go to build status activity
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildStatus.class);
		intent.putExtra("url",url);
		intent.putExtra("jobName", jobName);
    	startActivity(intent);
    	finish();		
    }
	
	// perform an async task to connect and fetch data from server 
	public class fetchAllBuild extends AsyncTask<String, Void, ArrayList<BuildData>>
	    {
		 @Override
		 protected ArrayList<BuildData> doInBackground(String... args){
			 ArrayList<String> buildUrl = fetchAllBuildUrl(url);
			 ArrayList<BuildData> buildList = new ArrayList<BuildData>();

			 try {
				for(String n:buildUrl)
				{
				    fetchBuild(n);
				}
			 } catch (Exception e){
				 runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();

						}
					});
			 }			 
		     return buildList;
		    }	 
		 protected void onPreExecute(){
			 super.onPreExecute();
			 pd.show();
		 }		 
		 protected void onPostExecute(ArrayList<BuildData> result){
			 BuildHistory.this.data = result;
			 if (BuildHistory.this.pd != null)
			 {
				BuildHistory.this.pd.dismiss();
				ListView list = (ListView) findViewById(R.id.buildList);
				adapter = new BuildListArrayAdapter(getApplicationContext(),R.layout.build_list_item,buildListout);
				list.setAdapter(adapter);
				
				list.setOnItemClickListener(new OnItemClickListener(){
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(BuildHistory.this,TerminalOutput.class);
						  intent.putExtra("url",buildListout.get(position).getUrl());
						  intent.putExtra("urlOriginal", url);
						  startActivity(intent);
					}
						
					});		 
			 }
		 }
		 }
	
	 
	 	//fetch all the build url from the server
		public ArrayList<String> fetchAllBuildUrl(String url)
		{
			//append the link for fetching the data from jenkins
			String buildUrl = url.replaceFirst("http", "https")+"api/json?pretty=true";
			ArrayList<String> buildUrlList = new ArrayList<String>();
			try{
				ServerParser http = new ServerParser();
				JSONObject json = http.getJSONFromUrl(buildUrl);
				JSONArray builds;
				builds = json.getJSONArray("builds");
				
				for(int i =0; i<builds.length();i++){
					JSONObject build = (JSONObject) builds.get(i);
					buildUrlList.add(build.getString("url"));
					buildUrlListout.add(build.getString("url"));
				}
			}
			catch (JSONException e){
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();

					}
				});
			}
			return buildUrlList;
		}
		//fetch the information of the build and store it in an object
		 public void fetchBuild(String url)
		    {
			 String BuildUrl = url.replaceFirst("http", "https")+"api/json?pretty=true";
			 BuildData buildData = new BuildData();
			 ArrayList<BuildData> buildList = new ArrayList<BuildData>();
			 
				try {
					 ServerParser http = new ServerParser();
		             JSONObject json = http.getJSONFromUrl(BuildUrl);
		             buildData.setBuiltOn(json.getString("builtOn"));
		             buildData.setDuration(json.getInt("duration"));
		             String[] id = json.getString("id").split("_");
		             buildData.setDate(id[0]);
		             buildData.setTime(id[1].replaceAll("-", ":"));
		             buildData.setNumber(json.getInt("number"));
		             buildData.setResult(json.getString("result"));
		             buildData.setUrl(json.getString("url"));
		    	     buildListout.add(buildData);
				}  catch (JSONException e) {
					runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
						}
					});
				}			 
		    }
}
