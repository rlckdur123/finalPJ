package com.muglang.muglangspace.oauth.provider;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
	Map<String, Object> attributes; //사용자 계정 속성을 담아줌
	
	public GoogleUserInfo(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	
	//구글이 제공하는 id를 받아옴
	@Override
	public String getProviderId() {
		return attributes.get("sub") + "";
	}
	
	//구글
	@Override
	public String getProvider() {
		return "google";
	}
	
	//구글이 제공하는 사용자의 이메일을 받아옴
	@Override
	public String getEmail() {
		return attributes.get("email") + "";
	}
	
	//구글이 제공하는 사용자의 이름을 받아옴
	@Override
	public String getName() {
		return attributes.get("name") + "";
	}
	
	
}
