package com.moa.backend.common.util.page;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Pagination {

    private int currentPage;    
    private int limit;          
    private int totalItems;     
    private int totalPages;     
    private int startPage;     
    private int endPage;       
    private boolean hasPrev;    
    private boolean hasNext;   

    public Pagination(PageRequest pageRequest, int totalItems) {
        this.currentPage = pageRequest.getPage();
        this.limit = pageRequest.getLimit();
        this.totalItems = totalItems;

        this.totalPages = (int) Math.ceil((double) totalItems / limit);

        int pageBlockSize = 10;

        this.startPage = ((currentPage - 1) / pageBlockSize) * pageBlockSize + 1;
        this.endPage = startPage + pageBlockSize - 1;

        if (endPage > totalPages) {
            endPage = totalPages;
        }
        if (endPage == 0) {
            endPage = 1;
        }

        this.hasPrev = this.startPage > 1;
        this.hasNext = this.endPage < totalPages;
    }
}

