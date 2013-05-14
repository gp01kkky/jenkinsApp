/* UserInformation.java
 * Creates a user information object 
 * author@KelvinKhoo
 */

package jenkinsapp.server.database;

public class UserInformation {

	private String name = "";
	private String url = "";
	private int lastActive ;
	private String project_name;
	private String project_url;
	
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
	public int getLastActive() {
		return lastActive;
	}
	public void setLastActive(int lastActive) {
		this.lastActive = lastActive;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getProject_url() {
		return project_url;
	}
	public void setProject_url(String project_url) {
		this.project_url = project_url;
	}
	
}
