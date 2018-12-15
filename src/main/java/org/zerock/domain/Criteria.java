package org.zerock.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@data를 쓰게 되면 생성자의 초기화를 시키기가 어렵다 그냥이게 편함
public class Criteria {
	private int pageNum;
	private int amount;
	
	private String type;
	private String keyword;
	
	public Criteria() {
		this(1,10);
	}

	public Criteria(int pageNum, int amount) {
		super();
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	public String[] getTypeArr() {
		System.out.println("에러 지점");
		
		return type == null? new String[] {}:type.split("");
	}
	
}
