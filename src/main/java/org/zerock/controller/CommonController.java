package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class CommonController {
	@GetMapping("/customLogin")
	public void loginInput(String error,String logout,Model model) {
		if(error!=null) {
			model.addAttribute("error", "로그인 에러");
		}
		if(logout != null) {
			model.addAttribute("logout","로그아웃");
		}
		
	}
	@GetMapping("/customLogout")
	public void logoutGET() {
		log.info("로그아웃");
	}
	@PostMapping("/customLogout")
	public void logoutPost() {
		log.info("post custom logout");
	}
	@GetMapping("/accessError")
	public void accessError() {
		log.info("엑세스 에러");
	}
}
