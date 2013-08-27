/** AddNewServer.java
 * 
 * Activity for adding a new instance (server url) and saving it
 * 
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import jenkinsapp.server.database.DatabaseUtils;
import jenkinsapp.server.database.ServerData;
import kkky.jenkinsapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewServer extends Activity {
	private long id = -1;
	private String hostName = null;
	private String description = null;
	private String isHttps = "FALSE";
	private String token = null;
	private String username = null;
	private String port = null;
	Context context = this;
	private DatabaseUtils datasource = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_server);
		
		datasource = new DatabaseUtils(this);
		datasource.open();
		Bundle mode = getIntent().getExtras();
		
    	if(mode !=null)
    	{	//https://ci.jenkins-ci.org/view/All/api/json?pretty=true
    		
    		hostName = mode.getString("url");
    		description = mode.getString("desc");
    		isHttps = mode.getString("isHttps");
    		username = mode.getString("username");
    		token = mode.getString("token");
    		id = mode.getLong("id");
    		port=mode.getString("port");
    	}
    	if(isHttps.equals("TRUE"))
    	{
    		CheckBox cb1 = (CheckBox)findViewById(R.id.checkbox_https);
    		cb1.setChecked(true);
    	}
    	else
    	{
    		CheckBox cb1 = (CheckBox)findViewById(R.id.checkbox_https);
    		cb1.setChecked(false);
    	}
    	
    	EditText objDescription = (EditText) findViewById(R.id.enter_description);
    	objDescription.setText(description);
		
		EditText objHostName = (EditText) findViewById(R.id.enter_url);
		objHostName.setText(hostName);
		
		EditText objuser = (EditText) findViewById(R.id.enter_username);
		objuser.setText(username);		
		
		EditText objtoken = (EditText) findViewById(R.id.enter_token);
		objtoken.setText(token);
		
		EditText objport = (EditText) findViewById(R.id.enter_port);
		objport.setText(port);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_new_server, menu);
		return true;
	}
	// go back to the previous activity
	public void onClickBackButton (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, MainMenu.class);
    	startActivity(intent);
    	finish();
    }
	
	public void onClickQuestionMark(View Button){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("API Token");
		//LayoutInflater.from(context).inflate(R.layout.question_mark, null);
		//ViewGroup parent = (ViewGroup)findViewById(R.id.imageView1); 
		//LayoutInflater inflater = getLayoutInflater();
		//View layout = inflater.inflate(R.layout.question_mark, null);
		//builder.setView(layout);
		builder.setMessage("To find API token, login to Jenkins server via webpage, click on your username, then click configure in the left hand panel. Click show API token and enter the token into the field below.");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	public void onCheckboxClicked(View checkbox) {
	    // Is the view now checked?
		
	    boolean checked =((CheckBox) checkbox).isChecked();
	    // Check which checkbox was clicked
	    switch(checkbox.getId()) {
	        case R.id.checkbox_https:
	        	if(checked)
	        	{ 
		            isHttps = "TRUE";
	        	}
	        	else
	        	{
	        		isHttps = "FALSE";
	        	}     
	            break;	      
	    }
	}
	//activated when user press the add button, save and store the user input 
	public void onClickAddButton (View Button) throws FileNotFoundException, IOException{		
		EditText objDescription = (EditText) findViewById(R.id.enter_description);
		description = objDescription.getText().toString();
		
		EditText objHostName = (EditText) findViewById(R.id.enter_url);
		hostName = objHostName.getText().toString();
		
		EditText objuser = (EditText) findViewById(R.id.enter_username);
		username = objuser.getText().toString();
		
		EditText objtoken = (EditText) findViewById(R.id.enter_token);
		token = objtoken.getText().toString().toLowerCase();
		
		EditText objport = (EditText) findViewById(R.id.enter_port);
		port = objport.getText().toString();
		
		
		
		if (!hostName.equals("") && !description.equals(""))
		{
			//if the user didnt key in http, then the http is automatically appended
			if (!hostName.substring(0, 4).equals("http")){
				hostName = "http://" + hostName;
			}
			
			if (hostName.substring(0,5).equals("https") && isHttps == "FALSE"){
				isHttps = "TRUE";
			}
			
			if(hostName.charAt(hostName.length()-1)=='/')
			{
				String newname = hostName.substring(0,hostName.length()-2);
				hostName = newname;
			}
			
		
			if(id!=-1)
			{
			datasource.updateServer(id,description, hostName, username, token, isHttps,port);
			datasource.close();
			}
			else
			{
			datasource.createServer(description, hostName, username, token, isHttps,port);
			datasource.close();
			}
			Intent intent = new Intent();
	    	intent.setClass(this, MainMenu.class);
	    	startActivity(intent);
	    	finish();
		}
		else {
			//if user didn't fill up the required field, we need to inform him
			Toast.makeText(getApplicationContext(), "Please fill the required fields (Host name and URL)", Toast.LENGTH_SHORT).show();
		}
    
    }
	@Override 
	protected void onResume(){
		datasource.open();
		super.onResume();
	}
	@Override
	protected void onPause(){
		datasource.open();
		super.onPause();
	}

}
