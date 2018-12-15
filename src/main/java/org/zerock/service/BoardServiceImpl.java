package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BoardServiceImpl implements BoardService {

	@Setter(onMethod_=@Autowired)
	private BoardMapper mapper;
	@Setter(onMethod_=@Autowired)
	private BoardAttachMapper a_mapper;
	
	@Override
	@Transactional
	public void register(BoardVO board) {
		log.info("register");
		
		mapper.insertSelectKey(board);
		if(board.getAttachList()==null || board.getAttachList().size()<=0) {
			return;
		}
		board.getAttachList().forEach(attach ->{
			attach.setBno(board.getBno());
			a_mapper.insert(attach);
		});
		
	}

	@Override
	public BoardVO get(Long bno) {
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		
		a_mapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board)==1;  
		
		if(modifyResult && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				a_mapper.insert(attach);
			});
			
		}
		
		return modifyResult; 
	}
	@Transactional
	@Override
	public boolean remove(Long bno) {
		a_mapper.deleteAll(bno);
		return mapper.delete(bno)==1;
	}

	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("페이징중");
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal() {
		
		return mapper.getTotalCount();
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		
		return a_mapper.findByBno(bno);
	}
	
	
	

}
