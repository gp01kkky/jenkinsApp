/*
 * object class to store the instances (server url, and description)
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

public class instanceData {
	
	private String description;
	private String hostName;
	public instanceData()
	{
		
	}
	
	public instanceData(String description, String hostName)
	{
		this.description =description;
		this.hostName = hostName;
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
	
	
}
