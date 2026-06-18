package com.moa.backend.common.util.page;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class PageResponse<T> {

    private List<T> content;        // 실제 DB에서 잘라온 데이터 리스트 (Board 등)
    private Pagination pagination;  // 위에서 계산한 페이징 정보

    public PageResponse(List<T> content, Pagination pagination) {
        this.content = content;
        this.pagination = pagination;
    }
}