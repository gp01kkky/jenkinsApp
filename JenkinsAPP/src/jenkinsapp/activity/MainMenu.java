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
import java.util.List;

import jenkinsapp.server.database.DatabaseUtils;
import jenkinsapp.server.database.ServerData;
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
	private DatabaseUtils datasource;
	List<ServerData> values;
	ImageView image;
	instanceData instanceList;
	ArrayList<instanceData> instancesList;
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> hostnames = new ArrayList<String>();
	ArrayList<String> isHttps = new ArrayList<String>();
	
	
	Activity activity = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_menu);
		
		datasource = new DatabaseUtils(this);
		datasource.open();
		values = datasource.getAllServer();
		
		instancesList = new ArrayList<instanceData>();
		ListView list = (ListView) findViewById(R.id.paramList);
		
		if (values.size() == 0){
			datasource.createServer("Jenkins CI", "https://ci.jenkins-ci.org", "", "", "TRUE");
			values = datasource.getAllServer();

		}
		
		
		for (int i=0; i<values.size(); i++){
			descriptions.add(values.get(i).getDescription());
		}

		
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		R.layout.image_list_item, R.id.jenkins_instances,descriptions);
		list.setAdapter(adapter);
		list.setLongClickable(true);

		list.setOnItemClickListener(new OnItemClickListener(){
		@Override	
		public void onItemClick(AdapterView<?> l, View v, int position, long id) 
		{
			  datasource.close();
			  Intent intent = new Intent();
			  intent.setClass(MainMenu.this,subMenu.class);
			  intent.putExtra("url",values.get(position).getUrl());
			  intent.putExtra("isHttps",values.get(position).getIsHttps());
			  intent.putExtra("desc", values.get(position).getDescription());
			  intent.putExtra("username", values.get(position).getUserName());
			  intent.putExtra("token", values.get(position).getToken());
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
						ServerData serverdata = values.get(position);
						datasource.deleteServer(serverdata);
						values.remove(position);
						descriptions.remove(position);
						adapter.notifyDataSetChanged();
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
		datasource.close();
		Intent intent = new Intent();
    	intent.setClass(this, AddNewServer.class);
    	startActivity(intent);
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
