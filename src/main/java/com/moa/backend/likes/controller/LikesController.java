package com.moa.backend.likes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.likes.model.vo.Likes;
import com.moa.backend.likes.service.LikesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardId}/likes")
public class LikesController {
	private final LikesService lService;

	// 특정 게시물 좋아요 여부 조회
	@GetMapping
	public ResponseEntity<Boolean> checkLikeStatus(
			@PathVariable("boardId") Long boardId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 비회원인 경우 무조건 false
		if (userDetails == null) {
			return ResponseEntity.ok(false);
		}

		Likes likes = new Likes();
		likes.setBoardId(boardId);
		likes.setMemberId(userDetails.getMemberId());

		// 서비스단에서 조사 개시 (결과는 true 아니면 false)
		boolean isLiked = lService.checkLikeStatus(likes);

		return ResponseEntity.ok(isLiked);
	}

	// 좋아요 등록
	@PostMapping
	public ResponseEntity<?> insertLike(
			@PathVariable("boardId") Long boardId, // 주소에서 boardId 얻음
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			Likes likes = new Likes();
			likes.setBoardId(boardId);
			likes.setMemberId(userDetails.getMemberId());
			lService.insertLike(likes);
			return ResponseEntity.ok("좋아요 성공");
		} catch (Exception e) {
			e.printStackTrace();
			// 프론트엔드한테 400(Bad Request) 코드와 함께 에러 메시지 쏘기
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 등록 실패: 존재하지 않는 게시글이거나 잘못된 요청입니다.");
		}
	}

	// 좋아요 취소
	@DeleteMapping
	public ResponseEntity<?> deleteLike(
			@PathVariable("boardId") Long boardId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			Likes likes = new Likes();
			likes.setBoardId(boardId);
			likes.setMemberId(userDetails.getMemberId());
			lService.deleteLike(likes);
			return ResponseEntity.ok("좋아요 취소 성공");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 취소 실패: 존재하지 않는 게시글이거나 잘못된 요청입니다.");
		}
	}
}
