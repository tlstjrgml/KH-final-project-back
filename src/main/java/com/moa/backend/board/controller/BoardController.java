package com.moa.backend.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.dto.BoardListResponseDTO;
import com.moa.backend.board.dto.BoardPageRequest;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.board.service.BoardService;
import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.common.util.page.PageRequest;
import com.moa.backend.common.util.page.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

	private final BoardService bService;

	@PostMapping("/write")
	public ResponseEntity<?> writeBoard(@RequestBody Board board,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		// JWT통해 받은 memberId로 변경
		board.setMemberId(userDetails.getMemberId());
		Board savedBoard = bService.insertBoard(board);

		return ResponseEntity.ok(savedBoard);
	}

	// 게시글 상세 조회
	@GetMapping("/{boardId}")
	public ResponseEntity<?> getBoardDetail(@PathVariable("boardId") Long boardId, // 아까 배운 억까 방지용 ("boardId") 명시!
			@AuthenticationPrincipal CustomUserDetails userDetails // 로그인 안한 경우 null
	) {
		try {
			// 서비스단에 글 번호와 로그인 유저 정보를 함께 응답
			BoardDetailResponseDTO detail = bService.getBoardDetail(boardId, userDetails);

			return ResponseEntity.ok(detail);
		} catch (IllegalArgumentException e) {
			// 없는 게시글 번호 등으로 예외가 던져지면 400 에러 처리
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류가 발생했습니다.");
		}
	}

	// 게시글 삭제
	@DeleteMapping("/{boardId}")
	public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Long boardId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			// 로그인한 유저의 ID를 함께 넘겨서 본인 글인지 서비스에서 한 번 더 검증
			bService.deleteBoard(boardId, userDetails.getMemberId());

			return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			// 본인 글이 아니거나 없는 글일 때 400 에러
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
		}
	}

	// 게시글 수정
	@PutMapping("/{boardId}")
	public ResponseEntity<?> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody Board updateData, // 수정할
																												// 제목과
																												// 내용이
																												// 담긴 객체
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			updateData.setBoardId(boardId);
			bService.updateBoard(updateData, userDetails.getMemberId());
			return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
		} catch (IllegalArgumentException e) {
			// 본인 글이 아니거나 없는 글일 때 400 에러
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류가 발생했습니다.");
		}
	}

	// 게시글 목록 페이징 조회
	@GetMapping("/list")
	public ResponseEntity<?> getBoardList(BoardPageRequest boardPageRequest) {
		try {
			// 게시판 전용 요청 DTO를 서비스로 토스
			PageResponse<BoardListResponseDTO> response = bService.getBoardList(boardPageRequest);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("목록을 불러오는 중 오류가 발생했습니다.");
		}
	}
}
