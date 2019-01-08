package org.zerock.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(value="/friend")
	public String friendPage(Model model,Principal principal) {
		if(principal==null) {
			return "accessError";
		}else {
			model.addAttribute("friends", service.getFriend(principal.getName()));
			model.addAttribute("requests", service.getRequestFriend(principal.getName()));
			
			return "/user/friend";
			
		}
	}
	
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
					//0일경우 상대방도 나에게 친구요청을 한적이 없으므로 친구요청 수행
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
			if(userid.equals(principal.getName())) {
				return new ResponseEntity<String>("자기 자신에게 친구추가 할 수 없습니다.",HttpStatus.OK);
			}else {
				FriendVO vo = new FriendVO();
				vo.setUserid1(userid);
				vo.setUserid2(principal.getName());
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
				vo.setUserid1(principal.getName());
				vo.setUserid2(userid);
				vo.setAccept(1);
				service.removeFriend(vo);
				return new ResponseEntity<String>("친구 삭제하셨습니다.",HttpStatus.OK);
				
			}
		}
	}
	
}
