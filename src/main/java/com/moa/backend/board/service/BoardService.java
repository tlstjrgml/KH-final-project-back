package com.moa.backend.board.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.moa.backend.common.service.S3UploadService;
import com.moa.backend.common.util.FileUtil;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.likes.model.mapper.LikesMapper;
import com.moa.backend.likes.model.vo.Likes;
import com.moa.backend.board.dto.AttachmentDownloadDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	private final LikesMapper likesMapper;
	private final FileUtil fileUtil;
	private final S3UploadService s3UploadService;

	@Transactional(rollbackFor = Exception.class)
	public Board insertBoard(BoardCreateRequest request) throws IOException {
		Board board = new Board();
		board.setBoardTitle(request.getBoardTitle());
		board.setBoardContent(request.getBoardContent());
		board.setBoardType(request.getBoardType());
		board.setMemberId(request.getMemberId());
		board.setWelfareId(request.getWelfareId());

		boardMapper.insertBoard(board);
		Long generatedBoardId = board.getBoardId();

		List<MultipartFile> files = request.getFiles();
		if (files != null && !files.isEmpty()) {
			Set<String> allowedExt = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "pdf", "doc", "docx"));

			for (MultipartFile file : files) {
				String originalName = file.getOriginalFilename();
				int dotIndex = originalName.lastIndexOf(".");
				String ext = originalName.substring(dotIndex + 1).toLowerCase();

				if (!allowedExt.contains(ext)) {
					throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + ext);
				}

				String url = s3UploadService.uploadFile(file);
				Attachment attm = new Attachment();
				attm.setOriginalName(originalName);
				attm.setAttmPath(url);
				attm.setBoardId(generatedBoardId);
				boardMapper.insertAttachment(attm);
			}
		}

		return board;
	}

	public int getListCount(int i) {
		return boardMapper.getListCount(i);
	}

	public BoardDetailResponseDTO getBoardDetail(Long boardId, CustomUserDetails userDetails) {
		BoardDetailResponseDTO dto = boardMapper.selectBoardDetail(boardId);
		if (dto == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}

		List<Attachment> fileList = boardMapper.selectAttachmentList(boardId);
		dto.setAttachments(fileList);

		if (userDetails != null) {
			Long loginMemberId = userDetails.getMemberId();

			boardMapper.updateViews(boardId);
			dto.setViews(dto.getViews() + 1);
			dto.setIsOwner(dto.getMemberId().equals(loginMemberId));

			int count = likesMapper.checkLikeStatus(new Likes(loginMemberId, boardId));
			dto.setIsLiked(count > 0);
		} else {
			dto.setIsLiked(false);
			dto.setIsOwner(false);
		}

		return dto;
	}

	public void deleteBoard(Long boardId, CustomUserDetails userDetails) {
		BoardDetailResponseDTO board = boardMapper.selectBoardDetail(boardId);
		if (board == null) {
			throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 게시글입니다.");
		}

		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin && !board.getMemberId().equals(userDetails.getMemberId())) {
			throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
		}

		int result = boardMapper.deleteBoard(boardId);
		if (result == 0) {
			throw new IllegalArgumentException("삭제 처리에 실패했습니다.");
		}
	}

	public void updateBoard(Long boardId, BoardUpdateRequestDTO updateData, CustomUserDetails userDetails) throws IOException {
		BoardDetailResponseDTO originalBoard = boardMapper.selectBoardDetail(boardId);
		if (originalBoard == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}

		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin && !originalBoard.getMemberId().equals(userDetails.getMemberId())) {
			throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
		}

		boardMapper.updateBoardContent(boardId, updateData);

		List<Long> deleteFileIds = updateData.getDeleteFileIds();
		if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
			for (Long attmId : deleteFileIds) {
				Attachment attm = boardMapper.selectAttachmentById(attmId);
				if (attm != null) {
					if (!attm.getBoardId().equals(boardId)) {
						throw new IllegalArgumentException("잘못된 접근입니다: 해당 파일에 대한 삭제 권한이 없습니다.");
					}
					s3UploadService.deleteFile(attm.getAttmPath());
					boardMapper.deleteAttachment(attmId);
				}
			}
		}

		List<MultipartFile> newFiles = updateData.getNewFiles();
		if (newFiles != null && !newFiles.isEmpty()) {
			Set<String> allowedExt = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "pdf", "doc", "docx"));

			for (MultipartFile file : newFiles) {
				if (!file.isEmpty()) {
					String originalName = file.getOriginalFilename();
					int dotIndex = originalName.lastIndexOf(".");
					String ext = originalName.substring(dotIndex + 1).toLowerCase();

					if (!allowedExt.contains(ext)) {
						throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + ext);
					}

					String url = s3UploadService.uploadFile(file);
					Attachment attm = new Attachment();
					attm.setOriginalName(originalName);
					attm.setAttmPath(url);
					attm.setBoardId(boardId);
					boardMapper.insertAttachment(attm);
				}
			}
		}
	}

	public PageResponse<BoardListResponseDTO> getBoardList(BoardPageRequest boardPageRequest) {
		int totalItems = boardMapper.selectBoardCount(boardPageRequest);
		Pagination pagination = new Pagination(boardPageRequest, totalItems);
		List<BoardListResponseDTO> list = boardMapper.selectBoardListWithPaging(boardPageRequest);
		return new PageResponse<>(list, pagination);
	}

	public List<BoardListResponseDTO> getTop5(String boardType) {
		return boardMapper.getTop5(boardType);
	}

	public List<BoardListResponseDTO> getRecentNotice() {
		return boardMapper.getRecentNotice();
	}
	
	public AttachmentDownloadDto downloadAttm(Long attmId) {
		Attachment attm = boardMapper.selectAttachmentById(AttmId);
		if(attm == null) {
			throw new IllegalArgumentException("존재하지않는 파일입니다");
		}
		
		byte[] fileData = s3UploadService.downloadFile(attm.getAttmPath());
		
		return new AttachmentDownloadDto(attm.getOriginalName(), fileData);
	}
	
	
}