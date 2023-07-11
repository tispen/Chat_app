package chatapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chatapp.decoders.MessageDecoder;
import chatapp.encoders.ChatMessageEncoder;
import chatapp.encoders.UsersUpdateMessageEncoder;
import chatapp.messages.ChatMessage;
import chatapp.messages.Message;
import chatapp.messages.UsersUpdateMessage;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.inject.Inject;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chatroom",
		decoders = { MessageDecoder.class },
		encoders = { ChatMessageEncoder.class, UsersUpdateMessageEncoder.class })
public class ChatWebSocket {
	
    @Inject
    private Person lastConnectedPerson;
    @Resource(name="comp/DefaultManagedExecutorService")
    private ManagedExecutorService mes;
    
    @OnOpen
    public void onOpen(Session session) {
    	mes.submit(() -> {
    		try {
    			String userName = lastConnectedPerson.getName();
    			session.getUserProperties().put("username", userName);
    			Calendar calendar = Calendar.getInstance();
    			Thread.sleep(500);
    			String date = "[" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + 
    					calendar.get(Calendar.MINUTE) + ":" + 
    					calendar.get(Calendar.SECOND) + "]";
    			String msg = "has connected to the chat!";
    			ChatMessage chatMessage = new ChatMessage(userName, date, msg);
				sendToAll(session, chatMessage);
				UsersUpdateMessage usersUpdateMessage = new UsersUpdateMessage(getUserList(session));
				sendToAll(session, usersUpdateMessage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	});
    }
    
    
    @OnMessage
    public void onMessage(Session session, Message message) {
    	if(message instanceof ChatMessage) {
    		ChatMessage msg = (ChatMessage) message;
    		sendToAll(session, msg);
    	}
    }
    
    @OnClose
    public void onClose(Session session) {
		String userName = (String) session.getUserProperties().get("username");
		Calendar calendar = Calendar.getInstance();
		String date = "[" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + 
				calendar.get(Calendar.MINUTE) + ":" + 
				calendar.get(Calendar.SECOND) + "]";
		String msg = "has left the chat!";
		ChatMessage chatMessage = new ChatMessage(userName, date, msg);
		sendToAll(session, chatMessage);
		UsersUpdateMessage usersUpdateMessage = new UsersUpdateMessage(getUserList(session));
		sendToAll(session, usersUpdateMessage);
    }
    
    synchronized public void sendToAll(Session session, Message message) {
    	try {
    		for(Session s : session.getOpenSessions()) {
    			if(s.isOpen()) {
    				s.getBasicRemote().sendObject(message);
    			}
    		}
    	} catch(IOException | EncodeException e) {
    		e.printStackTrace();
    	}
    }
    
    public List<String> getUserList (Session session) {
    	List<String> users = new ArrayList<String>();
    	for(Session s: session.getOpenSessions()) {
    		if(s.isOpen())
    			users.add(s.getUserProperties().get("username").toString());
    	}
    	return users;
    }
}
