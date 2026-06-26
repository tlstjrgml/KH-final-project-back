package com.moa.backend.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.dto.BoardListResponseDTO;
import com.moa.backend.board.dto.BoardPageRequest;
import com.moa.backend.board.dto.BoardUpdateRequestDTO;
import com.moa.backend.board.model.vo.Attachment;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.common.util.page.PageRequest;

@Mapper
public interface BoardMapper {

	void insertBoard(Board board);

	int getListCount(int i);

	void updateViews(Long boardId);

	BoardDetailResponseDTO selectBoardDetail(Long boardId);

	int deleteBoard(Long boardId);

	int updateBoard(Board updateData);

	int selectBoardCount(BoardPageRequest boardPageRequest);

	List<BoardListResponseDTO> selectBoardListWithPaging(BoardPageRequest boardPageRequest);

	int insertAttachment(Attachment attm);

	List<Attachment> selectAttachmentList(Long boardId);

	int updateBoardContent(Long boardId, BoardUpdateRequestDTO updateData);

	Attachment selectAttachmentById(Long attmId);

	int deleteAttachment(Long attmId);
	
	List<BoardListResponseDTO> getTop5(String boardType);

	List<BoardListResponseDTO> getRecentNotice();

	
	

}
