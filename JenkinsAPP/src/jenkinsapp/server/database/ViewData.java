package jenkinsapp.server.database;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewData implements Parcelable {
	private String name;
	private String url;
	
	public ViewData(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		
		out.writeString(url);
		out.writeString(name);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ViewData> CREATOR = new Parcelable.Creator<ViewData>() {
        public ViewData createFromParcel(Parcel in) {
            return new ViewData(in);
        }

        public ViewData[] newArray(int size) {
            return new ViewData[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private ViewData(Parcel in) {
    
    	url = in.readString();
    	name = in.readString();
    }
}
