package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgReportDTO {
	private int reportId; //신고 번호
	private int reportType; //신고 타입 번호
	private int sourceUserId; //신고한 사람 아이디값 String
	private int targetUserId; //신고당한 사람 아이디값 String
	private String reportDate;
	private int postId; //포스트 번호
	private int commentId; //포스트 댓글의 번호
	private int count;	//신고 누적 개수
}
