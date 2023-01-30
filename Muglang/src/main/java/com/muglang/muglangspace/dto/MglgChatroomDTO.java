package com.muglang.muglangspace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgChatroomDTO {
	private int chatroomId;
	private int part1;
	private int part2;
	private String roomDatetime;
	private String part1LeaveDateTime;
	private String part2LeaveDateTime;
}