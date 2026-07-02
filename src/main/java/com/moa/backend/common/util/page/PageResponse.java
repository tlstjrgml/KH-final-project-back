package com.moa.backend.common.util.page;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class PageResponse<T> {

    private List<T> content;        
    private Pagination pagination;   

    public PageResponse(List<T> content, Pagination pagination) {
        this.content = content;
        this.pagination = pagination;
    }
}