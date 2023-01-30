package com.muglang.muglangspace.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MglgPostFileId implements Serializable{
	private int mglgPost;
	private int postFileId;
}
