package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgBoardDTO {
	private int boardId;

	private int userId;
	
	private String boardTitle;
	private String boardContent;
	private int boardCount;
	private String boardDate;
}
