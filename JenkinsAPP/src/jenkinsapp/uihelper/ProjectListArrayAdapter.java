/* Customised ListArrayAdapter to display a list of build
 * information obtained from the server
 * The xml layout for this is project_list_item
 * 
 * With the help of the given example from the following site: 
 * http://www.ezzylearning.com/tutorial.aspx?tid=1763429
 *
 * author@Kelvin Khoo 
 */
package jenkinsapp.uihelper;

import java.util.ArrayList;

import jenkinsapp.server.database.JobData;
import kkky.jenkinsapp.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectListArrayAdapter extends ArrayAdapter<JobData> {

	 private Context context;
	 private ImageView jobStatusIcon;
	 private TextView jobName;
	 private ArrayList<JobData> jobList = new ArrayList<JobData>(); 
	 
	 public ProjectListArrayAdapter(Context context, int textViewResourceId, ArrayList<JobData> jobList) {
		super(context, textViewResourceId, jobList);
		this.context = context;
		this.jobList = jobList;
	 }
	
	 public int getCount() {
	        return this.jobList.size();
	    }
	 
	 public JobData getItem(int index) {
	        return this.jobList.get(index);
	    }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	           
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.project_list_item, parent, false);
	        }
	 
	        // Get the job object
	        JobData job = getItem(position);
	         
	        // get the ImageView id from project_list_item.xml
	        jobStatusIcon = (ImageView) row.findViewById(R.id.status_icon);
	         
	        // get the TextView id from project_list_item.xml
	        jobName = (TextView) row.findViewById(R.id.jobs_name);
	         
	 
	        //Set job Name
	        jobName.setText(job.getName());
	        //ImageView myImageView = (ImageView) findViewById(R.id.blinkingView01); 
	        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in);
	        Animation myFadeOutAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_out);
	
	        //Set different icon for different status
	        if(job.getColor().equals("blue")) 
	        {
	        	jobStatusIcon.setImageResource(R.drawable.blue_ball);
	        }
	        else if (job.getColor().equals("blue_anime"))
	        {
	        	jobStatusIcon.setImageResource(R.drawable.blue_ball);
	        	jobStatusIcon.startAnimation(myFadeInAnimation);
	        	jobStatusIcon.startAnimation(myFadeOutAnimation);
	        }
	        else if(job.getColor().equals("red"))
	        {
	        	jobStatusIcon.setImageResource(R.drawable.red_ball);
	        }
	        else if(job.getColor().equals("red_anime"))
	        {
	        	jobStatusIcon.setImageResource(R.drawable.red_ball);
	        jobStatusIcon.startAnimation(myFadeInAnimation);
        	jobStatusIcon.startAnimation(myFadeOutAnimation);
	        }
	        else if(job.getColor().equals("yellow"))
	        {
	        	jobStatusIcon.setImageResource(R.drawable.yellow_ball);
	        }
	        else if(job.getColor().equals("yellow_anime"))
	        {
	        	jobStatusIcon.setImageResource(R.drawable.yellow_ball);
	        	jobStatusIcon.startAnimation(myFadeInAnimation);
	        	jobStatusIcon.startAnimation(myFadeOutAnimation);
	        }
	        else
	        {
	        	jobStatusIcon.setImageResource(R.drawable.grey_ball);
	        }
	        return row;
	    }
	}
