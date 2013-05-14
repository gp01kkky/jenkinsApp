/* BuildQueue.java
 * To fetch the build that was queued from the server and output to an array list
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.util.ArrayList;

import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.QueueData;
import jenkinsapp.uihelper.QueueListArrayAdapter;
import kkky.jenkinsapp.R;

import org.json.JSONArray;
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

public class BuildQueue extends Activity {
	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	private QueueListArrayAdapter adapter;
	private ArrayList<QueueData> data;
	private static String url;
	private ArrayList<QueueData> queueListOut = new ArrayList<QueueData>();
	private String serverName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{	// append the url with the jenkins json api link
    		url = mode.getString("serverUrl")+"/queue/api/json?pretty=true";
    		serverName = mode.getString("serverName");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_build_queue);
		TextView textview =(TextView) findViewById(R.id.Project_title);
	    textview.setText("Queue: "+ serverName);
		
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		fetchQueue fetchQueue = new fetchQueue();
		fetchQueue.execute(url);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.build_queue, menu);
		return true;
	}
	
	// perform async task to fetch build queue from the server url
	public class fetchQueue extends AsyncTask<String, Void, ArrayList<QueueData>>
    {
	 @Override
	 protected ArrayList<QueueData> doInBackground(String... args)
	 {
		 ArrayList<QueueData> queueList = new ArrayList<QueueData>();		 
			try {
				
				ServerParser http = new ServerParser();
				
	             JSONObject json = http.getJSONFromUrl(url);
	             
	             JSONArray queues;
	             queues = json.getJSONArray("items");//store all the json data object
	             
	             for (int i = 0; i < queues.length(); i++) {
	            	 QueueData queueData = new QueueData(); //store queue data
	                 JSONObject queue = (JSONObject) queues.get(i);
	                 JSONObject task = (JSONObject) queue.get("task");
	                 queueData.setReason(queue.getString("why"));
	                 queueData.setJobName(task.getString("name"));
	                 queueData.setUrl(task.getString("url"));
	                 queueData.setResult(task.getString("color"));
	                 queueList.add(queueData);
	                 queueListOut.add(queueData);
	             }
	   
			}  catch (Exception e) {
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
					}
				});
			}			 
	     return queueList;
	 }
	 
	 protected void onPreExecute(){
		 super.onPreExecute();
		 pd.show();
	 }
	 
	 protected void onPostExecute(ArrayList<QueueData> result){
		 BuildQueue.this.data = result;
		 if (BuildQueue.this.pd != null)
		 {
			 BuildQueue.this.pd.dismiss();
			 ListView list = (ListView) findViewById(R.id.queueList);

			    adapter = new QueueListArrayAdapter(context,R.layout.queue_list_item,data);
				list.setAdapter(adapter);				
				list.setOnItemClickListener(new OnItemClickListener(){
				
				//go to the selected project
				@Override	
				public void onItemClick(AdapterView<?> l, View v, int position, long id) 
				{
					  Intent intent = new Intent();
					  intent.setClass(BuildQueue.this,BuildHistory.class);
					  intent.putExtra("url",data.get(position).getUrl());
					  startActivity(intent);
					  finish();
				}
					
				});
		 }

	 }
	 }
}
