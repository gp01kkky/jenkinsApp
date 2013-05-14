/* Calculate Time package
 * For converting time in milliseconds to hours, seconds days etc
 * author@KelvinKhoo
 */

package jenkinsapp.dataqueryserver;

public class CalcTime {
	
	private int milliseconds;
	private int seconds;
	private int minutes;
	private int day;
	private int hours;
	
	private static final int SECOND = 1000;
	private static final int MINUTE = 60*SECOND;
	private static final int HOUR = 60*MINUTE;
	private static final int DAY = 24*HOUR;
	
	public CalcTime(int milliseconds){
		this.milliseconds = milliseconds;
		
	}
	
	public String convertTimeUser(){
		convertTime();
		String time = "";
		
		if (this.day != 0){
			time += this.day + " days ";
		}
		
		if (this.hours != 0){
			time += this.hours + " hours ";
		}
		
		if (this.minutes != 0){
			time += this.minutes + " minutes ";
		}
		
		if (this.seconds != 0){
			time += this.seconds + " seconds";
		}
		
		return time;
	}
	
	public String convertTimeCom(){
		convertTime();
		String time = "";
		
		if (this.day != 0){
			time += this.day + " days ";
		}
		
		if (this.hours != 0){
			time += this.hours + " hours ";
		}
		
		if (this.minutes != 0){
			time += this.minutes + " minutes ";
		}
		
		if (this.seconds != 0){
			time += this.seconds + " seconds";
		}
		
		if (this.milliseconds != 0){
			time += this.milliseconds + " milliseconds";
		}
		
		return time;
	}
	
	public void convertTime(){
		if (this.milliseconds > DAY){
			this.day = this.milliseconds/DAY;
			this.milliseconds = this.milliseconds % DAY;
		}
		
		if (this.milliseconds > HOUR){
			this.hours = this.milliseconds/HOUR;
			this.milliseconds = this.milliseconds % HOUR;
		}
		
		if (this.milliseconds > MINUTE){
			this.minutes = this.milliseconds/MINUTE;
			this.milliseconds = this.milliseconds % MINUTE;
		}
		
		if (this.milliseconds > SECOND){
			this.seconds = this.milliseconds/SECOND;
			this.milliseconds = this.milliseconds % SECOND;
		}
		
	}
	
	public int getMilliseconds() {
		return milliseconds;
	}
	public int getSeconds() {
		return seconds;
	}
	public int getMinutes() {
		return minutes;
	}
	public int getDay() {
		return day;
	}
	public int getHours() {
		return hours;
	}

	

}
