/*
 * class object to store build information
 * 
 * author@Kelvin Khoo
 */
package jenkinsapp.server.database;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class BuildData implements Parcelable{
	
	private int duration =0;
	private int number =0;
	private String builtOn; //build on which server?
	private String date;
	private String time;
	private String result;
	private String url;
	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private ArrayList<String> changes = new ArrayList<String>();
	
	public BuildData(){
		
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getBuiltOn() {
		return builtOn;
	}
	public void setBuiltOn(String builtOn) {
		this.builtOn = builtOn;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getStatus()
	{
		if(result.equals("SUCCESS"))
		{
			return 100;
		}
		else
			return 0;
	}
	public ArrayList<String> getChanges() {
		return changes;
	}
	public void setChanges(ArrayList<String> changes) {
		this.changes = changes;
	}
	public void addChanges(String change)
	{
		changes.add(change);
	}
	
	
	/* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
	@Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeString(builtOn);
		out.writeString(date);
		out.writeString(time);
		out.writeString(result);
		out.writeString(url);
		out.writeSerializable(changes);
		out.writeInt(duration);
		out.writeInt(number);
		out.writeString(message);
	     // out.writeTypedList(typed array);

		
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BuildData> CREATOR = new Parcelable.Creator<BuildData>() {
        public BuildData createFromParcel(Parcel in) {
            return new BuildData(in);
        }

        public BuildData[] newArray(int size) {
            return new BuildData[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private BuildData(Parcel in) {
    	
    
        /*
         * Note the order of reading must be the same as writing it.
         */
		builtOn = in.readString();
    	date = in.readString();
     	time = in.readString();
    	result = in.readString();  
    	url = in.readString();
    	changes = (ArrayList<String>) in.readSerializable();
    	duration = in.readInt();
    	number = in.readInt();
    	message = in.readString();
    	
    	  //in.readTypedList(the_name_of_the_typed_array_created, theobjectclass.Creator); for passing typed arraylist
   
    }
    
}
