package chatapp.messages;

import java.util.List;

public class UsersUpdateMessage extends Message {

	private List<String> userList;
	
	public UsersUpdateMessage(List<String> userList) {
		this.userList = userList;
	}
	
	public List<String> getUserList() {
		return userList;
	}
	
	@Override
	public String toString() {
		return "[UsersUpdateMessage]" + userList.toString();
	}
}
