package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	@Setter(onMethod_ = @Autowired)
	private BoardMapper b_mapper;
	
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		b_mapper.updateReplyCnt(vo.getBno(), 1);
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		return mapper.update(vo);
		
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		
		ReplyVO vo = mapper.read(rno);
		
		b_mapper.updateReplyCnt(vo.getBno(), -1);
		return mapper.delete(rno);
		
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		return mapper.getListWithPaging(cri, bno);
		
	}

	@Override
	public ReplyPageDTO getReplyCount(Criteria cri, Long bno) {
		return new ReplyPageDTO(mapper.getReplyCount(bno),mapper.getListWithPaging(cri, bno));
	}




}
