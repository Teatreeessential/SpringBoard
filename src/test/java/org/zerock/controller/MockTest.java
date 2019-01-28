package org.zerock.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.domain.ChatCountVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.service.ChatService;
import org.zerock.service.FriendService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Setter;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class MockTest {
	
	
	@Setter(onMethod_ = @Autowired)
	private FriendService service;
	@Setter(onMethod_ = @Autowired)
	private ChatService chat_service;
	private MockMvc mockMvc;
	
	
	
//	@Test
	public void testregister() throws Exception{
		List<String> friends = service.getFriend("admin91").stream().map(val -> {
			if(val.getUserid1().equals("admin9"
					+ "1")) {
				return val.getUserid2()+"_"+val.getChat_room_num()+"-"+val.getChat_count();
			}else {
				return val.getUserid1()+"_"+val.getChat_room_num()+"-"+val.getChat_count();
			}
		}).collect(()-> new ArrayList<String>(),
					(c,s) -> c.add(s),
					(lst1,lst2) -> lst1.addAll(lst2));
		
		System.out.println(friends);
		
	}
//	@Test
	public void test() {
		ChatCountVO vo = new ChatCountVO();
		vo.setChat_room_num(12);
		vo.setUserid("admin91");
		
		chat_service.updateChatCount(vo);
		
		
	}
	@Test
	public void test1() {
		JsonParser jp = new JsonParser();
		String json = "{\"name\":\"안병찬\"}";
		JsonObject object = (JsonObject) jp.parse(json);
		
		object.addProperty("conut", 1);
		
		System.out.println(object.toString());
	}
	
	
}
