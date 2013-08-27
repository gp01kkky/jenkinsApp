/*
 * Class object to store the server information
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

/**
 * Used to store Jenkins server details.
 * Username and token is optional parameter
 * <p>Sample server data:
 * <pre>
 * {@code
 * |id| Hostname |         url            | username |     token    |
 * | 1| Jenkins  | https://ci-jenkins.org | gp01kkky | 12313Asaaad2 |
 * }
 * @author Kelvin
 *
 */
public class ServerData {
	/**
	 * Id for each of the data stored
	 */
	private long id;
	
	private String description = null;
	private String url = null; //ip address
	private String userName = null;
	private String password = null;
	private String token = null;
	private String isHttps = null;
	private String port = null;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
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
	
	public String getIsHttps() {
		return isHttps;
	}
	public void setIsHttps(String isHttps) {
		this.isHttps = isHttps;
	}
	
	

}
