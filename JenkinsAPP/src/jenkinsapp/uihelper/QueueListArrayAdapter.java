/* QueueListArrayAdapter.java
 * To display the list of queues and the status
 * author@KelvinKhoo
 * reference from http://developer.android.com/reference/android/widget/ListAdapter.html
 */

package jenkinsapp.uihelper;

import java.util.ArrayList;

import jenkinsapp.server.database.QueueData;
import kkky.jenkinsapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class QueueListArrayAdapter extends ArrayAdapter<QueueData> {

	 private Context context;
	 
	 private ImageView buildStatusIcon;
	 private TextView reason;
	 private TextView jobName;
	
	 
	 private ArrayList<QueueData> queueList = new ArrayList<QueueData>(); 
	 
	 public QueueListArrayAdapter(Context context, int textViewResourceId, ArrayList<QueueData> queueList) {
		super(context, textViewResourceId, queueList);
		this.context = context;
		this.queueList = queueList;
	}

	 public int getCount() {
	        return this.queueList.size();
	    }
	 
	 public QueueData getItem(int index) {
	        return this.queueList.get(index);
	    }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.queue_list_item, parent, false);
	        }
	 
	        // Get the build object
	        QueueData queue = getItem(position);
	         
	        // Get the image id from build_list_item.xml
	        buildStatusIcon = (ImageView) row.findViewById(R.id.status_icon);
	     
	        // get the TextView id from build_list_item.xml
	        
	        jobName = (TextView) row.findViewById(R.id.job_name);
	        reason = (TextView) row.findViewById(R.id.reason);
	       
	        // to set the string information for each build
	 	    jobName.setText(queue.getJobName());
	        reason.setText(queue.getReason());
	       
	        if(queue.getResult().equals("blue")) 
	        	buildStatusIcon.setImageResource(R.drawable.blue_ball);
	        else 
	        	buildStatusIcon.setImageResource(R.drawable.red_ball);
	         
	        return row;
	    }
	}
