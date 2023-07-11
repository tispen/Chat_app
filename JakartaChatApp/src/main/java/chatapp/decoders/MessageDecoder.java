package chatapp.decoders;


import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import chatapp.messages.ChatMessage;
import chatapp.messages.Message;
import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<Message>{
	
	private Map<String, String> messageMap;

	@Override
	public Message decode(String string) throws DecodeException {
		Message msg = null;
		if(willDecode(string)) {
			switch(messageMap.get("type")) {
			case "chat":
				msg = new ChatMessage(messageMap.get("username"),
						messageMap.get("date"),
						messageMap.get("text"));
				break;
			}
		} else {
			throw new DecodeException(string, "[Message] Can't decode");
		}
		return msg;
	}

	@Override
	public boolean willDecode(String string) {
		boolean decodes = false;
		messageMap = new HashMap<>();
		JsonParser parser = Json.createParser(new StringReader(string));
		while(parser.hasNext()) {
			if(parser.next() == JsonParser.Event.KEY_NAME) {
				String key = parser.getString();
				parser.next();
				String value = parser.getString();
				messageMap.put(key, value);
			}
		}
		
		Set<String> keys = messageMap.keySet();
		if(keys.contains("type")) {
			switch(messageMap.get("type")) {
			case "chat":
				if(keys.contains("username") && keys.contains("date") && keys.contains("text"))
					decodes = true;
				break;
			}
		}
		return decodes;
	}

}
