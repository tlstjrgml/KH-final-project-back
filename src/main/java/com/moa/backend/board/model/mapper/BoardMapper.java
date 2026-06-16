package com.moa.backend.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.dto.BoardListResponseDTO;
import com.moa.backend.board.dto.BoardPageRequest;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.common.util.page.PageRequest;

@Mapper
public interface BoardMapper {

	void insertBoard(Board board);

	int getListCount(int i);
//	List<Board> selectBoard();
//
//	int getListCount(int i);


	void updateViews(Long boardId);

	BoardDetailResponseDTO selectBoardDetail(Long boardId);

	int deleteBoard(Long boardId);

	int updateBoard(Board updateData);

	int selectBoardCount(String boardType);

	List<BoardListResponseDTO> selectBoardListWithPaging(BoardPageRequest boardPageRequest);
	

}
