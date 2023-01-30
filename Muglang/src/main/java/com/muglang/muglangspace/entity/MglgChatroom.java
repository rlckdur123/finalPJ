package com.muglang.muglangspace.entity;



import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_CHATROOM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
public class MglgChatroom {
	@Id
	private String chatroomId;
	private int part1;
	private int part2;
	private LocalDateTime roomDatetime;
	private LocalDateTime part1LeaveDateTime;
	private LocalDateTime part2LeaveDateTime;
	}
