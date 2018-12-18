package org.zerock.controller;



import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/replies")
@Log4j
public class ReplyController {
	
	@Setter(onMethod_= @Autowired)
	private ReplyService service;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/new", 
			consumes = "application/json",
			produces = {MediaType.TEXT_PLAIN_VALUE})
	//consumes는 json형식의 데이터만 처리하도록 하고 응답에 대한 결과로 text를 반환하도록 하겠다는 뜻
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		
		int insertCount = service.register(vo);
		
		return insertCount == 1 ? new ResponseEntity<>("success",HttpStatus.OK):
									new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@GetMapping(value="/pages/{bno}/{page}",
				produces = {MediaType.APPLICATION_XML_VALUE,
							MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(
			@PathVariable("page") int page,
			@PathVariable("bno") Long bno){
			
		Criteria cri = new Criteria(page,10);
		
		return new ResponseEntity<>(service.getReplyCount(cri, bno),HttpStatus.OK);
			
	}
	
	@GetMapping(value="/{rno}",
				produces= {
							MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		
		return new ResponseEntity<ReplyVO>(service.get(rno),HttpStatus.OK);
	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@DeleteMapping(value="/{rno}",produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo){
		
		return service.remove(vo.getRno()) == 1
				? new ResponseEntity<String>("success",HttpStatus.OK):
					new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@RequestMapping(method= {RequestMethod.PUT,RequestMethod.PATCH},
					value="/{rno}",
					consumes = "application/json",
					produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(
				@PathVariable("rno") Long rno,
				@RequestBody ReplyVO vo){
		
		vo.setRno(rno);
		return service.modify(vo) == 1 ?
				new ResponseEntity<String>("success",HttpStatus.OK):
					new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
