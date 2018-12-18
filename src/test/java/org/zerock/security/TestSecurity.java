package org.zerock.security;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.MemberMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@Log4j
public class TestSecurity {
	
	@Setter(onMethod_ = @Autowired)
	private MemberMapper mapper;
	
	@Test
	public void test() {
		MemberVO vo = mapper.read("admin90");
		
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		String pw =bc.encode("pw"+90);
		
		System.out.println("==================================");
		
		log.info(pw);
		log.info(vo.getUserpw());
		if(pw.equals(vo.getUserpw())){
			System.out.println("비밀번호 같음");
		}
		
		
		System.out.println("==================================");
		
		
	}
	
}
