package org.zerock.service;

import java.util.List;

import org.zerock.domain.ChatCountVO;
import org.zerock.domain.ChatVO;

public interface ChatService {
	public List<ChatVO> getChatList(int chatroomnum);
	public void insertChat(ChatVO vo);
	public void updateChatCount(ChatCountVO vo);
	public void resetChatCount(ChatCountVO vo);
	public String selectChatCount(ChatCountVO vo);
}
