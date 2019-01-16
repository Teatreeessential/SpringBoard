package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.FriendVO;

public interface FriendMapper {
	public FriendVO alreadyFriend(FriendVO vo);
	public boolean requestFriend(FriendVO vo);
	public boolean addFriend(FriendVO vo);
	public boolean removeFriend(FriendVO vo);
	
	public List<FriendVO> getFriend(String userid); //이미 친구인 사람들
	public List<FriendVO> getRequestFriend(String userid); //나에게 친구요청을 한 사람들
	public int getchatRoomNum();
	
	
	
}
