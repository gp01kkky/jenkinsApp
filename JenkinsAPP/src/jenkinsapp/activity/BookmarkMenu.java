/* MainMenu.java
 * Start activity of the app to display the instances
 * Reads in previous saved credentials and display instances as a ListView
 * author@KelvinKhoo
 */

package jenkinsapp.activity;

import java.util.ArrayList;
import java.util.List;

import jenkinsapp.server.database.BookmarkData;
import jenkinsapp.server.database.BookmarkDataUtils;
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

public class BookmarkMenu extends Activity {
	private BookmarkDataUtils datasource;
	List<BookmarkData> values;
	ImageView image;
	instanceData instanceList;
	/*ArrayList<instanceData> instancesList;
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> hostnames = new ArrayList<String>();
	ArrayList<String> isHttps = new ArrayList<String>();*/
	ArrayList<String> jobservername = new ArrayList<String>();
	
	Activity activity = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookmark_menu);
		
		datasource = new BookmarkDataUtils(this);
		datasource.open();
		values = datasource.getAllBookmark();
		
		//instancesList = new ArrayList<instanceData>();
		ListView list = (ListView) findViewById(R.id.paramList);
		
		if (values.size() == 0){
			

		}
		
		
		for (int i=0; i<values.size(); i++){
			jobservername.add(values.get(i).getJobName());
		}

		
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		R.layout.image_list_item, R.id.jenkins_instances,jobservername);
		list.setAdapter(adapter);
		list.setLongClickable(true);

		list.setOnItemClickListener(new OnItemClickListener(){
		@Override	
		public void onItemClick(AdapterView<?> l, View v, int position, long id) 
		{
			  datasource.close();
			  Intent intent = new Intent();
			  intent.setClass(BookmarkMenu.this,BuildStatus.class);
			  intent.putExtra("url",values.get(position).getBookmarkUrl());
			  intent.putExtra("isHttps",values.get(position).getIsHttps());
			  intent.putExtra("jobname", values.get(position).getJobName());
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
						BookmarkData bookmarkdata = values.get(position);
						datasource.deleteBookmark(bookmarkdata);
						values.remove(position);
						jobservername.remove(position);
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
