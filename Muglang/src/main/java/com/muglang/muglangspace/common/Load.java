package com.muglang.muglangspace.common;

import java.time.Duration;
import java.time.LocalDateTime;

import com.muglang.muglangspace.dto.MglgPostDTO;
import com.muglang.muglangspace.dto.MglgPostFileDTO;
import com.muglang.muglangspace.dto.MglgUserDTO;
import com.muglang.muglangspace.dto.MglgUserProfileDTO;
import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.entity.MglgUserProfile;


public class Load {
	//해당 메소드는 로그인 유저의 정보를 화면단에 출력하기 위해 HTML로 이동하기위해
	//DTO 데이터로 만들 때 사용하는 함수.
	//예) MglgUser의 정보를 꺼내고, 해당 객체를 MglgUserDTO로 바꿀 때 사용하면 됩니다.
	public static MglgUserDTO toHtml(MglgUser user) {
		MglgUserDTO info = MglgUserDTO.builder()
									  .userId(user.getUserId())
									  .userName(user.getUserName())
									  .userRole(user.getUserRole())
									  .firstName(user.getFirstName())
									  .lastName(user.getLastName())
									  .bio(user.getBio())
									  .regDate(user.getRegDate().toString())
									  .email(user.getEmail())
									  .address(user.getAddress())
									  .phone(user.getPhone())
									  .userNick(user.getUserNick())
									  .userSnsId(user.getUserSnsId())
									  .build();
		return info;
	}
	
	//Post관련 DTO로 변환하는 메소드. 화면단에 출력하기 위해 HTML에서 임시로 사용하라는 DTO 데이터 반환.
	public static MglgPostDTO toHtml(MglgPost mglgPost, MglgUser loginUser) {
		MglgPostDTO info = MglgPostDTO.builder()
					 .postId(mglgPost.getPostId())
					 .userId(loginUser.getUserId())
					 .restNm(mglgPost.getRestNm())
					 .postDate(mglgPost.getPostDate().toString())
					 .postRating(mglgPost.getPostRating())
					 .restRating(mglgPost.getRestRating())
			          .hashTag1(mglgPost.getHashTag1() == null || mglgPost.getHashTag1().equals("")  ? "": mglgPost.getHashTag1())
						.hashTag2(mglgPost.getHashTag2() == null || mglgPost.getHashTag2().equals("")  ? "": mglgPost.getHashTag2())
						.hashTag3(mglgPost.getHashTag3() == null || mglgPost.getHashTag3().equals("")  ? "": mglgPost.getHashTag3())
						.hashTag4(mglgPost.getHashTag4() == null || mglgPost.getHashTag4().equals("")  ? "": mglgPost.getHashTag4())
						.hashTag5(mglgPost.getHashTag5() == null || mglgPost.getHashTag5().equals("")  ? "": mglgPost.getHashTag5())
					 .postContent(mglgPost.getPostContent())
					 .betweenDate(Duration.between(mglgPost.getPostDate(), LocalDateTime.now()).getSeconds())
					 .build();
		return info;
	}
	
	//PostFile 관련 DTO로 변환하는 메소드. 파일들을 Entity로 바로 사용하지 않고, 따로 변환하기 위한 메소드.
	public static MglgPostFileDTO toHtml(MglgPostFile mglgPostFile) {
		MglgPostFileDTO info = MglgPostFileDTO.builder()
											  .postFileId(mglgPostFile.getPostFileId())
											  .postFileNm(mglgPostFile.getPostFileNm())
											  .postFilePath(mglgPostFile.getPostFilePath())
											  .postFileRegdate(mglgPostFile.getPostFileRegdate().toString())
											  .postFileOriginNm(mglgPostFile.getPostFileOriginNm())
											  .postFileCate(mglgPostFile.getPostFileCate())
											  .postFileStatus(mglgPostFile.getPostFileStatus())
											  .build();
		return info;
	}
	
	public static MglgUserProfileDTO toHtml(MglgUserProfile mglgUserProfile) {
		MglgUserProfileDTO info = MglgUserProfileDTO.builder()
													.userId(mglgUserProfile.getMglgUser().getUserId())
													.userProfileNm(mglgUserProfile.getUserProfileNm())
													.userProfileOriginNm(mglgUserProfile.getUserProfileOriginNm())
													.userProfilePath(mglgUserProfile.getUserProfilePath())
													.userProfileCate(mglgUserProfile.getUserProfileCate())
													.build();
		
		return info;
	}
	
}
