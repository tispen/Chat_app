package chatapp.messages;

public class ChatMessage extends Message {
	
	private String userName;
	private String date;
	private String message;
	
	public ChatMessage(String userName, String date, String message) {
		this.userName = userName;
		this.date = date;
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "[ChatMessage] " + userName + "-" + date + "-" + message; 
	}
}