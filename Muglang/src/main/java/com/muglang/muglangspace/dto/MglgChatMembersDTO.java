package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgChatMembersDTO {
	private String chatRoomId;
	private int userId;
	private String enterDate;
	private int leaveDate;
}
