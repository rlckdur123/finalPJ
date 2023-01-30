package com.muglang.muglangspace.service.mglgboard.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.entity.MglgBoard;
import com.muglang.muglangspace.repository.MglgBoardRepository;
import com.muglang.muglangspace.service.mglgboard.MglgBoardService;

@Service
public class MglgBoardServiceImpl implements MglgBoardService{
	@Autowired
	MglgBoardRepository mglgBoardRepository;

	@Override
	public List<MglgBoard> getFAQList() {

		return mglgBoardRepository.findAll();
	}

	@Override
	public void deleteBoard(int boardId) {
		mglgBoardRepository.deleteByBoardId(boardId); 
	}

	@Override
	public MglgBoard getBoard(MglgBoard board) {
		
		return mglgBoardRepository.findById(board.getBoardId()).get();
	}

	@Override
	public void updateBoard(MglgBoard board) {
		String boardTitle = board.getBoardTitle();
		String boardContent = board.getBoardContent();
		int boardId = board.getBoardId();
		mglgBoardRepository.updateBoard(boardTitle,boardContent,boardId);
		
	}

	@Override
	public void deleteBoard(MglgBoard board) {
		int boardId = board.getBoardId();
		mglgBoardRepository.deleteBoard(boardId);
	}

	@Override
	public void insertBoard(MglgBoard board) {
		String boardTitle = board.getBoardTitle();
		String boardContent = board.getBoardContent();
		mglgBoardRepository.insertBoard(boardTitle,boardContent);

	}
}
