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
@Table(name="T_MGLG_CHAT_MEMBERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
@IdClass(MglgChatMembersId.class)
public class MglgChatMembers {
	@Id
	private String chatRoomId;
	@Id
	private int userId;
	private LocalDateTime enterDate = LocalDateTime.now();
	private LocalDateTime leaveDate = LocalDateTime.now();
	
}
