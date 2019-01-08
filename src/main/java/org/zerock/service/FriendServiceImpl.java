package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.FriendVO;
import org.zerock.mapper.FriendMapper;

import lombok.Setter;

@Service
public class FriendServiceImpl implements FriendService {

	@Setter(onMethod_=@Autowired)
	private FriendMapper mapper;
	//이미 친구요청을 했는지 또는 이미 친구인지의 유효성을 파악하는 메서드
	@Override
	public FriendVO alreadyFriend(FriendVO vo) {
		FriendVO result1 = mapper.alreadyFriend(vo);
		//실제 값을 받은 뒤 a가 b에게 친구요청과
		//b가 a에게 친구요청이라는 두 값이 동일하다는 것을 인지 시켜주어야함
		//둘중 하나의 행만 존재하더라도 이미 a b둘중 하나가 친구요청을 수행했다는 의미
		vo.setUserid2(vo.getUserid1());
		vo.setUserid1(vo.getUserid2());
		FriendVO result2 = mapper.alreadyFriend(vo);
		
		return result1==null? result2:result1;
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
	//내가 userid2인 경우에만 accept를 시키는 것이므로 
	public boolean addFriend(FriendVO vo) {
		return mapper.addFriend(vo);
	}


	@Override
	public boolean removeFriend(FriendVO vo) {
		//remove는 둘 중 한명이 친구를 삭제할 수 있기 때문에 a,b행인지 b,a행인지 파악한 뒤 삭제를 시도해야함 
		//이미 친구인 상태이기때문에 a b b a 두행 중 하나는 무조건 존재함.
		FriendVO already = alreadyFriend(vo);
		return mapper.removeFriend(already);
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
