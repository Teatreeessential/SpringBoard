package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.ChatCountVO;
import org.zerock.domain.ChatVO;

public interface ChatMapper {
	public List<ChatVO> getChatList(int chatroomnum);
	public void insertChat(ChatVO vo);
	public boolean addChatCount(ChatCountVO vo);
	public void updateChatCount(ChatCountVO vo);
	public void resetChatCount(ChatCountVO vo);
	public int selectChatCount(ChatCountVO vo);
}
