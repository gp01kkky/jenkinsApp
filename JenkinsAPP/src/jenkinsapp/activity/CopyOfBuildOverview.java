/* Build Status class
 * Displays the build status as a graph and a list of details of previous builds
 * Activity written and graph integrated by Kelvin Khoo 
 * Graph by Edward James
 */

package jenkinsapp.activity;

import graphview.GraphView.GraphViewData;
import java.util.ArrayList;

import jenkinsapp.activity.BuildQueue.fetchQueue;
import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.BuildData;
import jenkinsapp.server.database.JobOverviewData;
import jenkinsapp.uihelper.BuildHistoryArrayAdapter;
import jenkinsapp.uihelper.BuildTextArrayAdapter;
import jenkinsapp.uihelper.RecentChangesArrayAdapter;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CopyOfBuildOverview extends Activity {

	static GraphViewData[] graphData;

	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	BuildTextArrayAdapter adapter;
	RecentChangesArrayAdapter adapter2;
	private ArrayList<BuildData> data;
	private static String url;
	private String jobName = "";
	private String isHttps = "";
	private String username = "";
	private String token = "";
	private ArrayList<BuildData> overviewData;
	private ArrayList<BuildData> recentChanges; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
		if (mode != null) {
			jobName = mode.getString("jobName");
			url = mode.getString("url");
			isHttps = mode.getString("isHttps");
			username = mode.getString("username");
			token = mode.getString("token");

		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.copy_of_activity_build_overview);
		TextView textview = (TextView) findViewById(R.id.Project_title);
		textview.setText(jobName);
		// display a dialog box for loading
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

	public void onClickRefreshButton(View view) {
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		fetchAllBuilds fetchAllBuilds = new fetchAllBuilds();
		fetchAllBuilds.execute(url);
	}

	public void onClickHomeButton(View Button) {
		Intent intent = new Intent();
		intent.setClass(CopyOfBuildOverview.this, MainMenu.class);
		startActivity(intent);
	}

	public void onClickBuildHistory(View Button) {
		Intent intent = new Intent();
		intent.setClass(this, BuildHistory.class);
		intent.putExtra("url", url);
		intent.putExtra("jobName", jobName);
		intent.putExtra("isHttps", isHttps);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
		startActivity(intent);
		finish();
	}

	public void onClickBuildStatus(View Button) {
		Intent intent = new Intent();
		intent.setClass(this, BuildStatus.class);
		intent.putExtra("url", url);
		intent.putExtra("jobName", jobName);
		intent.putExtra("isHttps", isHttps);
		intent.putExtra("username", username);
		intent.putExtra("token", token);
		startActivity(intent);
		finish();
	}

	public void onClickGraph(View LinearLayout) {
		// do nothing
	}

	public class fetchAllBuilds extends
			AsyncTask<String, Void, ArrayList<BuildData>> {
		@Override
		protected ArrayList<BuildData> doInBackground(String... args) {
			// ArrayList<String> buildUrl = new ArrayList<String>();
		    recentChanges = new ArrayList<BuildData>();
			overviewData= new ArrayList<BuildData>();
			ArrayList<BuildData> buildList = new ArrayList<BuildData>();
			String buildUrl;
			if (isHttps.equals("TRUE")) {
				if (!(url.substring(0, 5).equalsIgnoreCase("https"))) {
					buildUrl = url.replaceFirst("http", "https")
							+ "api/json?tree=builds[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastSuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastStableBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastUnsuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastFailedBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]]";
				} else {
					buildUrl = url
							+ "api/json?tree=builds[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastSuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastStableBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastUnsuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastFailedBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]]";
				}
			} else
				buildUrl = url
						+ "api/json?tree=builds[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastSuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastStableBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastUnsuccessfulBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]],lastFailedBuild[url,number,id,timestamp,result,duration,builtOn,changeSet[items[comment]]]";

			try {
				ServerParser http = new ServerParser();
				JSONArray builds;
				JSONObject change;
				JSONArray buildChanges;
				JSONObject json = http
						.getJSONFromUrl(buildUrl, username, token);
			
				JSONObject lastFailed = json.optJSONObject("lastFailedBuild");
				if(lastFailed!=null)
				{
				String lastFailedMsg = "Last Failed Build (" + lastFailed.getString("number") + "), " + lastFailed.getString("id");
				BuildData lastFailedBuild = new BuildData();
				change = lastFailed.getJSONObject("changeSet");
				buildChanges = change.getJSONArray("items");
				for(int j=0;j<buildChanges.length();j++)
				{
					JSONObject commented =  (JSONObject) buildChanges.get(j);
					if(commented.opt("comment")!=null)
						lastFailedBuild.addChanges(commented.getString("comment"));
				}
				lastFailedBuild.setBuiltOn(lastFailed.getString("builtOn"));
				lastFailedBuild.setDuration(lastFailed.getInt("duration"));
				String[] id = lastFailed.getString("id").split("_");

				lastFailedBuild.setDate(id[0]);
				lastFailedBuild.setTime(id[1].replaceAll("-", ":"));
				lastFailedBuild.setNumber(lastFailed.getInt("number"));
				lastFailedBuild.setResult(lastFailed.getString("result"));
				lastFailedBuild.setUrl(lastFailed.getString("url"));
				lastFailedBuild.setMessage(lastFailedMsg);
				overviewData.add(lastFailedBuild);
				}
				
				JSONObject lastStable = json.optJSONObject("lastStableBuild");
				if(lastStable!=null){
				String lastStableMsg = "Last Stable Build (" + lastStable.getString("number") + "), " + lastStable.getString("id");
				BuildData lastStableBuild = new BuildData();
				change = lastStable.getJSONObject("changeSet");
				buildChanges = change.getJSONArray("items");
				for(int j=0;j<buildChanges.length();j++)
				{
					JSONObject commented =  (JSONObject) buildChanges.get(j);
					if(commented.opt("comment")!=null)
						lastStableBuild.addChanges(commented.getString("comment"));
				}
				lastStableBuild.setBuiltOn(lastStable.getString("builtOn"));
				lastStableBuild.setDuration(lastStable.getInt("duration"));
				String[] id = lastStable.getString("id").split("_");
				
				lastStableBuild.setDate(id[0]);
				lastStableBuild.setTime(id[1].replaceAll("-", ":"));
				lastStableBuild.setNumber(lastStable.getInt("number"));
				lastStableBuild.setResult(lastStable.getString("result"));
				lastStableBuild.setUrl(lastStable.getString("url"));
				lastStableBuild.setMessage(lastStableMsg);
				overviewData.add(lastStableBuild);
				}
				
				
				JSONObject lastSuccessful = json.optJSONObject("lastSuccessfulBuild");
				if(lastSuccessful!=null)
				{
				String lastSuccessfulMsg = "Last Successful Build (" + lastSuccessful.getString("number") + "), " + lastSuccessful.getString("id");
				BuildData lastSuccessfulBuild = new BuildData();
				change = lastSuccessful.getJSONObject("changeSet");
				buildChanges = change.getJSONArray("items");
				for(int j=0;j<buildChanges.length();j++)
				{
					JSONObject commented =  (JSONObject) buildChanges.get(j);
					if(commented.opt("comment")!=null)
						lastSuccessfulBuild.addChanges(commented.getString("comment"));
				}
				lastSuccessfulBuild.setBuiltOn(lastSuccessful.getString("builtOn"));
				lastSuccessfulBuild.setDuration(lastSuccessful.getInt("duration"));
				String[] id = lastSuccessful.getString("id").split("_");

				lastSuccessfulBuild.setDate(id[0]);
				lastSuccessfulBuild.setTime(id[1].replaceAll("-", ":"));
				lastSuccessfulBuild.setNumber(lastSuccessful.getInt("number"));
				lastSuccessfulBuild.setResult(lastSuccessful.getString("result"));
				lastSuccessfulBuild.setUrl(lastSuccessful.getString("url"));
				lastSuccessfulBuild.setMessage(lastSuccessfulMsg);
				overviewData.add(lastSuccessfulBuild);
				}
				
				JSONObject lastUnsuccessful = json.optJSONObject("lastUnsuccessfulBuild");
				if(lastUnsuccessful!=null)
				{
				String lastUnsuccessfulMsg = "Last UnsuccessfulBuild (" + lastUnsuccessful.getString("number") + "), " + lastUnsuccessful.getString("id");
				BuildData lastUnsuccessfulBuild = new BuildData();
				change = lastUnsuccessful.getJSONObject("changeSet");
				buildChanges = change.getJSONArray("items");
				for(int j=0;j<buildChanges.length();j++)
				{
					JSONObject commented =  (JSONObject) buildChanges.get(j);
					if(commented.opt("comment")!=null)
						lastUnsuccessfulBuild.addChanges(commented.getString("comment"));
				}
				lastUnsuccessfulBuild.setBuiltOn(lastUnsuccessful.getString("builtOn"));
				lastUnsuccessfulBuild.setDuration(lastUnsuccessful.getInt("duration"));
				String[] id = lastUnsuccessful.getString("id").split("_");

				lastUnsuccessfulBuild.setDate(id[0]);
				lastUnsuccessfulBuild.setTime(id[1].replaceAll("-", ":"));
				lastUnsuccessfulBuild.setNumber(lastUnsuccessful.getInt("number"));
				lastUnsuccessfulBuild.setResult(lastUnsuccessful.getString("result"));
				lastUnsuccessfulBuild.setUrl(lastUnsuccessful.getString("url"));
				lastUnsuccessfulBuild.setMessage(lastUnsuccessfulMsg);
				overviewData.add(lastUnsuccessfulBuild);
				}
				
			
				builds = json.getJSONArray("builds");
				ArrayList<String> comments = new ArrayList<String>();
				for (int i = 0; i < builds.length(); i++) {
					JSONObject build = (JSONObject) builds.get(i);
					change = build.getJSONObject("changeSet");
					buildChanges = change.getJSONArray("items");
					
					BuildData buildData = new BuildData();
					buildData.setBuiltOn(build.getString("builtOn"));
					buildData.setDuration(build.getInt("duration"));
					String[] id = build.getString("id").split("_");

					buildData.setDate(id[0]);
					buildData.setTime(id[1].replaceAll("-", ":"));
					buildData.setNumber(build.getInt("number"));
					buildData.setResult(build.getString("result"));
					buildData.setUrl(build.getString("url"));
					for(int j=0;j<buildChanges.length();j++)
					{
						JSONObject commented =  (JSONObject) buildChanges.get(j);
						if(commented.opt("comment")!=null)
							buildData.addChanges(commented.getString("comment"));
					}
					
					buildList.add(buildData);
					if(buildData.getChanges().size()>0)
					{
						recentChanges.add(buildData);
					}
				}

			} catch (Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(context, "Failed to contact server",
								Toast.LENGTH_SHORT).show();

					}
				});
			}

			return buildList;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd.show();
		}

		protected void onPostExecute(ArrayList<BuildData> result) {
			CopyOfBuildOverview.this.data = result;
			if (CopyOfBuildOverview.this.pd != null) {
				CopyOfBuildOverview.this.pd.dismiss();
				ListView list = (ListView) findViewById(R.id.recentChangesList);

				ListView list2 = (ListView) findViewById(R.id.buildHistoryList);

				adapter = new BuildTextArrayAdapter(getApplicationContext(),
						R.layout.text_list, overviewData);
				if(recentChanges.size()==0)
				{
					TextView text = (TextView) findViewById(R.id.RecentChangesText);
					text.setText("No recent changes");
				}
				else
				{
				adapter2 = new RecentChangesArrayAdapter(getApplicationContext(),R.layout.changes_list_item,recentChanges);
					
				list.setAdapter(adapter2);
				list.setOnItemClickListener(new OnItemClickListener(){
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(CopyOfBuildOverview.this,TerminalOutput.class);
						  intent.putExtra("buildData", data.get(position)); 
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("urlOriginal", url);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}
						
					});		 
				}
//				adapter2 = new RecentChangesArrayAdapter(getApplicationContext(),R.layout.changes_list_item,data);
				list2.setAdapter(adapter);
				list2.setOnItemClickListener(new OnItemClickListener(){
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(CopyOfBuildOverview.this,TerminalOutput.class);
						  intent.putExtra("buildData", overviewData.get(position)); 
						  intent.putExtra("url",overviewData.get(position).getUrl());
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
}