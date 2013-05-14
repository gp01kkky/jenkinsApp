/* UserList.java
 * Create and display user lists and details
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.util.ArrayList;
import jenkinsapp.dataqueryserver.ServerParser;
import jenkinsapp.server.database.UserInformation;
import jenkinsapp.uihelper.UserListArrayAdapter;
import kkky.jenkinsapp.R;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserList extends Activity {
	Activity activity = this;
	Context context = this;
	private ProgressDialog pd = null;
	private UserListArrayAdapter adapter;
	private ArrayList<UserInformation> data;
	private static String url;
	private String serverName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("serverUrl")+"/asynchPeople/api/json?pretty=true";
    		serverName = mode.getString("serverName");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_list);
		TextView textview =(TextView) findViewById(R.id.Project_title);
	    textview.setText("User: "+ serverName);
		
		pd = new ProgressDialog(activity, R.style.popupStyle);
		pd.setMessage("Fetching user information...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		fetchUser fetchUser = new fetchUser();
		fetchUser.execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list, menu);
		return true;
	}
	
	
	public class fetchUser extends AsyncTask<String, Void, ArrayList<UserInformation>>
    {
	 @Override
	 protected ArrayList<UserInformation> doInBackground(String... args){
		 ArrayList<UserInformation> userList = new ArrayList<UserInformation>();
	     
		 
			try {
				
				ServerParser http = new ServerParser();
				
	             JSONObject json = http.getJSONFromUrl(url);
	             
	             JSONArray users;
	             users = json.getJSONArray("users");//store the job data
	             
	             for (int i = 0; i < users.length(); i++) {
	            	 UserInformation userData = new UserInformation(); // store job data
	                 JSONObject user = (JSONObject) users.get(i);
	                 JSONObject project = (JSONObject) user.get("project");
	                 JSONObject details = (JSONObject) user.get("user");
	                 
	                 userData.setLastActive(user.getInt("lastChange"));
	                 userData.setProject_name(project.getString("name"));
	                 userData.setProject_url(project.getString("url"));
	                 userData.setName(details.getString("fullName"));
	                 userData.setUrl(details.getString("absoluteUrl"));
	                 userList.add(userData);	                 
	             }
	   
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				runOnUiThread(new Runnable(){
					public void run(){
				    	Toast.makeText(context, "Failed to contact server", Toast.LENGTH_SHORT).show();
					}
				});
			}
			 
	     return userList;
	    }
	 
	 
	 protected void onPreExecute(){
		 super.onPreExecute();
		 pd.show();
	 }
	 
	 protected void onPostExecute(ArrayList<UserInformation> result){
		 UserList.this.data = result;
		 if (UserList.this.pd != null)
		 {
			 UserList.this.pd.dismiss();
			 ListView list = (ListView) findViewById(R.id.userList);
			 adapter = new UserListArrayAdapter(context,R.layout.user_list_item,data);
			 list.setAdapter(adapter);
			 list.setOnItemClickListener(new OnItemClickListener(){
			 @Override	
				public void onItemClick(AdapterView<?> l, View v, int position, long id) 
				{
					  Intent intent = new Intent();
					  intent.setClass(UserList.this,BuildStatus.class);
					  intent.putExtra("url",data.get(position).getProject_url());
					  startActivity(intent);
				}		
				});
		 }
	 }
	 }
}

