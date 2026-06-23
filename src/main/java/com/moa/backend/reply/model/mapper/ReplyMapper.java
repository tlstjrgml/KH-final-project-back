package com.moa.backend.reply.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.reply.dto.ReplyListResponseDTO;
import com.moa.backend.reply.model.vo.Reply;

@Mapper
public interface ReplyMapper {

	int insertReply(Reply reply);

	int updateReply(Reply reply);

	Reply selectReplyById(Long replyId);

	int selectReplyCount(Map<String, Object> params);

	List<ReplyListResponseDTO> selectReplyList(Map<String, Object> params);

	int deleteReply(Long replyId);
}
