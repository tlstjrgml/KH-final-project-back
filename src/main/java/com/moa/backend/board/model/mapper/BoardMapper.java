package com.moa.backend.board.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.vo.Board;

@Mapper
public interface BoardMapper {

	void insertBoard(Board board);

//	List<Board> selectBoard();
//
//	int getListCount(int i);


	void updateViews(Long boardId);

	BoardDetailResponseDTO selectBoardDetail(Long boardId);

	

}
