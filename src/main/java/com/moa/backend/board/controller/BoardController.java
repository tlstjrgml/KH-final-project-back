package com.moa.backend.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.vo.Board;
import com.moa.backend.board.service.BoardService;
import com.moa.backend.common.config.jwt.CustomUserDetails;

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

//	@GetMapping("/list")
//    public ResponseEntity<?> listBoard(
//            @RequestParam(value = "page", defaultValue = "1")
//            int currentPage) {
//
//        int listCount = bService.getListCount(2);
//
//        PageInfo pi = Pagination.getPageInfo(
//                currentPage,
//                listCount,
//                10
//        );
//
//        List<Board> boardList = bService.selectBoard();
//
//        return ResponseEntity.ok(boardList);
//    }

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
}
