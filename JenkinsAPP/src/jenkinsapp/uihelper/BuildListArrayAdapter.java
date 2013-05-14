/* Customised ListArrayAdapter to display a list of build
 * information obtained from the server
 * The xml layout for this is build_list_item
 * 
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
import android.widget.ImageView;
import android.widget.TextView;


public class BuildListArrayAdapter extends ArrayAdapter<BuildData> {

	 private Context context;
	 
	 private ImageView buildStatusIcon;
	 private TextView buildNumber;
	 private TextView buildDate;
	 private TextView duration;
	 private TextView buildTime;
	 
	 private ArrayList<BuildData> buildList = new ArrayList<BuildData>(); 
	 
	 public BuildListArrayAdapter(Context context, int textViewResourceId, ArrayList<BuildData> buildList) {
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
	            row = inflater.inflate(R.layout.build_list_item, parent, false);
	        }
	 
	        // Get the build object
	        BuildData build = getItem(position);
	         
	        // Get the image id from build_list_item.xml
	        buildStatusIcon = (ImageView) row.findViewById(R.id.status_icon);
	     
	        // get the TextView id from build_list_item.xml
	        
	        buildNumber = (TextView) row.findViewById(R.id.build_number);
	        buildDate = (TextView) row.findViewById(R.id.build_date);
	        duration = (TextView) row.findViewById(R.id.build_duration);
	        buildTime = (TextView) row.findViewById(R.id.build_time);
	        
	        // to set the string information for each build
	 	    buildDate.setText("Date: " +build.getDate());
	        buildTime.setText("Time: "+build.getTime());
	        buildNumber.setText("Build: " + Integer.toString(build.getNumber()));
	        duration.setText("Took "+Integer.toString(build.getDuration()) + "ms on " + build.getBuiltOn());
	        if(build.getResult().equals("FAILURE")) 
	        buildStatusIcon.setImageResource(R.drawable.red_ball_icon);
	        else 
	        	buildStatusIcon.setImageResource(R.drawable.blue_ball_icon);
	         
	        return row;
	    }
	}
