package com.muglang.muglangspace.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MglgChatMessageId implements Serializable {
	private String chatRoomId;
	private int chatMsgId;
}
