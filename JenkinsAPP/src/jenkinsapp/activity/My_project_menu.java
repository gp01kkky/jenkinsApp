/* The project menu Activity
 * Displays the menu of icons for project navigation
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import kkky.jenkinsapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class My_project_menu extends Activity {
	String url = "";
	String serverUrl = "";
	String serverName = "";
	String jobName = "";
	String isHttps = "";
	String username = "";
	String token = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		isHttps = mode.getString("isHttps");
    		serverUrl = mode.getString("serverUrl");
    		serverName = mode.getString("serverName");
    		jobName = mode.getString("jobName");
    		username = mode.getString("username");
    		token = mode.getString("token");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
       
		setContentView(R.layout.activity_my_project_menu);
		 TextView textview =(TextView) findViewById(R.id.Server_title);
	     textview.setText("Server: "+ serverName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_project_menu, menu);
		return true;
	}
	
	public void onClickBuildHistory (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("url", url);
    	intent.putExtra("jobName", jobName);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, BuildHistory.class);
    	startActivity(intent);
    }
 
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("url", url);
    	intent.putExtra("jobName", jobName);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, BuildStatus.class);
    	startActivity(intent);
    }
	
	public void onClickPeople (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("serverUrl", serverUrl);
    	intent.putExtra("serverName", serverName);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, UserList.class);
    	startActivity(intent);
    }
	
	public void onClickBuildQueue (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("serverUrl", serverUrl);
    	intent.putExtra("serverName", serverName);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, BuildQueue.class);
    	startActivity(intent);
    }

}
