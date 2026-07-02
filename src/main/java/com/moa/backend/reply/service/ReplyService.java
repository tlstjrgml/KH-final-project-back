package com.moa.backend.reply.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moa.backend.common.util.page.PageRequest;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.reply.dto.ReplyListResponseDTO;
import com.moa.backend.reply.model.mapper.ReplyMapper;
import com.moa.backend.reply.model.vo.Reply;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {
	private final ReplyMapper replyMapper;

	public int insertReply(Reply reply) {
        if ("R".equals(reply.getCode())) {
            
            Reply parentReply = replyMapper.selectReplyById(reply.getRefId());
            
            // 부모 댓글이 존재하지 않는 경우 차단
            if (parentReply == null) {
                throw new IllegalArgumentException("부모 댓글이 존재하지 않습니다.");
            }
            
            // 부모 댓글의 구분이 이미 'R'(대댓글)인 경우
            if ("R".equals(parentReply.getCode())) {
                throw new IllegalStateException("대댓글에는 더 이상 대댓글을 달 수 없습니다.");
            }
        }
		
		return replyMapper.insertReply(reply);
	}

	public int updateReply(Reply reply) {
		return replyMapper.updateReply(reply);
	}

	public PageResponse<ReplyListResponseDTO> getReplyList(Long refId, PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<>();
		params.put("refId", refId);  

		int offset = (pageRequest.getPage() - 1) * pageRequest.getLimit();  
		params.put("offset", offset);
		params.put("limit", pageRequest.getLimit());

		int totalItems = replyMapper.selectReplyCount(params); 
		
		List<ReplyListResponseDTO> content = replyMapper.selectReplyList(params);

		Pagination pagination = new Pagination(pageRequest, totalItems);

		return new PageResponse<>(content, pagination);
	}

	public void deleteReply(Long replyId, Long memberId) {
		Reply reply = replyMapper.selectReplyById(replyId);

		if (reply == null) {
			throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글입니다.");
		}

		// 글 작성자 ID와 현재 로그인한 유저 ID가 일치하는지 확인
		if (!reply.getMemberId().equals(memberId)) {
			throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}

		// 검증 통과 시 상태값 업데이트
		int result = replyMapper.deleteReply(replyId);
		if (result == 0) {
			throw new IllegalArgumentException("삭제 처리에 실패했습니다.");
		}
	}
}



























