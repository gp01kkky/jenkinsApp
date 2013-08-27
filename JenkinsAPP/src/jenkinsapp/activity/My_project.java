/* My_project.java
 * Draws data from the server by passing in the url in an asynctask
 * Displays the list of jobs in a ListView
 * author@KelvinKhoo
 * 
 */

package jenkinsapp.activity;


import java.util.ArrayList;

import jenkinsapp.activity.TerminalOutput.fetchConsole;
import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.BookmarkData;
import jenkinsapp.server.database.BookmarkDataUtils;
import jenkinsapp.server.database.DatabaseUtils;
import jenkinsapp.server.database.JobData;
import jenkinsapp.server.database.ServerData;
import jenkinsapp.server.database.ViewData;
import jenkinsapp.uihelper.ProjectListArrayAdapter;
import jenkinsapp.uihelper.ViewListArrayAdapter;
import kkky.jenkinsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


@SuppressLint("NewApi")
public class My_project extends Activity {
	private BookmarkDataUtils datasource;
	JSONObject json;
	JSONArray jobs = null;
	Activity activity = this;
	Context context = this;
	// url to make request
	private String currentState = "Connection Failed";
	private String url = "";
	private String serverUrl = "";
	private String isHttps = "";
	private ProgressDialog pd = null;
	private ArrayList<JobData> data;
	private ArrayList<JobData> success = new ArrayList<JobData>();
	private ArrayList<JobData> fail = new ArrayList<JobData>();
	private ArrayList<ViewData> viewList = new ArrayList<ViewData>();
	Dialog listDialog;
	ProjectListArrayAdapter adapter;
	private String desc = "";
	private String username = "";
	private String token = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		Bundle mode = getIntent().getExtras();
		
    	if(mode !=null)
    	{	//https://ci.jenkins-ci.org/view/All/api/json?pretty=true
    		
    		url = mode.getString("url");
    		if(url.charAt(url.length()-1)=='/')
    			{
    				String newUrl = url.substring(0,url.length()-1);
    				//String newUrl = url.replace(url.substring(url.length()-1), "");
    				url = newUrl;
    			}
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

	public void onClickRefreshButton(View view){
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
	
	 public void showdialog()
	    {
	        listDialog = new Dialog(this, android.R.style.Theme_Translucent);
	         listDialog.setCancelable(true);
	        //listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        //listDialog.setTitle("Select Item");
	         LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         View v = li.inflate(R.layout.views_menu, null, false);
	         listDialog.setContentView(v);
	         //there are a lot of settings, for dialog, check them all out!
	 
	         ListView list1 = (ListView) listDialog.findViewById(R.id.viewsList);
	         ViewListArrayAdapter adapter = new ViewListArrayAdapter(getApplicationContext(),R.layout.project_list_item,viewList);
			 list1.setAdapter(adapter);	        
	         listDialog.setCancelable(true);
	         list1.setOnItemClickListener(new OnItemClickListener(){
					
					@Override	
					public void onItemClick(AdapterView<?> l, View v, int position, long id) 
					{
						  Intent intent = new Intent();
						  intent.setClass(My_project.this,My_project_without_views.class);
						  intent.putExtra("url",viewList.get(position).getUrl());
						  //intent.putExtra("jobName", viewList.get(position).getName());
						  //intent.putExtra("serverName", viewList.get(position).getName());
						  intent.putExtra("desc", desc);
						  intent.putExtra("serverUrl",url);
						  intent.putExtra("isHttps", isHttps);
						  intent.putExtra("username", username);
						  intent.putExtra("token", token);
						  intent.putParcelableArrayListExtra("viewData", viewList);
						  startActivity(intent);
						  finish();
					}
						
					});
			 
			 
	         listDialog.show();
	    }
	public void onSettingsButtonClick(View Button) {
		
		showdialog();
		
//		LayoutInflater layoutInflater 
//	     = (LayoutInflater)getBaseContext()
//	      .getSystemService(LAYOUT_INFLATER_SERVICE);  

//	     //final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT); 
//	     final PopupWindow popupWindow = new PopupWindow(popupView, 800, 970); 
//	    popupWindow.showAtLocation(Button, Gravity.CENTER, 0, 0);
	
/*
		
		PopupMenu popup = new PopupMenu(context, Button);
		popup.getMenuInflater().inflate(viewList, popup.getMenu());
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
									//if bookmark doesn't exist
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
									//datasource.close();
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
									//datasource.close();
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
									BookmarkData newBookmark = new BookmarkData(jobName, bookmarkUrl, username, token, isHttps);				
									datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
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
		*/
		//return true;
	}
	
	 
	 public class fetchJob extends AsyncTask<String, Void, ArrayList<JobData>>
	    {
		 @Override
		 protected ArrayList<JobData> doInBackground(String... args){
			 ArrayList<JobData> jobList = new ArrayList<JobData>();
		     String buildUrl;
			 if(isHttps.equals("TRUE"))
			 	{
				 String sub = url.substring(0, 5);
				 	if (!sub.equals("https")){
				 		buildUrl = url.replaceFirst("http", "https")+"/api/json";
				 	}
				 	else {
				 		buildUrl = url + "/api/json";
				 	}
			 	}
			 	else
			 		buildUrl = url + "/api/json";
			 
				try {
					
					ServerParser http = new ServerParser();
					// perform conenction to server
		             json = http.getJSONFromUrl(buildUrl,username,token);  
		             
				}  catch (Exception e) {
					// TODO Auto-generated catch block
					runOnUiThread(new Runnable(){
						public void run(){
					    	Toast.makeText(context, "Failed to connect server", Toast.LENGTH_SHORT).show();
						}
					});
				     return jobList;

				}
				
				
	             JSONArray views = new JSONArray();
	             try {
	            	JSONObject currentView = json.getJSONObject("primaryView");
		            currentState = currentView.getString("name");
					views = json.getJSONArray("views");
					for (int i = 0; i < views.length(); i++) {
		            	 ViewData viewData = new ViewData(); // store job data
		                 JSONObject view = (JSONObject) views.get(i);
		                 viewData.setName(view.getString("name"));
		                 viewData.setUrl(view.getString("url"));
		                 viewList.add(viewData);
		             }
				 
				} catch (Exception e) {
					try {
						currentState = json.getString("name");
					} catch (Exception e1) {
						runOnUiThread(new Runnable(){
							public void run(){
						    	Toast.makeText(context, "Unable to retrieve data", Toast.LENGTH_SHORT).show();
							}
						});		
					     return jobList;

					}

				}   
	             
	             try{
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
				 }catch (Exception e)
				 {
					// TODO Auto-generated catch block
						runOnUiThread(new Runnable(){
							public void run(){
						    	Toast.makeText(context, "Unable to retrieve data", Toast.LENGTH_SHORT).show();
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
			 TextView textview1 = (TextView) findViewById(R.id.Views);
			 textview1.setText(currentState);
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
									if (!datasource.bookmarkExists(new BookmarkData(jobName, bookmarkUrl, username, token, isHttps))){
										datasource.createBookmark(jobName, bookmarkUrl, username, token, isHttps);
										dialog.dismiss();
								    	Toast.makeText(context, "Bookmark added", Toast.LENGTH_SHORT).show();

									}
									else{
										dialog.dismiss();
								    	Toast.makeText(context, "Bookmark already existed", Toast.LENGTH_SHORT).show();
									}

									//datasource.close();
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
