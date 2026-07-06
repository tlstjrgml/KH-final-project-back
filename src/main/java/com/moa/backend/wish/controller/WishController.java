package com.moa.backend.wish.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.backend.common.config.jwt.CustomUserDetails;
import com.moa.backend.wish.model.vo.Wish;
import com.moa.backend.wish.service.WishService;
import com.moa.backend.wish.model.dto.WishResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wish")
@CrossOrigin(origins = {"http://localhost:5173", "http://3.38.12.241"})
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    private Long getMemberId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMemberId();
    }

    @PostMapping("/{welfareId}")
    public ResponseEntity<String> addWish(@PathVariable("welfareId") Long welfareId) {
        Wish wish = new Wish();
        wish.setMemberId(getMemberId());
        wish.setWelfareId(welfareId);
        wishService.addWish(wish);
        return ResponseEntity.ok("찜 추가");
    }

    @DeleteMapping("/{welfareId}")
    public ResponseEntity<String> removeWish(@PathVariable("welfareId") Long welfareId) {
        Wish wish = new Wish();
        wish.setMemberId(getMemberId());
        wish.setWelfareId(welfareId);
        wishService.removeWish(wish);
        return ResponseEntity.ok("찜 해제");
    }

    @GetMapping("/check/{welfareId}")
    public ResponseEntity<Boolean> checkWish(@PathVariable("welfareId") Long welfareId) {
        Wish wish = new Wish();
        wish.setMemberId(getMemberId());
        wish.setWelfareId(welfareId);
        return ResponseEntity.ok(wishService.checkWish(wish));
    }
    
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishList() {
        return ResponseEntity.ok(wishService.getWishList(getMemberId()));
    }
}