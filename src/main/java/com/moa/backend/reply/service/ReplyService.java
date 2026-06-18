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
		// 만약 사용자가 대댓글(R)을 달겠다고 요청한 경우
        if ("R".equals(reply.getCode())) {
            
            // 1. 프론트엔드가 보낸 refId(부모 댓글 번호)로 부모 데이터를 DB에서 땡겨옵니다.
            Reply parentReply = replyMapper.selectReplyById(reply.getRefId());
            
            // 부모 댓글이 존재하지 않는 경우 차단
            if (parentReply == null) {
                throw new IllegalArgumentException("부모 댓글이 존재하지 않습니다.");
            }
            
            // 부모 댓글의 구분이 이미 'R'(대댓글)인 경우
            if ("R".equals(parentReply.getCode())) {
                // 대댓글에 또 대댓글을 다는 행위이므로 예외를 발생시켜 작업을 중단시킵니다.
                throw new IllegalStateException("대댓글에는 더 이상 대댓글을 달 수 없습니다.");
            }
        }
		
		return replyMapper.insertReply(reply);
	}

	public int updateReply(Reply reply) {
		return replyMapper.updateReply(reply);
	}

	public PageResponse<ReplyListResponseDTO> getReplyList(Long refId, PageRequest pageRequest) {
		// 마이바티스용 파라미터 맵
		Map<String, Object> params = new HashMap<>();
		params.put("refId", refId); // 조회할 boardId

		int offset = (pageRequest.getPage() - 1) * pageRequest.getLimit(); // 몇개를 건너뛸것인가?
		params.put("offset", offset);
		params.put("limit", pageRequest.getLimit());

		// DB에서 댓글 갯수를 가져옴
		int totalItems = replyMapper.selectReplyCount(params); 
		
		// 페이징처리 대댓글 포함 특정 boardId의 댓글 가져오기
		List<ReplyListResponseDTO> content = replyMapper.selectReplyList(params);

		Pagination pagination = new Pagination(pageRequest, totalItems);

		return new PageResponse<>(content, pagination);
	}
}



























