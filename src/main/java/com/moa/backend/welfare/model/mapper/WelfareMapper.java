package com.moa.backend.welfare.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.welfare.model.vo.Welfare;

@Mapper
public interface WelfareMapper {
    void insertWelfare(Welfare vo);
}