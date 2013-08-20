package jenkinsapp.server.database;

public class BookmarkData {
	
	private String bookmarkUrl;
	private String jobName;
	private String serverName;
	private String isHttps;
	private String userName;
	private String token;
	
	public BookmarkData(){
		
	}
	
	public BookmarkData(String hostname, String bookmarkUrl, String userName, String token, String isHttps){
		this.jobName = hostname;
		this.bookmarkUrl = bookmarkUrl;
		this.userName = userName;
		this.token = token;
		this.isHttps = isHttps;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getIsHttps() {
		return isHttps;
	}
	public void setIsHttps(String isHttps) {
		this.isHttps = isHttps;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private long id;
	
	public String getBookmarkUrl() {
		return bookmarkUrl;
	}
	public void setBookmarkUrl(String bookmarkUrl) {
		this.bookmarkUrl = bookmarkUrl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
