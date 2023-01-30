package com.muglang.muglangspace.service.mglgboard;

import java.util.List;

import com.muglang.muglangspace.entity.MglgBoard;

public interface MglgBoardService {
	List<MglgBoard> getFAQList();
	void deleteBoard(int boardId); 
	MglgBoard getBoard(MglgBoard board);
	void updateBoard(MglgBoard board);
	void deleteBoard(MglgBoard board);
	void insertBoard(MglgBoard board);
}
