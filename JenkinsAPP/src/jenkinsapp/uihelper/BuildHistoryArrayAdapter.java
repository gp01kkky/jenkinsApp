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


public class BuildHistoryArrayAdapter extends ArrayAdapter<BuildData> {

	 private Context context;
	 private TextView buildNumber;
	 private TextView buildState;
	 private TextView duration;
	 
	 private ArrayList<BuildData> buildList = new ArrayList<BuildData>(); 
	 
	 public BuildHistoryArrayAdapter(Context context, int textViewResourceId, ArrayList<BuildData> buildList) {
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
	            row = inflater.inflate(R.layout.build_list, parent, false);
	        }
	 
	        // Get the build object
	        BuildData build = getItem(position);
	     
	        // get the TextView id from build_list_item.xml
	        
	        buildNumber = (TextView) row.findViewById(R.id.build_number);
	        duration = (TextView) row.findViewById(R.id.build_duration);
	        buildState = (TextView) row.findViewById(R.id.build_state);
	        
	        // to set the string information for each build
	        buildNumber.setText(Integer.toString(build.getNumber()));
	        duration.setText(Integer.toString(build.getDuration()) + " ms");
	        buildState.setText(build.getResult());
	         
	        return row;
	    }
	}