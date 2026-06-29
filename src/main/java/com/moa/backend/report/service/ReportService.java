package com.moa.backend.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.backend.board.dto.BoardDetailResponseDTO;
import com.moa.backend.board.model.mapper.BoardMapper;
import com.moa.backend.board.model.vo.Attachment;
import com.moa.backend.common.util.page.PageResponse;
import com.moa.backend.common.util.page.Pagination;
import com.moa.backend.notification.SseEmitterRepository;
import com.moa.backend.reply.model.mapper.ReplyMapper;
import com.moa.backend.report.dto.ReportCreateRequestDTO;
import com.moa.backend.report.dto.ReportDetailResponseDTO;
import com.moa.backend.report.dto.ReportListResponseDTO;
import com.moa.backend.report.dto.ReportMyResponseDto;
import com.moa.backend.report.dto.ReportPageRequestDTO;
import com.moa.backend.report.dto.ReportUpdateRequestDTO;
import com.moa.backend.report.dto.ReportedReplyInfo;
import com.moa.backend.report.model.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportMapper reportMapper;
	private final BoardMapper boardMapper;
	private final ReplyMapper replyMapper;

	private final SseEmitterRepository sseEmitterRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void insertReport(ReportCreateRequestDTO request) {
		String type = request.getTargetType();
		if (!"FRE".equals(type) && !"REV".equals(type) && !"REP".equals(type)) {
			throw new IllegalArgumentException("잘못된 대상 타입입니다. (FRE, REV, REP만 가능)");
		}
		
		// 중복 신고 검증 로직 추가
		// DB에 동일한 memberId와 targetId로 신고한 내역이 있는지 카운트를 가져옵니다.
		int duplicateCount = reportMapper.checkDuplicateReport(request);
		if (duplicateCount > 0) {
			// 프론트엔드의 catch 블록으로 에러 메시지를 던집니다.
			throw new IllegalArgumentException("이미 신고한 게시물입니다.");
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
        if(result > 0) {
            Long memberId = reportMapper.selectReporterIdByReportId(reportUpdateRequest.getReportId());
            System.out.println("신고자 memberId: " + memberId); // 추가
            if("DONE".equals(reportUpdateRequest.getStatus())){
                sseEmitterRepository.sendNotification(memberId, "신고가 처리되었습니다. 사유: " + reportUpdateRequest.getReportResult());
            }else if("REJECT".equals(reportUpdateRequest.getStatus())) {
                sseEmitterRepository.sendNotification(memberId, "신고가 반려되었습니다. 사유: " + reportUpdateRequest.getReportResult());
            }
        }
        
        
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
	
	public List<ReportMyResponseDto> selectMyReportResponse(Long memberId){
		return reportMapper.selectMyReportResponse(memberId);
	}
	
	public List<ReportMyResponseDto> selectReceivedReportResponse(Long memberId){
		return reportMapper.selectReceivedReportResponse(memberId);
	}
}
	