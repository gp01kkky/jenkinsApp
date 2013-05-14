/* UserListArrayAdapter.java
 * To display the user information
 * author@KelvinKhoo
 */

package jenkinsapp.uihelper;

import java.util.ArrayList;

import jenkinsapp.dataqueryserver.CalcTime;
import jenkinsapp.server.database.BuildData;
import jenkinsapp.server.database.QueueData;
import jenkinsapp.server.database.UserInformation;
import kkky.jenkinsapp.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class UserListArrayAdapter extends ArrayAdapter<UserInformation> {

	 private Context context;
	 
	 private TextView name;
	 private TextView last_active;
	 private TextView project;
	 
     CalcTime CalcTime;
	 
	 private ArrayList<UserInformation> userList = new ArrayList<UserInformation>(); 
	 
	 public UserListArrayAdapter(Context context, int textViewResourceId, ArrayList<UserInformation> userList) {
		super(context, textViewResourceId, userList);
		this.context = context;
		this.userList = userList;
	}

	 public int getCount() {
	        return this.userList.size();
	    }
	 
	 public UserInformation getItem(int index) {
	        return this.userList.get(index);
	    }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        if (row == null) {
	            LayoutInflater inflater = (LayoutInflater) this.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            row = inflater.inflate(R.layout.user_list_item, parent, false);
	        }
	 
	        // Get the build object
	        UserInformation user = getItem(position);
	     
	        // get the TextView id from build_list_item.xml
	        
	        name = (TextView) row.findViewById(R.id.name);
	        last_active = (TextView) row.findViewById(R.id.last_active);

	        //Convert last active time 
	        int lastActive = user.getLastActive();
	        CalcTime = new CalcTime(lastActive); 
	       
	        // to set the string information for each build
	 	    name.setText(user.getName());
	        last_active.setText("Last Active " + CalcTime.convertTimeUser() + " on\n" + user.getProject_name());
	         
	        return row;
	    }
	}
