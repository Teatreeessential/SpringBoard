package org.zerock.domain;

import java.util.Date;

import lombok.Data;

@Data
public class FriendVO implements Cloneable {
	private	String userid1;
	private String userid2;
	private int accept;
	private int chat_room_num; //chat_room 은 10000개까지
	private Date time;
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

}
