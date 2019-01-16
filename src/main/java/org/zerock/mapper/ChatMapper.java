package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.ChatVO;

public interface ChatMapper {
	public List<ChatVO> getChatList(int chatroomnum);
	public void insertChat(ChatVO vo);
}
