package com.moa.backend.likes.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.moa.backend.likes.model.vo.Likes;

@Mapper
public interface LikesMapper {

	int checkLikeStatus(Likes likes);

	void insertLike(Likes likes);

	void deleteLike(Likes likes);

}
