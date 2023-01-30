package com.muglang.muglangspace.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="T_MGLG_USER")
@SequenceGenerator(
		name="MglgUserSequenceGenerator",
		sequenceName="T_MGLG_USER_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
public class MglgUser {
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="MglgUserSequenceGenerator"
	)
	private int userId;
	
	private String userName;
	@Column
	@ColumnDefault("'0000'")
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String address;
	private String bio;
	@Column
	@ColumnDefault("'N'")
	private String userBanYn;
	private LocalDateTime regDate;
	@Column
	@ColumnDefault("'ROLE_USER'")
	private String userRole;
	private String userSnsId;
	private String userNick;

	@Transient
	private String searchCondition;					//검색 조건
	@Transient
	private String searchKeyword;					//검색 키워드
	@Transient
	private int reportCnt;							//신고당한 횟수
	
}
