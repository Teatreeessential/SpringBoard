package org.zerock.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.domain.ChatVO;
import org.zerock.domain.FriendVO;
import org.zerock.service.FriendService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping(value="/user")
public class FriendController {
	
	@Setter(onMethod_=@Autowired)
	private FriendService service;
	
	//친구창 페이지
	@RequestMapping(value="/friend")
	public String friendPage(Model model,Principal principal) {
		if(principal==null) {
			return "accessError";
		}else {
			//친구목록과 채팅창 번호 받아서 처리
			List<String> chat_room_nums;
			List<String> friends = service.getFriend(principal.getName()).stream().map(val -> {
				if(val.getUserid1().equals(principal.getName())) {
					return val.getUserid2()+"_"+val.getChat_room_num();
				}else {
					return val.getUserid1()+"_"+val.getChat_room_num();
				}
			}).collect(()-> new ArrayList<String>(),
						(c,s) -> c.add(s),
						(lst1,lst2) -> lst1.addAll(lst2));
			
			
			
			model.addAttribute("friends", friends);
			model.addAttribute("requests", service.getRequestFriend(principal.getName()));
			
			return "/user/friend";
			
		}
	}
	//리스트에서 친구요청
	@RequestMapping(value="/requestfriend",produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> requestfriend(@RequestParam String userid,Principal principal){
		if(principal==null) {
			return new ResponseEntity<String>("로그인 후 친구추가 하십시오",HttpStatus.OK);
		}else {
			if(userid.equals(principal.getName())) {
				return new ResponseEntity<String>("자기 자신에게 친구추가 할 수 없습니다.",HttpStatus.OK);
			}else {
				FriendVO vo = new FriendVO();
				//이미 다른 사람이 나에게 친구요청을 신청했을 경우를 우선적으로 파악함
				vo.setUserid1(principal.getName());
				vo.setUserid2(userid);
				vo.setAccept(0);
				if(service.requestFriend(vo)) {
					//true일 경우 상대방이 나에게 친구요청을 한 적이 없으므로 친구요청 가능
					return new ResponseEntity<String>("친구 요청을 보냈습니다.",HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("이미 친구이거나 친구요청을 받거나 보냈습니다.",HttpStatus.OK);
				}
			}
		}
	}
	@RequestMapping(value="/acceptfriend",produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> addfriend(@RequestParam String userid,Principal principal){
		if(principal==null) {
			return new ResponseEntity<String>("로그인 후 친구추가 하십시오",HttpStatus.OK);
		}else {
			//올바르지 않은 경로로 요청했을 때
			if(userid.equals(principal.getName())) {
				return new ResponseEntity<String>("자기 자신에게 친구추가 할 수 없습니다.",HttpStatus.OK);
			}else {
				FriendVO vo = new FriendVO();
				// userid1 -> userid2 이기 때문에 userid2가 내 아이디일 경우에는 userid1은 무조건 친구신청자
				vo.setUserid1(userid); //상대방 아이디
				vo.setUserid2(principal.getName()); //내 아이디
				vo.setAccept(1);
				service.addFriend(vo);
				return new ResponseEntity<String>(userid+"님을 친구 추가 하셨습니다.",HttpStatus.OK);
				
			}
		}
	}
	
	
	@RequestMapping(value="/removefriend",produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> removefriend(@RequestParam String userid,Principal principal){
		if(principal==null) {
			return new ResponseEntity<String>("로그인 후 친구삭제 하십시오",HttpStatus.OK);
		}else {
			if(userid.equals(principal.getName())) {
				return new ResponseEntity<String>("자기 자신에게 친구삭제를 할 수 없습니다.",HttpStatus.OK);
			}else {
				FriendVO vo = new FriendVO();
				vo.setUserid1(userid);
				vo.setUserid2(principal.getName());
				vo.setAccept(1);
				if(service.removeFriend(vo)) {
					return new ResponseEntity<String>("친구 삭제하셨습니다.",HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("유효한 경로로 요청하시길 바랍니다.",HttpStatus.OK);
				}
				
			}
		}
	}
	
	
}
