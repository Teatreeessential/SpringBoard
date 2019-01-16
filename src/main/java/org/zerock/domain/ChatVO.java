package org.zerock.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ChatVO {
	private int chat_room_num;
	private String userid;
	private String message;
	private String message_date;
}
