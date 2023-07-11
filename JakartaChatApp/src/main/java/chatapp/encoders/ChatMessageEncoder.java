package chatapp.encoders;

import java.io.StringWriter;

import chatapp.messages.ChatMessage;
import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {

	@Override
	public String encode(ChatMessage chatMessage) throws EncodeException {
		StringWriter swriter = new StringWriter();
		try (JsonGenerator jsonGen = Json.createGenerator(swriter)) {
			jsonGen.writeStartObject()
				.write("type", "chat")
				.write("username", chatMessage.getUserName())
				.write("date", chatMessage.getDate())
				.write("text", chatMessage.getMessage())
			.writeEnd();
		}
		return swriter.toString();
	}
}
