package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.ChatVO;
import org.zerock.mapper.ChatMapper;

import lombok.Setter;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Setter(onMethod_=@Autowired)
	private ChatMapper mapper;
	@Override
	public List<ChatVO> getChatList(int chatroomnum) {
		return mapper.getChatList(chatroomnum);
	}
	
	//소켓 통신시 대화를 db에 저장
	@Override
	public void insertChat(ChatVO vo) {
		mapper.insertChat(vo);
	}
	
}
