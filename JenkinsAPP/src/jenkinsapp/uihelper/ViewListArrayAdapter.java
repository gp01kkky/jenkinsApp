package jenkinsapp.uihelper;

import java.util.ArrayList;

import jenkinsapp.server.database.JobData;
import jenkinsapp.server.database.ViewData;
import kkky.jenkinsapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewListArrayAdapter extends ArrayAdapter<ViewData>{
	 private Context context;
	 private ImageView nameIcon;
	 private TextView name;
	 private ArrayList<ViewData> viewList = new ArrayList<ViewData>(); 
	 
	 public ViewListArrayAdapter(Context context, int textViewResourceId, ArrayList<ViewData> viewList) {
		super(context, textViewResourceId, viewList);
		this.context = context;
		this.viewList = viewList;
	 }
	
	 public int getCount() {
	        return this.viewList.size();
	    }
	 
	 public ViewData getItem(int index) {
	        return this.viewList.get(index);
	    }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
		 View row = convertView;
	        if (row == null) {
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.project_list_item, parent, false);
	        }
	 
	        // Get the view object
	        ViewData v = getItem(position);
	         
	        // get the ImageView id from project_list_item.xml
	        nameIcon = (ImageView) row.findViewById(R.id.status_icon);
	         
	        // get the TextView id from project_list_item.xml
	        name = (TextView) row.findViewById(R.id.jobs_name);
	         
	 
	        //Set job Name
	        name.setText(v.getName());
	         
	        return row;
	    }
}
