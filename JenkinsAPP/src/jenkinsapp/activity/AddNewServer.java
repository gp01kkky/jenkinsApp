/* AddNewServer.java
 * 
 * Activity for adding a new instance (server url) and saving it
 * 
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

import kkky.jenkinsapp.R;



import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewServer extends Activity {
	
	private String hostName = null;
	private String description = null;
	Context context = this;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_server);
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
	//activated when user press the add button, save and store the user input 
	public void onClickAddButton (View Button) throws FileNotFoundException, IOException{
		EditText objDescription = (EditText) findViewById(R.id.enter_description);
		description = objDescription.getText().toString();
		
		EditText objHostName = (EditText) findViewById(R.id.enter_url);
		hostName = objHostName.getText().toString();
		String desc = "descdetails.khoo";
		String hosts = "hostdetails.khoo";
		if (!hostName.equals("") && !description.equals(""))
		{
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(desc, MODE_APPEND));
			out.write(description + "\n");
			out.close();
			
			OutputStreamWriter outw = new OutputStreamWriter(openFileOutput(hosts, MODE_APPEND));
			outw.write(hostName + "\n");
			outw.close();
			
			
			Intent intent = new Intent();
	    	intent.setClass(this, MainMenu.class);
	    	startActivity(intent);
	    	finish();
		}
		else {
			//if user didn't fill up the required field, we need to inform him
			Toast.makeText(getApplicationContext(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
		}
    
    }

}
