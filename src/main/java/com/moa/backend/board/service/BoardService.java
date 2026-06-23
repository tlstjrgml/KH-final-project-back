package com.moa.backend.board.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moa.backend.board.dto.BoardCreateRequest;
import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.dto.BoardListResponseDTO;
import com.moa.backend.board.dto.BoardPageRequest;
import com.moa.backend.board.dto.BoardUpdateRequestDTO;
import com.moa.backend.board.model.mapper.BoardMapper;
import com.moa.backend.board.model.vo.Attachment;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.common.util.FileUtil;
import com.moa.backend.common.util.page.PageRequest;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.likes.model.mapper.LikesMapper;
import com.moa.backend.likes.model.vo.Likes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	private final LikesMapper likesMapper;
	private final FileUtil fileUtil;

//	public Board insertBoard(Board board) {
//		boardMapper.insertBoard(board);
//		return board;
//	}
	
	@Transactional(rollbackFor = Exception.class)
	public Board insertBoard(BoardCreateRequest request) {
		Board board = new Board();
        board.setBoardTitle(request.getBoardTitle());
        board.setBoardContent(request.getBoardContent());
		board.setBoardType(request.getBoardType());
		board.setMemberId(request.getMemberId());
		board.setWelfareId(request.getWelfareId());

		boardMapper.insertBoard(board);
		Long generatedBoardId = board.getBoardId(); // db의 boardId

		// DTO에서 파일 리스트를 꺼내 유효성 검사 후 업로드 가동
		List<MultipartFile> files = request.getFiles();
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					Attachment attm = fileUtil.saveFile(file);
					if (attm == null) {
						throw new RuntimeException("파일 업로드 중 오류가 발생하여 게시글 등록이 취소되었습니다.");
					}

					attm.setBoardId(generatedBoardId);
					boardMapper.insertAttachment(attm);
				}
			}
		}

        return board;
	}

	public int getListCount(int i) {
		return boardMapper.getListCount(i);
	}

	public BoardDetailResponseDTO getBoardDetail(Long boardId, CustomUserDetails userDetails) {
		// 1. DB에서 글 정보 가져오기
		BoardDetailResponseDTO dto = boardMapper.selectBoardDetail(boardId);
		if (dto == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		// 2. 해당 게시글 첨부파일 목록 가져오
		List<Attachment> fileList = boardMapper.selectAttachmentList(boardId);
		dto.setAttachments(fileList);

		// 3. 로그인시 조회수 올림, 내글인지, 좋아요 눌렀는지
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

	public void deleteBoard(Long boardId, Long memberId) {
		// 1. 상세조회 때 썼던 매퍼 재활용해서 글 정보 가져오기
		BoardDetailResponseDTO board = boardMapper.selectBoardDetail(boardId);

		if (board == null) {
			throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 게시글입니다.");
		}

		// 2. 글 작성자 ID와 현재 로그인한 유저 ID가 일치하는지 확인
		if (!board.getMemberId().equals(memberId)) {
			throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
		}

		// 3. 검증 통과 시 상태값 업데이트 때리기
		int result = boardMapper.deleteBoard(boardId);
		if (result == 0) {
			throw new IllegalArgumentException("삭제 처리에 실패했습니다.");
		}
	}

//	public void updateBoard(Board updateData, Long loginMemberId) {
//		BoardDetailResponseDTO originBoard = boardMapper.selectBoardDetail(updateData.getBoardId());
//
//		if (originBoard == null) {
//			throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 게시글입니다.");
//		}
//
//		// 2. 작성자 ID와 현재 로그인한 유저 ID 비교
//		if (!originBoard.getMemberId().equals(loginMemberId)) {
//			throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
//		}
//
//		// 3. 검증 통과 시 수정 처리 실행
//		int result = boardMapper.updateBoard(updateData);
//		if (result == 0) {
//			throw new IllegalArgumentException("수정 처리에 실패했습니다.");
//		}
//	}

	public void updateBoard(Long boardId, BoardUpdateRequestDTO updateData, CustomUserDetails userDetails) {
		// boardId를 통해 db에서 글 정보 가져옴 
		BoardDetailResponseDTO originalBoard = boardMapper.selectBoardDetail(boardId);
		if (originalBoard == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		if (!originalBoard.getMemberId().equals(userDetails.getMemberId())) {
			throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
		}

		// 게시글 본문 업데이트(제목, 내용, 복지 id)
		boardMapper.updateBoardContent(boardId, updateData);
		
		// 삭제할 파일 삭제 
		List<Long> deleteFileIds = updateData.getDeleteFileIds();
		if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
			for(Long attmId : deleteFileIds) {
				Attachment attm = boardMapper.selectAttachmentById(attmId);
				
				if (attm != null) {
					// 다른 board의 첨부파일인지 검사 
					if (!attm.getBoardId().equals(boardId)) {
						throw new IllegalArgumentException("잘못된 접근입니다: 해당 파일에 대한 삭제 권한이 없습니다.");
					}
					
					// 검증 통과 시 실제 맥북 폴더에서 파일 제거 후 DB 상태값 'N' 처리
					fileUtil.deleteFile(attm.getRenameName());
					boardMapper.deleteAttachment(attmId); 
				}
			}
		}

		List<MultipartFile> newFiles = updateData.getNewFiles();
		if (newFiles != null && !newFiles.isEmpty()) {
			for (MultipartFile file : newFiles) {
				if (!file.isEmpty()) {
					Attachment attm = fileUtil.saveFile(file);
					if (attm == null) {
						throw new RuntimeException("파일 수정 중 업로드 오류가 발생했습니다.");
					}
					// 현재 수정한 글 번호를 부모 ID로 박아서 새롭게 인서트
					attm.setBoardId(boardId);
					boardMapper.insertAttachment(attm);
				}
			}
		}
		
	}

	public PageResponse<BoardListResponseDTO> getBoardList(BoardPageRequest boardPageRequest) {

		// 1. 전체 게시글 개수 조회
        int totalItems = boardMapper.selectBoardCount(boardPageRequest);
        
        // 2. 페이징 계산기 조립
        Pagination pagination = new Pagination(boardPageRequest, totalItems);
        
		// 3. DB에서 현재 페이지 조건(offset, limit)에 맞는 딱 10개(혹은 세팅된 만큼)의 글만 가져오기
		List<BoardListResponseDTO> list = boardMapper.selectBoardListWithPaging(boardPageRequest);

		// 4. 최종 공용 배달 상자에 데이터와 페이징 정보를 묶어서 완성
		return new PageResponse<>(list, pagination);
	}

	public List<BoardListResponseDTO> getTop5(String boardType) {
		return boardMapper.getTop5(boardType);
	}

	public List<BoardListResponseDTO> getRecentNotice() {
		return boardMapper.getRecentNotice();
	}

}
