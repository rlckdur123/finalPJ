package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgPostDTO {

	private int postId;

	private int userId;

	private int postRating;
	private String postContent;
	private String restNm;
	private int restRating; 
	private String hashTag1;
	private String hashTag2;
	private String hashTag3;
	private String hashTag4;
	private String hashTag5;
	private String postDate;
	private long betweenDate;
	private String searchKeyword;
	private int likeCnt;
	private int postLike;
}
