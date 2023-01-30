package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MglgUserProfileDTO {
	private int userId;
	private String userProfileNm;
	private String userProfileOriginNm;
	private String userProfilePath;
	private String userProfileCate;
	private String userProfileStatus;
	private String newUserProfileNm;
}
