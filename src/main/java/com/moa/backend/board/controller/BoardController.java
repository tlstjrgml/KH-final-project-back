package com.moa.backend.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/list")
    public ResponseEntity<?> listBoard(
            @RequestParam(value = "page", defaultValue = "1")
            int currentPage) {

        int listCount = bService.getListCount(2);

        PageInfo pi = Pagination.getPageInfo(
                currentPage,
                listCount,
                10
        );

        List<Board> boardList = bService.selectBoard();

        return ResponseEntity.ok(boardList);
    }

}
