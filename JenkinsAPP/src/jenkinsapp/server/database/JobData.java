/*
 * Class object to store job information
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

public class JobData {
	
	private String name = null;
	private String url = null;
	private String color = null;
			
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
