/*
 * This class fetch the last builds of a job from a given url
 * @author Kelvin Khoo
 */
package jenkinsapp.dataqueryserver;

import java.util.ArrayList;

import jenkinsapp.server.database.BuildData;

import org.json.JSONException;
import org.json.JSONObject;


public class FetchLastBuild {

	public ArrayList<BuildData> fetchLastBuild(String url)
    {
	 String lastBuildUrl = url.replaceFirst("http", "https")+"api/json?pretty=true";
     BuildData buildData = new BuildData();
 	 ArrayList<BuildData> buildList = new ArrayList<BuildData>();
	 
		try {
			
			 ServerParser http = new ServerParser();
             JSONObject json = http.getJSONFromUrl(lastBuildUrl,null,null);
             
             buildData.setBuiltOn(json.getString("builtOn"));
             buildData.setDuration(json.getInt("duration"));
             String[] id = json.getString("id").split("_");
             buildData.setDate(id[0]);
             buildData.setTime(id[1].replaceAll("-", ":"));
             buildData.setNumber(json.getInt("number"));
             buildData.setResult(json.getString("result"));
             buildData.setUrl(json.getString("url"));
             buildList.add(buildData);
   
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		 
     return buildList;
    }
}
