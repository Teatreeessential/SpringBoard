package org.zerock.controller;

import static org.junit.Assert.assertNotNull;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml",
						"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@Log4j
public class BoardControllerTests {
	
	@Setter(onMethod_ = @Autowired)
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;
	
	@org.junit.Before
	public void set() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	
//	@Test
	public void testList() throws Exception {
		
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
				.andReturn()
				.getModelAndView()
				.getModelMap());
	}
//	@Test
	public void testregister() throws Exception{
		String resultpage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
							.param("title", "테스트")
							.param("content", "테스트내용")
							.param("writer","안병찬")
							).andReturn().getModelAndView().getViewName();
		log.info(resultpage);				
	}
//	@Test
	public void getread() throws Exception{
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/get")
				.param("bno", "2"))
				.andReturn().getModelAndView().getModelMap());
	}
//	@Test
	public void getupdate() throws Exception{
		log.info(mockMvc.perform(MockMvcRequestBuilders.post("/board/modify")
				.param("bno", "2")
				.param("title", "테스트")
				.param("content", "테스트내용")
				.param("writer","안병찬"))
				.andReturn().getModelAndView().getModelMap());
	}
	
//	@Test
	public void getlist() throws Exception {
		log.info(mockMvc.perform(
				MockMvcRequestBuilders.get("/board/list")
				.param("pageNum", "2")
				.param("amount", "50"))
				.andReturn().getModelAndView().getModelMap());
	}
	@Test
	public void pagetest() {
		Criteria cri = new Criteria();
		cri.setPageNum(5);
		PageDTO dto = new PageDTO(cri, 123);
		System.out.println(dto.toString());
	}
}
