package chatapp.encoders;

import java.io.StringWriter;

import chatapp.messages.UsersUpdateMessage;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class UsersUpdateMessageEncoder implements Encoder.Text<UsersUpdateMessage> {

	@Override
	public String encode(UsersUpdateMessage usersUpdateMessage) throws EncodeException {
		StringWriter swriter = new StringWriter();
		try (JsonGenerator jsonGen = Json.createGenerator(swriter)) {
			jsonGen.writeStartObject()
				.write("type", "users")
				.writeStartArray("userlist");
			for(String user : usersUpdateMessage.getUserList())
				jsonGen.write(user);
			jsonGen.writeEnd().writeEnd();
		}
		return swriter.toString();
	}
}
