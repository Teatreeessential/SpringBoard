package org.zerock.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;

@Aspect
public class BoardAop {
	
	@Setter(onMethod_=@Autowired)
	private BoardMapper mapper;
	
	@Pointcut("execution(public * org.zerock.service..*)")
	public void counter() {
		
	}
}
