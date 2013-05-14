/* MainMenu.java
 * Start activity of the app to display the instances
 * Reads in previous saved credentials and display instances as a ListView
 * author@KelvinKhoo
 */

package jenkinsapp.activity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import jenkinsapp.server.database.instanceData;
import kkky.jenkinsapp.R;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainMenu extends Activity {
	ImageView image;
	instanceData instanceList;
	ArrayList<instanceData> instancesList;
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> hostnames = new ArrayList<String>();
	
	String desc = "descdetails.khoo";
	String hosts = "hostdetails.khoo";
	
	Activity activity = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_menu);
		
		instancesList = new ArrayList<instanceData>();
		ListView list = (ListView) findViewById(R.id.paramList);		
		//fetch the server url from the file
		try {
			FileInputStream in = openFileInput(hosts);
			if (in != null){
				InputStreamReader tmp = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder buf = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null){
					buf.append(line + "\n");
				}
				
				in.close();
				
				String buffer = buf.toString();
				String bufferArray[] = buffer.split("\n");
				for (int i=0; i<bufferArray.length; i++){
					hostnames.add(bufferArray[i]);				
				}
			}
			
		}catch(Throwable t) {
			//Create a file to store server url if it does not exist
			try {				
				OutputStreamWriter out = new OutputStreamWriter(openFileOutput(hosts, 0));
				out.write("https://ci.jenkins-ci.org" + "\n");
				out.close();
				// add a default server url for demo purposes
				hostnames.add("https://ci.jenkins-ci.org");			
			} catch (Throwable d){
				Toast.makeText(getApplicationContext(), "Error generating a file", Toast.LENGTH_SHORT).show();
			}
		}
		
		try {
			FileInputStream in = openFileInput(desc);
			if (in != null){
				InputStreamReader tmp = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder buf = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null){
					buf.append(line + "\n");
				}
				in.close();
				String buffer = buf.toString();
				String bufferArray[] = buffer.split("\n");
				for (int i=0; i<bufferArray.length; i++){
					descriptions.add(bufferArray[i]);				
				}
				
			}
		}catch(Throwable t) {
			//If the File doesnt exist generate a new file and add the default server 
			try {
				
				OutputStreamWriter out = new OutputStreamWriter(openFileOutput(desc, 0));
				out.write("CI Server" + "\n");
				out.close();
				//default server name added for demo purposes
				descriptions.add("CI Server");
				
			} catch (Throwable d){
				Toast.makeText(getApplicationContext(), "Error generating a file", Toast.LENGTH_SHORT).show();
			}
		}	
		
		for (int i=0; i<descriptions.size(); i++){
			instanceData instance = new instanceData(descriptions.get(i), hostnames.get(i));
			instancesList.add(instance);
		}

		
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		R.layout.image_list_item, R.id.jenkins_instances,descriptions);
		list.setAdapter(adapter);
		list.setLongClickable(true);

		list.setOnItemClickListener(new OnItemClickListener(){
		@Override	
		public void onItemClick(AdapterView<?> l, View v, int position, long id) 
		{

			  Intent intent = new Intent();
			  intent.setClass(MainMenu.this,My_project.class);
			  intent.putExtra("url",instancesList.get(position).getHostName());
			  intent.putExtra("desc", descriptions.get(position));
			  startActivity(intent);		
		}
		
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> parent, View view,
                    final int position, long id){				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom ));
				alertDialog.setTitle("Delete item");
				alertDialog.setMessage("Are you sure you want to delete this instance?");
				alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						descriptions.remove(position);
						hostnames.remove(position);
						adapter.notifyDataSetChanged();
						
						//Write to file again
						try{
						OutputStreamWriter out = new OutputStreamWriter(openFileOutput(desc, 0));
						for (String description:descriptions){
							out.write(description + "\n");
						}
						out.close();
						} catch(Throwable t){
							
						}
						
						try {
						OutputStreamWriter outw = new OutputStreamWriter(openFileOutput(hosts, 0));
						for (String hostName : hostnames){
							outw.write(hostName + "\n");
						}
						outw.close();
						} catch(Throwable d){
							
						}

						dialog.dismiss();
				}
			});
				alertDialog.setPositiveButton("Keep", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					} });
				alertDialog.show();
				
				return true;				
			}
		});
	}
	public void onClickAddButton (View Button) 
	{
		Intent intent = new Intent();
    	intent.setClass(this, AddNewServer.class);
    	startActivity(intent);
	}
}
