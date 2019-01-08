package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardPointVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.BoardPointMapper;
import org.zerock.mapper.MemberMapper;

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
	@Setter(onMethod_=@Autowired)
	private BoardPointMapper p_mapper;
	
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
	@Transactional
	public BoardVO get(Long bno) {
		mapper.updateReplyCnt(bno, 1);
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

	@Override
	@Transactional
	public boolean plusMemberClickPoint(BoardPointVO vo) {
		if(p_mapper.IsClick(vo)>0){ //userid,bno에 해당되는 값이 없을경우 -> 추천하지 않음
			return false;
		}else {
			mapper.plusBoardPoint(vo.getBno()); //board테이블에  point 수정 +1
			p_mapper.insertBoardPoint(vo); //추천시 boardpoint에 bno userid insert
		}
		return true;
	}
	@Override
	@Transactional
	public boolean minusMemberClickPoint(BoardPointVO vo) {
		if(p_mapper.IsClick(vo)>0){ //userid,bno에 해당되는 값이 없을경우 -> 추천
			mapper.minusBoardPoint(vo.getBno()); //board테이블에  point 수정 -1
			p_mapper.deleteBoardPoint(vo); //추천시 boardpoint에 bno userid delete
			return true;
		}else {
			return false;
			
		}
		
	}

	@Override
	public boolean isrecommend(String userid, Long bno) {
		BoardPointVO vo = new BoardPointVO();
		vo.setBno((long)bno);
		vo.setUserid(userid);
		if(p_mapper.IsClick(vo)>0) {
			return true;  //해당 유저가 이미 글을 추천했을 경우
		}else {
			return false; //해당 유저가 글을 아직 추천하지 않았을 경우
		}
	}
	
	
	
	

}
