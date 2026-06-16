package com.moa.backend.board.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.vo.Board;

@Mapper
public interface BoardMapper {

	void insertBoard(Board board);

	int getListCount(int i);

	void updateViews(Long boardId);

	BoardDetailResponseDTO selectBoardDetail(Long boardId);

	int deleteBoard(Long boardId);

	int updateBoard(Board updateData);
	

}
