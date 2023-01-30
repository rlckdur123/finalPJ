package com.muglang.muglangspace.oauth.provider;

//221219 김동현 작업중
//221221 작업끝
//Google, Kakao, Naver 소셜 로그인 기능 구현을 위한 Interface
public interface OAuth2UserInfo {
	String getProviderId(); //소셜 로그인 업체가 제공하는 ID(long으로 옴)
	String getProvider(); //소셜 로그인 제공 업체명
	String getEmail(); //업체로부터 받은 사용자 Email (선택)
	String getName(); //업체로부터 받은 닉네임(필수)
}
