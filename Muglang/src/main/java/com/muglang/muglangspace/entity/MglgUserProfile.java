package com.muglang.muglangspace.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_USER_PROFILE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
@IdClass(MglgUserProfileId.class)
public class MglgUserProfile {
	//외래키로 1대1 대응되는 유저 사진의 정보를 담는 데이터 베이스
	//해당 유저아이디의 프로필은 유저 1명당 1개씩 가짐.
	//파일의 정보를 유저 최초 로그인시 기본 이미지 파일 정보를 DB에 저장하도록 하면됨.
	//N: 변경 없는 상태, U: 변경한 상태 (파일 처리와는 달리 이미지 있던 것을 수정하는 것만 수행)
	@Id
	@OneToOne
	@JoinColumn(name="USER_ID")
	private MglgUser mglgUser;
	@Id
	private String userProfileNm;
	private String userProfileOriginNm;
	private String userProfilePath;
	private String userProfileCate;
	@Transient
	private String userProfileStatus = "N";
	@Transient
	private String newUserProfileNm;
}
