package com.muglang.muglangspace.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_CHAT_MSG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@IdClass(MglgChatMessageId.class)
public class MglgChatMessage {
	@Id
	private String chatRoomId;
	@Id
	private int chatMsgId;
	private int userId;
	private String chatContent;
	private LocalDateTime chatTime = LocalDateTime.now();
	private String roomType;
}
