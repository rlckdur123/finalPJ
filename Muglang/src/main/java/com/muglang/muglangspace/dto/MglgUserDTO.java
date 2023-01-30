package com.muglang.muglangspace.dto;

import javax.persistence.Column;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgUserDTO {
	private int userId;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String address;
	private String bio;
	private String userBanYn;
	private String regDate;
	private String userRole;
	private String userSnsId;
	private String userNick;
	private String searchCondition;
	private String searchKeyword;
	private int reportCnt;

}
