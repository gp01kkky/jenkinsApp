package jenkinsapp.server.database;

public class JobOverviewData {

	private String message;
	private String url;
	
	JobOverviewData(){
		
	}
	public JobOverviewData(String message,String url)
	{
		this.message=message;
		this.url=url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
