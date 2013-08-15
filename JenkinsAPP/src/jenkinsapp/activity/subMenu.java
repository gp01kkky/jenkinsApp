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

public class subMenu extends Activity {
	private static String url = "";
	//private static String serverUrl = "";
	private static String isHttps = "";
	private String desc = "";
	private String username = "";
	private String token = "";
	
	
	
//	String url = "";
//	String serverUrl = "";
//	String serverName = "";
//	String jobName = "";
//	String isHttps = "";
//	String username = "";
//	String token = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle mode = getIntent().getExtras();
    	if(mode !=null)
    	{
    		url = mode.getString("url");
    		desc = mode.getString("desc");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");
    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
       
		setContentView(R.layout.project_menu);
		 TextView textview =(TextView) findViewById(R.id.Server_title);
	     textview.setText("Server: "+ desc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_project_menu, menu);
		return true;
	}
	
	public void onClickBookmark (View Button){
		Intent intent = new Intent();
    	intent.setClass(this, BookmarkMenu.class);
    	startActivity(intent);
    }
 
	public void onClickViewJobs (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("url", url);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
    	intent.putExtra("desc", desc);
		intent.putExtra("token", token);
    	intent.setClass(this, My_project.class);
    	startActivity(intent);
    }
	
	public void onClickUser (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("serverUrl", url);
    	intent.putExtra("serverName", desc);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, UserList.class);
    	startActivity(intent);
    }
	
	public void onClickJobQueue (View Button){
    	Intent intent = new Intent();
    	intent.putExtra("serverUrl", url);
    	intent.putExtra("serverName", desc);
    	intent.putExtra("isHttps", isHttps);
    	intent.putExtra("username", username);
		  intent.putExtra("token", token);
    	intent.setClass(this, BuildQueue.class);
    	startActivity(intent);
    }

}
