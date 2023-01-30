package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgChatMessageDTO {
	private String chatRoomId;
	private int chatMsgId;
	private int userId;
	private String chatContent;
	private String chatTime;
}
