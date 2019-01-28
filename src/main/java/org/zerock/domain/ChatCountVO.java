package org.zerock.domain;

import lombok.Data;

@Data
public class ChatCountVO {
	private int chat_room_num;
	private String userid;
	private int chat_count;
}
