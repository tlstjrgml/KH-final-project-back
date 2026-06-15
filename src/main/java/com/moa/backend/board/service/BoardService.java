package com.moa.backend.board.service;

import org.springframework.stereotype.Service;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.mapper.BoardMapper;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.likes.model.mapper.LikesMapper;
import com.moa.backend.likes.model.vo.Likes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	private final LikesMapper likesMapper;

	public Board insertBoard(Board board) {
		boardMapper.insertBoard(board);
		return board;
	}

//	public List<Board> selectBoard() {
//		return mapper.selectBoard();
//	}

	public int getListCount(int i) {
		return boardMapper.getListCount(i);
	}

	public BoardDetailResponseDTO getBoardDetail(Long boardId, CustomUserDetails userDetails) {
		// 1. DB에서 글 정보 가져오기
		BoardDetailResponseDTO dto = boardMapper.selectBoardDetail(boardId);
		if (dto == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}

		// 2. 로그인시 조회수 올림, 내글인지, 좋아요 눌렀는지
		if (userDetails != null) {
			Long loginMemberId = userDetails.getMemberId();

			// 조회수 증가
			boardMapper.updateViews(boardId);
			dto.setViews(dto.getViews() + 1);

			// 내글 여부
			dto.setIsOwner(dto.getMemberId().equals(loginMemberId));

			// 내가 좋아요를 눌렀는지 파악
			int count = likesMapper.checkLikeStatus(new Likes(loginMemberId, boardId));
			dto.setIsLiked(count > 0);
		} else {
			// 비회원인 경우
			dto.setIsLiked(false);
			dto.setIsOwner(false);
		}

		return dto;
	}

}
