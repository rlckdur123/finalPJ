package com.muglang.muglangspace.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
	Map<String, Object> attributes; //사용자 계정 속성을 담아줌
	Map<String, Object> properties;	//사용자 계정 정보를 담아줌
	
	public NaverUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.properties = (Map<String, Object>)attributes.get("response");
	}
	
	//네이버가 제공하는 id를 받아옴
	@Override
	public String getProviderId() {
		return properties.get("id") + "";
	}
	
	//네이버
	@Override
	public String getProvider() {
		return "naver";
	}
	
	//네이버가 제공하는 사용자의 이메일을 받아옴(필수)
	@Override
	public String getEmail() {
		return properties.get("email") + "";
	}
	
	//네이버가 제공하는 사용자의 닉네임을 받아옴(필수)
	@Override
	public String getName() {
		return properties.get("nickname") + "";
	}

}
