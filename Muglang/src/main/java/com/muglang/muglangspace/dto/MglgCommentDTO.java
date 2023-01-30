package com.muglang.muglangspace.dto;

import java.time.LocalDateTime;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgCommentDTO {
	private int commentId;
	private int postId;
	private int userId;
	private String commentContent;
	private String commentDate;

}
