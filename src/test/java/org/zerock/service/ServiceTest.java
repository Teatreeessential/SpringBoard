package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardPointVO;
import org.zerock.domain.FriendVO;

import lombok.Setter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class ServiceTest {
	
	@Setter(onMethod_=@Autowired)
	private BoardService service;
	@Setter(onMethod_=@Autowired)
	private FriendService f_service;
	
//	@Test
	public void test() {
		BoardPointVO vo = new BoardPointVO();
		vo.setBno((long)475229);
		vo.setUserid("admin90");
		
		System.out.println(service.minusMemberClickPoint(vo));
	}
	
	@Test
	public void test2() {
		FriendVO vo = new FriendVO();
		vo.setAccept(0);
		vo.setUserid1("admin90");
		vo.setUserid2("admin89");
		
//		System.out.println(f_service.alreadyFriend(vo));
//		System.out.println(f_service.requestFriend(vo));
//		vo.setAccept(1);
//		System.out.println(f_service.addFriend(vo));
		System.out.println(f_service.removeFriend(vo));
	}
	
}
