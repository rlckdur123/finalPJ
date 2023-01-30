package com.muglang.muglangspace.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MglgCommentId implements Serializable{
	private int commentId;
	private int mglgPost;
}
