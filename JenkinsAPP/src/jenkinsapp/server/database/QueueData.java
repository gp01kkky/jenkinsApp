/* 
 * class object to store all the build queue 
 * @author Kelvin Khoo
 */
package jenkinsapp.server.database;

public class QueueData {
	
	private String jobName = "";
	private String url ="";
	private String reason ="";
	private String result ="";
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
