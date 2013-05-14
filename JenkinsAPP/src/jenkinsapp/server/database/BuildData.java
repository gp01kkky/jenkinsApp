/*
 * class object to store build information
 * 
 * author@Kelvin Khoo
 */
package jenkinsapp.server.database;

public class BuildData {
	
	private int duration =0;
	private int number =0;
	private String builtOn = ""; //build on which server?
	private String date = "";
	private String time = "";
	private String result = "";
	private String url = "";
	
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
}
