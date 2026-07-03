package com.moa.backend.reply.controller;

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

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.common.util.page.PageRequest;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.reply.model.vo.Reply;
import com.moa.backend.reply.service.ReplyService;
import com.moa.backend.reply.dto.ReplyListResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
	
	private final ReplyService replyService;

	@PostMapping("/write")
	public ResponseEntity<?> insertReply(
			@RequestBody Reply reply,
			@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		try {
			reply.setMemberId(userDetails.getMemberId());

			int result = replyService.insertReply(reply);

			if (result > 0) {
				return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 성공적으로 등록되었습니다.");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 등록에 실패했습니다.");
			}
		} catch (IllegalStateException | IllegalArgumentException e) {
			// 서비스단에서 전달하 "대대댓글 금지" 메시지를 그대로 프론트엔드에 리턴 (400 에러)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}
	
	@PutMapping("/{replyId}")
	public ResponseEntity<?> updateReply(
			@PathVariable("replyId") Long replyId, 
			@RequestBody Reply reply,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		reply.setReplyId(replyId);
		reply.setMemberId(userDetails.getMemberId());

		int result = replyService.updateReply(reply);

		if (result > 0) {
			return ResponseEntity.ok("댓글이 수정되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 수정 권한이 없거나 존재하지 않는 댓글입니다.");
		}
	}
	
	
	// 특정 게시물 댓글 목록 조회 (페이징 처리됨)
	@GetMapping("/list/{refId}")
	public ResponseEntity<?> getReplyList(
			@PathVariable("refId") Long refId, 
			PageRequest pageRequest) {
		try {
            PageResponse<ReplyListResponseDTO> response = replyService.getReplyList(refId, pageRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 목록을 불러오는 중 오류가 발생했습니다.");
        }
	}
	
	// 댓글 삭제
	@DeleteMapping("/{replyId}")
	public ResponseEntity<?> deleteReply(
			@PathVariable("replyId") Long replyId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			// 로그인한 유저의 ID를 함께 넘겨서 본인 댓글인지 서비스에서 한 번 더 검증
			replyService.deleteReply(replyId, userDetails.getMemberId());

			return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
		} catch (IllegalArgumentException e) {
			// 본인 댓글이 아니거나 없는 글일 때 400 에러
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
		}
	}
}































