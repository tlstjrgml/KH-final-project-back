package com.moa.backend.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.mapper.BoardMapper;
import com.moa.backend.board.model.vo.Attachment;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.reply.model.mapper.ReplyMapper;
import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportDetailResponseDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportPageRequestDTO;
import com.moa.backend.report.dto.ReportUpdateRequestDTO;
import com.moa.backend.report.dto.ReportedBoardInfo;
import com.moa.backend.report.dto.ReportedReplyInfo;
import com.moa.backend.report.model.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportMapper reportMapper;
	private final BoardMapper boardMapper;
	private final ReplyMapper replyMapper;

	@Transactional(rollbackFor = Exception.class)
	public void insertReport(ReportCreateRequestDTO request) {
		String type = request.getTargetType();
		if (!"FRE".equals(type) && !"REV".equals(type) && !"REP".equals(type)) {
			throw new IllegalArgumentException("잘못된 대상 타입입니다. (FRE, REV, REP만 가능)");
		}
		
		reportMapper.insertReport(request);
		
	}

	public PageResponse<ReportListResponseDTO> selectReportList(ReportPageRequestDTO reportPageRequest) {
		int totalCount = reportMapper.selectReportCount(reportPageRequest);

		List<ReportListResponseDTO> reportList = reportMapper.selectReportList(reportPageRequest);

		Pagination pagination = new Pagination(reportPageRequest, totalCount);

		return new PageResponse<>(reportList, pagination);
	}

	@Transactional(rollbackFor = Exception.class)
    public boolean updateReport(ReportUpdateRequestDTO reportUpdateRequest) {
        int result = reportMapper.updateReport(reportUpdateRequest);
        return result > 0;
    }

	public ReportDetailResponseDTO<Object> getReportDetail(Long reportId) {
		ReportDetailResponseDTO<Object> detail = reportMapper.selectReportDetailBase(reportId);
        if (detail == null) {
            throw new IllegalArgumentException("존재하지 않는 신고 내역입니다.");
        }
        
        String type = detail.getTargetType();
        Long targetId = detail.getTargetId();

		if ("FRE".equals(type) || "REV".equals(type)) {
			BoardDetailResponseDTO boardInfo = boardMapper.selectBoardDetail(targetId);
			if (boardInfo != null) {
				List<Attachment> attachments = boardMapper.selectAttachmentList(targetId);
				boardInfo.setAttachments(attachments);
			}
			detail.setTargetDetails(boardInfo);
        } else if ("REP".equals(type)) {
            ReportedReplyInfo replyInfo = reportMapper.selectReportedReply(targetId);

            detail.setTargetDetails(replyInfo); 
        }
        
        return detail;
	}
}