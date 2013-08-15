/* My_project.java
 * Draws data from the server by passing in the url in an asynctask
 * Displays the list of jobs in a ListView
 * author@KelvinKhoo
 * 
 */

package jenkinsapp.activity;


import java.util.ArrayList;

import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.BookmarkDataUtils;
import jenkinsapp.server.database.DatabaseUtils;
import jenkinsapp.server.database.JobData;
import jenkinsapp.server.database.ServerData;
import jenkinsapp.uihelper.ProjectListArrayAdapter;
import kkky.jenkinsapp.R;

import org.json.JSONArray;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


@SuppressLint("NewApi")
public class My_project extends Activity {
	private BookmarkDataUtils datasource;
	JSONArray jobs = null;
	Activity activity = this;
	Context context = this;
	// url to make request
	private static String url = "";
	private static String serverUrl = "";
	private static String isHttps = "";
	private ProgressDialog pd = null;
	private ArrayList<JobData> data;
	private ArrayList<JobData> success = new ArrayList<JobData>();
	private ArrayList<JobData> fail = new ArrayList<JobData>();
	ProjectListArrayAdapter adapter;
	private String desc = "";
	private String username = "";
	private String token = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		Bundle mode = getIntent().getExtras();
		
    	if(mode !=null)
    	{	//https://ci.jenkins-ci.org/view/All/api/json?pretty=true
    		
    		url = mode.getString("url")+"/api/json";
    		serverUrl = mode.getString("url");
    		desc = mode.getString("desc");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");
    	}
    	
