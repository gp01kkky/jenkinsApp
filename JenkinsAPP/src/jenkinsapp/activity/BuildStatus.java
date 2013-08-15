/* Build Status class
 * Displays the build status as a graph and a list of details of previous builds
 * Activity written and graph integrated by Kelvin Khoo 
 * Graph by Edward James
 */

package jenkinsapp.activity;

import graphview.GraphView.GraphViewData;
import java.util.ArrayList;
import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.BuildData;
import jenkinsapp.uihelper.BuildHistoryArrayAdapter;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BuildStatus extends Activity {

	static GraphViewData[] graphData;
	
	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	BuildHistoryArrayAdapter adapter;
	private ArrayList<BuildData> data;
	private static String url;
	private String jobName = "";
	private String isHttps = "";
	private String username = "";
	private String token = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		jobName = mode.getString("jobName");
    		url = mode.getString("url");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");

    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_build_status);
		TextView textview =(TextView) findViewById(R.id.Project_title);
	    textview.setText(jobName);
		//display a dialog box for loading
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		fetchAllBuilds fetchAllBuilds = new fetchAllBuilds();
		fetchAllBuilds.execute(url);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.build_history, menu);
		return true;
	}
	
	 public void onClickHomeButton(View Button){
		 Intent intent = new Intent();
		 intent.setClass(BuildStatus.this, MainMenu.class);
		 startActivity(intent);
	 }
	 
	 public void onClickBuildHistory (View Button){
	    	Intent intent = new Intent();
	    	intent.setClass(this, BuildHistory.class);
			intent.putExtra("url",url);
			intent.putExtra("jobName", jobName);
			intent.putExtra("isHttps", isHttps);
			intent.putExtra("username", username);
			intent.putExtra("token", token);
	    	startActivity(intent);
	    	finish();		
	    }
	 

	 public void onClickGraph (View LinearLayout){
	   // do nothing
	 } 	 	
	 public class fetchAllBuilds extends AsyncTask<String, Void, ArrayList<BuildData>>
	    {
		 @Override
		 protected ArrayList<BuildData> doInBackground(String... args){
			 //ArrayList<String> buildUrl = new ArrayList<String>();
			 ArrayList<BuildData> buildList = new ArrayList<BuildData>();
			 String buildUrl;
			 if(isHttps.equals("TRUE"))
			 	{
			 		buildUrl = url.replaceFirst("http", "https")+"api/json?tree=builds[number,id,timestamp,result,duration,builtOn,url]";
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
	             
			}  catch (JSONException e) {
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
			 BuildStatus.this.data = result;
			 if (BuildStatus.this.pd != null)
			 {
				 BuildStatus.this.pd.dismiss();
				 ListView list = (ListView) findViewById(R.id.buildHistoryList);

				 adapter = new BuildHistoryArrayAdapter(getApplicationContext(),R.layout.build_list,data);
				 list.setAdapter(adapter);
				 // draw graph here
				 int num = data.size();
				 if (num != 0){
				 graphData = new GraphViewData[num];
				 for (int i=0; i<num; i++) {
					graphData[i] = new GraphViewData(i, data.get(i).getStatus());
				}

				LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
				layout.addView(GraphFactory.buildGraph(false, "Test Result Trend", graphData, context)); 
			 }
			 
			 else {
				 runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
						}
					});
			 }
			 }
		 }
		 
		 
	    }
}