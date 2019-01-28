package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.ChatCountVO;
import org.zerock.domain.ChatVO;
import org.zerock.domain.FriendVO;
import org.zerock.mapper.ChatMapper;
import org.zerock.mapper.FriendMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class FriendServiceImpl implements FriendService {

	@Setter(onMethod_=@Autowired)
	private FriendMapper mapper;
	//이미 친구요청을 했는지 또는 이미 친구인지의 유효성을 파악하는 메서드
	@Setter(onMethod_=@Autowired)
	private ChatMapper chat_mapper;
	@Override
	public FriendVO alreadyFriend(FriendVO vo) {
		try {
			FriendVO change_value = (FriendVO)vo.clone();
			change_value.setUserid1(vo.getUserid2());
			change_value.setUserid2(vo.getUserid1());
			
			FriendVO result1 = mapper.alreadyFriend(vo);
			FriendVO result2 = mapper.alreadyFriend(change_value);
			
			return result1!=null? result1:result2;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean requestFriend(FriendVO vo) {
		//내가 친구추가를 할때 상대방이 이미 했을 지도 모르기 때문에 파악 해야함
			FriendVO already = alreadyFriend(vo);
			if(already!=null) {
				return false;
			}else {				
				return mapper.requestFriend(vo);
			}
		}

	@Override
	@Transactional
	//내가 userid2인 경우에만 accept를 시키는 것이므로 
	public boolean addFriend(FriendVO vo) {
		//delete update 문에서는 시퀀스 x 그래서 select문으로 가져와 insert 하는 쪽으로 해결
		//일단 친구추가 accept 1로 변경 chat room 추가
		try {
			int chat_room_num = mapper.getchatRoomNum();
			vo.setChat_room_num(chat_room_num);
			mapper.addFriend(vo);
			//채팅 카운트 세는 테이블에 값을 insert
			ChatCountVO chatcountvo = new ChatCountVO();
			chatcountvo.setChat_room_num(chat_room_num);
			chatcountvo.setChat_count(0);
			chatcountvo.setUserid(vo.getUserid1());
			chat_mapper.addChatCount(chatcountvo);
			chatcountvo.setUserid(vo.getUserid2());
			chat_mapper.addChatCount(chatcountvo);
			return true;
		}catch(Exception e) {
			log.info("addFriend 메서드 호출 중 에러");
			return false;
		}
	}


	@Override
	public boolean removeFriend(FriendVO vo) {
		//remove는 둘 중 한명이 친구를 삭제할 수 있기 때문에 a,b행인지 b,a행인지 파악한 뒤 삭제를 시도해야함 
		//이미 친구인 상태이기때문에 a b b a 두행 중 하나는 무조건 존재함.
		FriendVO already = alreadyFriend(vo);
		if(already == null) {
			return false; //삭제할 친구가 없다는 뜻
		}
		return mapper.removeFriend(already); //친구삭제 무사히 진행함
	}

	

	@Override
	public List<FriendVO> getFriend(String userid) {
		return mapper.getFriend(userid);
	}

	@Override
	public List<FriendVO> getRequestFriend(String userid) {
		return mapper.getRequestFriend(userid);
	}



}
