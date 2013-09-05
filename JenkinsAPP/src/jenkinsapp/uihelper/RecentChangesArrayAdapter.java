/*
 * a customized array adapter to display a list of builds in the build history activity
 * author@Kelvin Khoo
 * reference from http://developer.android.com/reference/android/widget/ListAdapter.html
 */
package jenkinsapp.uihelper;

import java.util.ArrayList;

import jenkinsapp.server.database.BuildData;
import kkky.jenkinsapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class RecentChangesArrayAdapter extends ArrayAdapter<BuildData> {

	 private Context context;
	 private TextView buildNumber;
	 private TextView changes;
	 
	 private ArrayList<BuildData> buildList = new ArrayList<BuildData>(); 
	 private ArrayList<String> strChange = new ArrayList<String>();

	 public RecentChangesArrayAdapter(Context context, int textViewResourceId, ArrayList<BuildData> buildList) {
		super(context, textViewResourceId, buildList);
		this.context = context;
		this.buildList = buildList;
	}

	 public int getCount() {
	        return this.buildList.size();
	    }
	 
	 public BuildData getItem(int index) {
	        return this.buildList.get(index);
	    }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.changes_list_item, parent, false);
	        }
	 
	        // Get the build object
	        BuildData build = getItem(position);
	     
	        // get the TextView id from build_list_item.xml
	        
	        buildNumber = (TextView) row.findViewById(R.id.Build_Number);
	        changes = (TextView) row.findViewById(R.id.Changes);
	        
	        // to set the string information for each build
	        strChange = build.getChanges();
	        if(strChange.size()>0)
	        {
	            StringBuilder textChanges = new StringBuilder();

	        	for(int i=0;i<strChange.size();i++)
	        	{
	        		textChanges.append((i+1)+". " + strChange.get(i)+"\n");
	        	}
		        changes.setText(textChanges.toString());

	        }
	        else
	        {
	        	changes.setText("No changes");
	        }
	        buildNumber.setText("#"+Integer.toString(build.getNumber())+ " (" + build.getDate() +", " + build.getTime() +")");
	         
	        return row;
	    }
	}