package org.zerock.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardPointVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	@Setter(onMethod_ = @Autowired)
	private BoardService service;
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null||attachList.size()<=0) {
			return;
		}
		attachList.forEach(attach -> {
			Path file = Paths.get("c:\\upload\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
			try {
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbnail =  Paths.get("c:\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					Files.deleteIfExists(thumbnail);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
	}
	
	@GetMapping("/list")
	public void list(Model model,Criteria cri) {
		log.info("list....");
		System.out.println(cri.getPageNum());
		model.addAttribute("list",service.getList(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal()));
		
	}
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {
		
	}
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register...");
		//bno 값은 언제 들어가게 되는건가?
		log.info("------------register");
		System.out.println(board);
		service.register(board);
		rttr.addFlashAttribute("result",board.getBno());
		return "redirect:/board/list";
	}
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno,@ModelAttribute("cri") Criteria cri , Model model,Principal principal) {
		log.info("/get...");
		try {
			model.addAttribute("isrecommend", service.isrecommend(principal.getName(),bno));
		}catch(NullPointerException e) {
			model.addAttribute("isrecommend",false);
		}finally {
			model.addAttribute("board",service.get(bno));
		}
	}
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board,@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("update... ");
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","success");
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("keyword",cri.getKeyword());
		rttr.addAttribute("type",cri.getType());
		return "redirect:/board/list";
		
	}
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")
	public String remove(Long bno,@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		
		List<BoardAttachVO> list = service.getAttachList(bno);
		
		if(service.remove(bno)) {
			deleteFiles(list);
			rttr.addFlashAttribute("result","success");
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("keyword",cri.getKeyword());
		rttr.addAttribute("type",cri.getType());
		//redirect시 넘겨 줄 파라미터 값
		//flash는 파라미터값이 아니라 view에서 사용될 일회성 데이터
		return "redirect:/board/list";
		
	}
	@GetMapping(value="/getAttachList",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		return new ResponseEntity<List<BoardAttachVO>>(service.getAttachList(bno),HttpStatus.OK);
	}

	@GetMapping(value="/addpoint",produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> addPoint(@RequestParam Long bno,Principal principal) {
		System.out.println(bno);
		
		if(principal==null) {
			return new ResponseEntity<String>("false",HttpStatus.OK);
		}else {
			BoardPointVO vo = new BoardPointVO();
			vo.setUserid(principal.getName());
			vo.setBno(bno);
			if(service.plusMemberClickPoint(vo)) { //true일 경우 추천하지 않은 게시글이고 정상적으로 추천됨 //그냥 url로 들어오면 차단
				return new ResponseEntity<String>("추천 하셨습니다.",HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("이미 추천한 게시글 입니다.",HttpStatus.OK);
			}
		}
		
	}
	
	@GetMapping(value="/minuspoint",produces="text/plain;charset=UTF-8")
	public ResponseEntity<String> minusPoint(@RequestParam Long bno,Principal principal) {
		if(principal==null) {
			return new ResponseEntity<String>("false",HttpStatus.OK);
		}else {
			BoardPointVO vo = new BoardPointVO();
			vo.setUserid(principal.getName());
			vo.setBno(bno);
			if(service.minusMemberClickPoint(vo)) { //true일 경우 추천하지 않은 게시글이고 정상적으로 추천됨 //그냥 url로 들어오면 차단
				return new ResponseEntity<String>("추천 취소하셨습니다.",HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("올바른 경로로 접근해주세요",HttpStatus.OK);
			}
		}
		
		
	}
	
	
	
	
	@GetMapping("/accessError")
	public String accessError() {
		return "/accessError";
	}
	
	
	
}
