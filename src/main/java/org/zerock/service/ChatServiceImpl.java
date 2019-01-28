package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.ChatCountVO;
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
	
	//채팅창 count 증가 관련 메서드 유저가 클릭했을 때 0 채팅을 받을 때 1씩증가 조회하여 표기
	@Override
	public void updateChatCount(ChatCountVO vo) {
		mapper.updateChatCount(vo);
	}

	@Override
	public void resetChatCount(ChatCountVO vo) {
		mapper.resetChatCount(vo);
	}

	@Override
	public String selectChatCount(ChatCountVO vo) {
		return Integer.toString(mapper.selectChatCount(vo));
	}

	
	
}
