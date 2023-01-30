package com.muglang.muglangspace.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_REPORT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@IdClass(MglgReportId.class)
public class MglgReport {
	@Id
	private int reportId; //신고 번호
	@Id
	private int reportType; //신고 타입 번호
	private int sourceUserId; //신고한 사람 아이디값 String
	private int targetUserId; // 신고당한 사람 아이디값 String
	private LocalDateTime reportDate = LocalDateTime.now();
	private int postId; //포스트 번호
	private int commentId; //포스트 댓글의 번호
	
	@Transient
	private int count;
}