    	//url = "https://ci.jenkins-ci.org/view/All/api/json?pretty=true";
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_project);
		datasource = new BookmarkDataUtils(this);
		datasource.open();
		TextView textview = (TextView) findViewById(R.id.Project_title);
		textview.setText("Server: " + desc);

		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Downloading data...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		fetchJob fetchJob = new fetchJob();
		fetchJob.execute(url);		
		
	}
	
	 public void onClickHomeButton(View Button){
		 Intent intent = new Intent();
		 intent.setClass(My_project.this, MainMenu.class);
		 startActivity(intent);
	 }
	
	public void onSettingsButtonClick(View Button) {
		PopupMenu popup = new PopupMenu(context, Button);
		popup.getMenuInflater().inflate(R.menu.my_project_popup, popup.getMenu());
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item){
				//Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
				if (item.getTitle().equals("Success Builds")){
					Toast.makeText(context, "Fetching Success Builds", Toast.LENGTH_SHORT).show();
					
					ListView list = (ListView) findViewById(R.id.paramList);

				    adapter = new ProjectListArrayAdapter(getApplicationContext(),R.layout.project_list_item,success);
				    
					list.setAdapter(adapter);
					list.setLongClickable(true);

					//create an onClickListener here
					list.setOnItemClickListener(new OnItemClickListener(){
					
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(My_project.this,BuildStatus.class);
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("jobName", data.get(position).getName());
						  intent.putExtra("serverName", desc);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}
					
						
					});
					//for long click
					list.setOnItemLongClickListener(new OnItemLongClickListener(){
						public boolean onItemLongClick(AdapterView<?> parent, View view,
			                    final int position, long id){				
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom ));
							alertDialog.setTitle("Add Bookmark");
							alertDialog.setMessage("Do you want to add this bookmark?");
							alertDialog.setNegativeButton("Add", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									String bookmarkUrl = data.get(position).getUrl();
									String jobName = data.get(position).getName() + "\n("+ desc +")";
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
									datasource.close();
									dialog.dismiss();
							}
						});
							alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									dialog.dismiss();
								} });
							alertDialog.show();
							
							return true;				
						}
					});
					
				}

				if (item.getTitle().equals("Fail Builds")){
					Toast.makeText(context, "Fetching Fail Builds", Toast.LENGTH_SHORT).show();
					
					ListView list = (ListView) findViewById(R.id.paramList);

				    adapter = new ProjectListArrayAdapter(getApplicationContext(),R.layout.project_list_item,fail);
				    
					list.setAdapter(adapter);
					list.setLongClickable(true);

					//create an onClickListener here
					list.setOnItemClickListener(new OnItemClickListener(){
					
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(My_project.this,BuildStatus.class);
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("jobName", data.get(position).getName());
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}		
					});
					
					//for long click
					list.setOnItemLongClickListener(new OnItemLongClickListener(){
						public boolean onItemLongClick(AdapterView<?> parent, View view,
			                    final int position, long id){				
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom ));
							alertDialog.setTitle("Add Bookmark");
							alertDialog.setMessage("Do you want to add this bookmark?");
							alertDialog.setNegativeButton("Add", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									String bookmarkUrl = data.get(position).getUrl();
									String jobName = data.get(position).getName() + "\n("+ desc +")";
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
									datasource.close();
									dialog.dismiss();
							}
						});
							alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									dialog.dismiss();
								} });
							alertDialog.show();
							
							return true;				
						}
					});
				}
				if (item.getTitle().equals("All Builds")){
					Toast.makeText(context, "Fetching All Builds", Toast.LENGTH_SHORT).show();
					
					ListView list = (ListView) findViewById(R.id.paramList);

				    adapter = new ProjectListArrayAdapter(getApplicationContext(),R.layout.project_list_item,data);
				    
					list.setAdapter(adapter);
					list.setLongClickable(true);

					//create an onClickListener here
					list.setOnItemClickListener(new OnItemClickListener(){
					
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(My_project.this,BuildStatus.class);
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("jobName", data.get(position).getName());
						  intent.putExtra("serverName", desc);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}	
					});	
					
					//for long click
					list.setOnItemLongClickListener(new OnItemLongClickListener(){
						public boolean onItemLongClick(AdapterView<?> parent, View view,
			                    final int position, long id){				
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom ));
							alertDialog.setTitle("Add Bookmark");
							alertDialog.setMessage("Do you want to add this bookmark?");
							alertDialog.setNegativeButton("Add", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									String bookmarkUrl = data.get(position).getUrl();
									String jobName = data.get(position).getName() + "\n("+ desc +")";
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
									datasource.close();
									dialog.dismiss();
							}
						});
							alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									dialog.dismiss();
								} });
							alertDialog.show();
							
							return true;				
						}
					});
				}
				return true;
			}
		});
		popup.show();
		
		//return true;
	}
	
	 
	 public class fetchJob extends AsyncTask<String, Void, ArrayList<JobData>>
	    {
		 @Override
		 protected ArrayList<JobData> doInBackground(String... args){
			 ArrayList<JobData> jobList = new ArrayList<JobData>();
		     
			 
				try {
					
					ServerParser http = new ServerParser();
					// perform conenction to server
		             JSONObject json = http.getJSONFromUrl(url,username,token);
		             JSONArray jobs;
		             jobs = json.getJSONArray("jobs");//get all the job from jenkins

		             for (int i = 0; i < jobs.length(); i++) {
		            	 JobData jobData = new JobData(); // store job data
		                 JSONObject job = (JSONObject) jobs.get(i);
		                 
		                 jobData.setName(job.getString("name"));
		                 jobData.setUrl(job.getString("url"));
		                 jobData.setColor(job.getString("color"));		                 
		                 jobList.add(jobData);
		                 if (jobData.getColor().equals("blue")){
		                	 success.add(jobData);
		                 }
		                 else { fail.add(jobData);	}
		                 
		             }
		   
				}  catch (Exception e) {
					// TODO Auto-generated catch block
					runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to connect server", Toast.LENGTH_SHORT).show();
						}
					});
				}
				 
		     return jobList;
		    }
		 
		 
		 protected void onPreExecute(){
			 super.onPreExecute();
			 pd.show();
		 }
		 
		 protected void onPostExecute(ArrayList<JobData> result){
			 My_project.this.data = result;
			 if (My_project.this.pd != null)
			 {
				 My_project.this.pd.dismiss();
				 ListView list = (ListView) findViewById(R.id.paramList);
				    adapter = new ProjectListArrayAdapter(getApplicationContext(),R.layout.project_list_item,data);
					list.setAdapter(adapter);
					list.setLongClickable(true);
					list.setOnItemClickListener(new OnItemClickListener(){
					
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(My_project.this,BuildStatus.class);
						  intent.putExtra("url",data.get(position).getUrl());
						  intent.putExtra("jobName", data.get(position).getName());
						  intent.putExtra("serverName", desc);
						  intent.putExtra("serverUrl",serverUrl);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  startActivity(intent);
					}
						
					});
					
					//for long click
					list.setOnItemLongClickListener(new OnItemLongClickListener(){
						public boolean onItemLongClick(AdapterView<?> parent, View view,
			                    final int position, long id){				
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom ));
							alertDialog.setTitle("Add Bookmark");
							alertDialog.setMessage("Do you want to add this bookmark?");
							alertDialog.setNegativeButton("Add", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									String bookmarkUrl = data.get(position).getUrl();
									String jobName = data.get(position).getName() + "\n("+ desc +")";
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
									datasource.close();
									dialog.dismiss();
							}
						});
							alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									dialog.dismiss();
								} });
							alertDialog.show();
							
							return true;				
						}
					});
			 }

		 }
		 }
	     
}
