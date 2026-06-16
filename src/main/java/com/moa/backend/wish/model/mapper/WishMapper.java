package com.moa.backend.wish.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.wish.model.vo.Wish;

@Mapper
public interface WishMapper {
    void addWish(Wish wish);
    void removeWish(Wish wish);
    int checkWish(Wish wish);
    List<Wish> getWishList(Long memberId);
}