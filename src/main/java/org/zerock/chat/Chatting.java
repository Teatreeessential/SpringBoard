package org.zerock.chat;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.zerock.domain.ChatVO;
import org.zerock.service.ChatService;
import org.zerock.util.TranDate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Setter;
import lombok.extern.log4j.Log4j;


@Log4j
public class Chatting extends TextWebSocketHandler {

	private static Map<String,WebSocketSession> sessionMap = new HashMap<String,WebSocketSession>();
	@Setter(onMethod__ = @Autowired)
	private ChatService service;
	private JsonParser jp = new JsonParser();

	// 클라이언트와 연결 이후에 실행되는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionMap.put(session.getPrincipal().getName(),session);
		
	}

	// 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		JsonObject object = (JsonObject) jp.parse(message.getPayload());
		ChatVO vo = new ChatVO();
		vo.setChat_room_num(object.get("chat_room_num").getAsInt());
		vo.setMessage(object.get("message").getAsString());
		vo.setUserid(object.get("userid").getAsString());
		vo.setMessage_date(object.get("message_date").getAsString());
		// 해당 대화를 db에 저장 date값은 db내 sysdate로 default;
		service.insertChat(vo);
		// 현재 서버와 연결된 소켓을 돌면서 상대방아이디를 찾는다.		
		sessionMap.get(object.get("opponent_userid").getAsString()).sendMessage(message);
		
		
		
		
		
	}

	// 클라이언트와 연결을 끊었을 때 실행되는 메소드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getPrincipal().getName());
		log.info("연결 끊김");
	}
}
