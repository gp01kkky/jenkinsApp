/*
 * This class fetch the url of the builds from the given job url
 * author@Kelvin Khoo
 */

package jenkinsapp.dataqueryserver;

import org.json.JSONException;
import org.json.JSONObject;

public class FetchLastBuildUrl
{	
	public String fetchLastBuildUrl(String url)
	{
		String buildUrl = url.replaceFirst("http", "https")+"api/json?pretty=true";
		String lastBuildUrl ="";
		try {
			ServerParser http = new ServerParser();
			JSONObject json = http.getJSONFromUrl(buildUrl);
			JSONObject lastBuild;
			lastBuild = json.getJSONObject("lastBuild");//store the job data
			lastBuildUrl = lastBuild.getString("url");
		} catch (JSONException e) {
		// TODO Auto-generated catch block
		}
	 
		return lastBuildUrl;
	}
}