package org.zerock.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.domain.ChatCountVO;
import org.zerock.domain.ChatVO;
import org.zerock.service.ChatService;

import lombok.Setter;

@Controller
public class ChatController {
	
	@Setter(onMethod_=@Autowired)
	private ChatService service;
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/chat",produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ChatVO>> getChatList(@RequestParam String chatRoomNum,Principal principal){
		List<ChatVO> list = service.getChatList(Integer.parseInt(chatRoomNum));
		return new ResponseEntity<List<ChatVO>>(list,HttpStatus.OK);
	}
	@GetMapping(value="/reset",produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<String> resetCount(@RequestParam String chatRoomNum,Principal principal){
		ChatCountVO vo = new ChatCountVO();
		vo.setChat_room_num(Integer.parseInt(chatRoomNum));
		vo.setUserid(principal.getName());
		service.resetChatCount(vo);
		return new ResponseEntity<String>("0",HttpStatus.OK);
	}
}
