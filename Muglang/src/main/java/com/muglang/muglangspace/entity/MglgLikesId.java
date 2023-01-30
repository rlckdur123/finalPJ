package com.muglang.muglangspace.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MglgLikesId implements Serializable{
	private int mglgUser;
	private int mglgPost;
}
