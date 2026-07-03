package com.moa.backend.likes.service;

import org.springframework.stereotype.Service;

import com.moa.backend.likes.model.mapper.LikesMapper;
import com.moa.backend.likes.model.vo.Likes;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {
	private final LikesMapper mapper;

	public boolean checkLikeStatus(Likes likes) {
		int count = mapper.checkLikeStatus(likes);

		return count > 0;
	}

	public void insertLike(Likes likes) {
		mapper.insertLike(likes);
	}

	public void deleteLike(Likes likes) {
		mapper.deleteLike(likes);
	}
}
