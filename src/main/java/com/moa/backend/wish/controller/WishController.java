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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wish")
@CrossOrigin(origins = "http://localhost:5173")
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
    
    //마이페이지에서 복지이름이나 카테고리 같은 것도 보여주려면 WelfareListDTO랑 join해서 반환
    @GetMapping
    public ResponseEntity<List<Wish>> getWishList() {
        return ResponseEntity.ok(wishService.getWishList(getMemberId()));
    }
}