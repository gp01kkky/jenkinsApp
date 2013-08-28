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
	private String isHttps = "";
	private String username = "";
	private String token = "";
	private ArrayList<BuildData> buildListout = new ArrayList<BuildData>();
	private ArrayList<String> buildUrlListout = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		jobName = mode.getString("jobName");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_build_history);
		TextView textview =(TextView) findViewById(R.id.Project_title);
	    textview.setText(jobName);
		//display a pop up loading window
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		fetchAllBuild fetchAllBuild = new fetchAllBuild();
		fetchAllBuild.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.build_status, menu);
		return true;
	}
	
	 public void onClickHomeButton(View Button){
		 Intent intent = new Intent();
		 intent.setClass(BuildHistory.this, MainMenu.class);
		 startActivity(intent);
	 }
	 
	 public void onClickRefreshButton(View view){
		 pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		fetchAllBuild fetchAllBuild = new fetchAllBuild();
		fetchAllBuild.execute(url);
	 }
	 
	// go to build status activity
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildStatus.class);
		intent.putExtra("url",url);
		intent.putExtra("jobName", jobName);
		intent.putExtra("isHttps", isHttps);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
    	startActivity(intent);
    	finish();		
    }
	
	// perform an async task to connect and fetch data from server 
	 public class fetchAllBuild extends AsyncTask<String, Void, ArrayList<BuildData>>
	    {
		 @Override
		 protected ArrayList<BuildData> doInBackground(String... args){
			 //ArrayList<String> buildUrl = new ArrayList<String>();
			 ArrayList<BuildData> buildList = new ArrayList<BuildData>();
			 String buildUrl;
			 if(isHttps.equals("TRUE"))
			 	{
				 	if(!(url.substring(0, 5).equalsIgnoreCase("https")))
				 	{
			 		buildUrl = url.replaceFirst("http", "https")+"api/json?tree=builds[number,id,timestamp,result,duration,builtOn,url]";
				 	}
				 	else
				 	{
				 		buildUrl = url + "api/json?tree=builds[number,id,timestamp,result,duration,builtOn,url]";
				 	}
			 	}
			 	else
			 		buildUrl = url + "api/json?tree=builds[number,id,timestamp,result,duration,builtOn,url]";
			 

			 try {
				 ServerParser http = new ServerParser();
	             JSONObject json = http.getJSONFromUrl(buildUrl,username,token);
	             
	             JSONArray builds;
					builds = json.getJSONArray("builds");
					
					for(int i =0; i<builds.length();i++){
						JSONObject build = (JSONObject) builds.get(i);
						 BuildData buildData = new BuildData();

						 buildData.setBuiltOn(build.getString("builtOn"));
			             buildData.setDuration(build.getInt("duration"));
			             String[] id = build.getString("id").split("_");
			             
			             buildData.setDate(id[0]);
			             buildData.setTime(id[1].replaceAll("-", ":"));
			             buildData.setNumber(build.getInt("number"));
			             buildData.setResult(build.getString("result"));
			             buildData.setUrl(build.getString("url"));		
			             buildList.add(buildData);
					}
	             
			}  catch (Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Retrying...", Toast.LENGTH_SHORT).show();

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
				adapter = new BuildListArrayAdapter(getApplicationContext(),R.layout.build_list_item,data);
				list.setAdapter(adapter);
				
				list.setOnItemClickListener(new OnItemClickListener(){
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(BuildHistory.this,TerminalOutput.class);
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("urlOriginal", url);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}
						
					});		 
			 }
		 }
		 

		 
		 }
	
	 
//	 	//fetch all the build url from the server
//		public ArrayList<String> fetchAllBuildUrl(String url)
//		{
//			//append the link for fetching the data from jenkins
//			//String buildUrl = url.replaceFirst("http", "https")+"api/json?pretty=true";
//			String buildUrl;
//			if(isHttps.equals("TRUE"))
//			{
//				buildUrl = url.replaceFirst("http", "https")+"/api/json";
//			}
//			else
//			{
//				buildUrl = url + "/api/json";
//			}
//			
//			ArrayList<String> buildUrlList = new ArrayList<String>();
//			try{
//				ServerParser http = new ServerParser();
//				JSONObject json = http.getJSONFromUrl(buildUrl,username,token);
//				JSONArray builds;
//				builds = json.getJSONArray("builds");
//				
//				for(int i =0; i<builds.length();i++){
//					JSONObject build = (JSONObject) builds.get(i);
//					buildUrlList.add(build.getString("url"));
//					buildUrlListout.add(build.getString("url"));
//				}
//			}
//			catch (JSONException e){
//				runOnUiThread(new Runnable(){
//					public void run(){
//				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
//
//					}
//				});
//			}
//			return buildUrlList;
//		}
//		//fetch the information of the build and store it in an object
//		 public void fetchBuild(String url)
//		    {
//			 String BuildUrl = url.replaceFirst("http", "https")+"api/json";
//			 BuildData buildData = new BuildData();
//			 ArrayList<BuildData> buildList = new ArrayList<BuildData>();
//			 
//				try {
//					 ServerParser http = new ServerParser();
//		             JSONObject json = http.getJSONFromUrl(BuildUrl,username,token);
//		             buildData.setBuiltOn(json.getString("builtOn"));
//		             buildData.setDuration(json.getInt("duration"));
//		             String[] id = json.getString("id").split("_");
//		             buildData.setDate(id[0]);
//		             buildData.setTime(id[1].replaceAll("-", ":"));
//		             buildData.setNumber(json.getInt("number"));
//		             buildData.setResult(json.getString("result"));
//		             buildData.setUrl(json.getString("url"));
//		    	     buildListout.add(buildData);
//				}  catch (JSONException e) {
//					runOnUiThread(new Runnable(){
//						public void run(){
//					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
//						}
//					});
//				}			 
//		    }
}
