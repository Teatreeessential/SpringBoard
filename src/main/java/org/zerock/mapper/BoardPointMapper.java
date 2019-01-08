package org.zerock.mapper;

import org.zerock.domain.BoardPointVO;
import org.zerock.domain.BoardVO;

public interface BoardPointMapper {
	public int IsClick(BoardPointVO vo); 
	public void insertBoardPoint(BoardPointVO vo); 
	public void deleteBoardPoint(BoardPointVO vo);
}
