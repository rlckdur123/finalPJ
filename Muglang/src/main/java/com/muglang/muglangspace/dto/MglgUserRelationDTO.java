package com.muglang.muglangspace.dto;


import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgUserRelationDTO {
	private int userId;
	private int followerId;
	private String followDate;
	private int postCount;
	private int followingCount;
	private int followCount;
}
