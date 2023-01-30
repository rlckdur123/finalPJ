package com.muglang.muglangspace.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
	Map<String, Object> attributes; //사용자 계정 속성을 담아줌
	Map<String, Object> properties; //사용자 계정 정보를 담아줌
	
	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.properties = (Map<String, Object>)attributes.get("kakao_account");
	}
	
	//카카오가 제공하는 id를 받아옴
	@Override
	public String getProviderId() {
		return attributes.get("id") + "";
	}
	
	//카카오
	@Override
	public String getProvider() {
		return "kakao";
	}
	
	//카카오가 제공하는 사용자의 이메일을 받아옴(선택)
	@Override
	public String getEmail() {
		return properties.get("email") + "";
	}
	
	//카카오가 제공하는 사용자의 닉네임을 받아옴(필수)
	@Override
	public String getName() {
		Map<String, Object> profile = (Map<String, Object>)properties.get("profile");
		return profile.get("nickname") + "";
	}
	
	
}
