package com.muglang.muglangspace.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MglgChatMembersId implements Serializable {
	private int chatRoomId;
	private int userId;
}
