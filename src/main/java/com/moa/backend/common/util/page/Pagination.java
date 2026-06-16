package com.moa.backend.common.util.page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Pagination {

    private int currentPage;   // 현재 페이지 번호
    private int limit;         // 한 페이지당 보여줄 개수
    private int totalItems;    // 전체 데이터(게시글) 개수
    private int totalPages;    // 전체 페이지 개수
    private int startPage;     // 하단 네비게이션 시작 번호 (예: [1], [11], [21])
    private int endPage;       // 하단 네비게이션 끝 번호 (예: [10], [20], [30])
    private boolean hasPrev;   // 이전 페이지 블록 존재 여부
    private boolean hasNext;   // 다음 페이지 블록 존재 여부

    // 생성자를 실행할 때 모든 계산이 자동으로 끝나게 설계합니다.
    public Pagination(PageRequest pageRequest, int totalItems) {
        this.currentPage = pageRequest.getPage();
        this.limit = pageRequest.getLimit();
        this.totalItems = totalItems;

        // 1. 전체 페이지 수 계산 (예: 142개 글 / 10 = 14.2 -> 올림해서 15페이지)
        this.totalPages = (int) Math.ceil((double) totalItems / limit);

        // 2. 하단에 보여줄 페이지 버튼 개수 설정 (예: [1] ~ [10] 까지 보여주기)
        int pageBlockSize = 10;

        // 3. 현재 페이지 기준 시작/끝 페이지 계산
        // 현재 3페이지면? ((3-1)/10)*10 + 1 = 1페이지가 시작
        this.startPage = ((currentPage - 1) / pageBlockSize) * pageBlockSize + 1;
        this.endPage = startPage + pageBlockSize - 1;

        // 4. 끝 페이지가 전체 페이지 수보다 크면 맞춰주기 (예: 전체 15뿐인데 20까지 나오면 안됨)
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        if (endPage == 0) {
            endPage = 1;
        }

        // 5. 이전/다음 버튼 활성화 여부
        this.hasPrev = this.startPage > 1;
        this.hasNext = this.endPage < totalPages;
    }
}