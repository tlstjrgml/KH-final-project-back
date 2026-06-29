package com.moa.backend.wish.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moa.backend.wish.model.mapper.WishMapper;
import com.moa.backend.wish.model.vo.Wish;
import com.moa.backend.wish.model.dto.WishResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishMapper wishMapper;

    public void addWish(Wish wish) {
        wishMapper.addWish(wish);
    }

    public void removeWish(Wish wish) {
        wishMapper.removeWish(wish);
    }

    public boolean checkWish(Wish wish) {
        return wishMapper.checkWish(wish) > 0;
    }
    
    public List<WishResponseDto> getWishList(Long memberId) {
        return wishMapper.getWishList(memberId);
    }
}