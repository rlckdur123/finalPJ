package com.muglang.muglangspace.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_CHATROOMS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
public class MglgChatrooms {
	@Id
	private int chatRoomId;
	private String chatRoomName;
}
