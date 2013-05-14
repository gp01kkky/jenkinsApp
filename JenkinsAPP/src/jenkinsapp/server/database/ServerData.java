/*
 * Class object to store the server information
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

public class ServerData {
	
	private String description = null;
	private String url = null; //ip address
	private String userName = null;
	private String password = null;
	private String token = null;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	

}
