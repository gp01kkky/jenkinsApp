/*
 * object class to store the instances (server url, and description)
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

public class instanceData {
	
	private String description;
	private String hostName;
	private String isHttps;
	public instanceData()
	{
		
	}
	
	public instanceData(String description, String hostName,String isHttps)
	{
		this.description =description;
		this.hostName = hostName;
		this.isHttps = isHttps;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIsHttps() {
		return isHttps;
	}
	public void setIsHttps(String isHttps) {
		this.isHttps = isHttps;
	}
	
	
}
