package com.moa.backend.board.service;

import org.springframework.stereotype.Service;

import com.moa.backend.board.model.mapper.BoardMapper;
import com.moa.backend.board.model.vo.Board;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper mapper;

	public Board insertBoard(Board board) {
		mapper.insertBoard(board);
		return board;
	}
}
