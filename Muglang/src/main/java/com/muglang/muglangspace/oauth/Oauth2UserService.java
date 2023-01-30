package com.muglang.muglangspace.oauth;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.controller.UserController;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.oauth.provider.GoogleUserInfo;
import com.muglang.muglangspace.oauth.provider.KakaoUserInfo;
import com.muglang.muglangspace.oauth.provider.NaverUserInfo;
import com.muglang.muglangspace.oauth.provider.OAuth2UserInfo;
import com.muglang.muglangspace.repository.MglgUserRepository;

import lombok.RequiredArgsConstructor;

@Service //서비스 표시
public class Oauth2UserService extends DefaultOAuth2UserService {
	
	//이미 가입한 회원인지 검사하기위해 MglgUserRepository를 가져옴
	@Autowired
	MglgUserRepository mglgUserRepository;

	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("111111111111111111111");

		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> temp = oAuth2User.getAttributes();
		
		Iterator<String> iter = temp.keySet().iterator(); //이더레이터로 키 값만 저장
		
		while(iter.hasNext()) {
			System.out.println(iter.next());
			System.out.println(userRequest.getAccessToken().getTokenValue());
		}
		
		String userName = ""; //닉네임
		String providerId = "";//업체가 제공한 아이디
		String providerName = userRequest.getClientRegistration().getRegistrationId();//업체이름
		
		OAuth2UserInfo oAuth2UserInfo = null;
		
		//소셜 카테고리 검증 카카오, 네이버, 구글
		if(providerName.equals("kakao")) { //업체의 이름이 카카오라면 유저의 정보를 변수에 담아줌
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
			userName = oAuth2UserInfo.getName();
			providerId = oAuth2UserInfo.getProviderId();		
		} else if(providerName.equals("naver"))  { //업체의 이름이 네이버라면 유저의 정보를 변수에 담아줌
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
			userName = oAuth2UserInfo.getName();
			providerId = oAuth2UserInfo.getProviderId();
		} else if(providerName.equals("google"))  { //업체의 이름이 서브(구글)라면 유저의 정보를 변수에 담아줌
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
			userName = oAuth2UserInfo.getName();
			providerId = oAuth2UserInfo.getProviderId();
		} else {
			System.out.println("소셜 계정을 다시 확인해주세요");
		}
		
		String provider = oAuth2UserInfo.getProvider();
		//userSnsId = kakao_4891279
		String userSnsId = provider + "_" + providerId; //id는 식별 목적, 표시는 닉네임으로
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		//사용자가 이미 소셜 로그인한 기록이 있는지 검사
		MglgUser mglgUser;
		
		System.out.println("userSnsId====================================" + userSnsId);
		
		//userSnsId가 존재하면 true 존재하지 않으면 false로 반환, user 정보를 찾는 것은 userSnsId로 sns로그인의 기록이 있는지 확인한다.
		if(mglgUserRepository.findByUserSnsId(userSnsId) != null) {
			//userId가 존재할 시 정보를 mglgUser 엔티티에 담아줌
			System.out.println("존재하는 회원입니다. 로그인을 진행합니다.");
			mglgUser = mglgUserRepository.findByUserSnsId(userSnsId);
		} else {
			System.out.println("회원이 존재하지 않습니다. 새로 회원을 등록해야합니다.");
			//존재하지 않으면 null로 리턴하여 회원가입
			mglgUser = null;
		}
		if(mglgUser == null) {
			mglgUser = MglgUser.builder()
							   .userSnsId(userSnsId)
							   .userName(userName)
							   .email(email)
							   .userRole(role)
							   .build();
			System.out.println("새로운 회원을 등록할 임시 정보를 취합니다." + mglgUser);
			//세션에 담아 추가적인 정보를 처리하려고 했지만 실패함. 오버라이딩 안하면 자동로딩을 안함.
			//session.setAttribute("loginUser", mglgUser);
			//추가정보 입력한 뒤, 로그인 처리를 마무리 할 예정
			
			//mglgUserRepository.save(mglgUser);
		}
		

		//Spring Security의 in-momory 세션 저장소인
		//SecurityContextHolder에 인증 정보 저장 <- 들어갈 수 있는 인증객체는 Authentication(인증)
		//Authentication은 AbstractAuthenticationToken으로 구현되어 있고
		//AbstractAuthenticationToken은 OAuth2LoginAuthenticationToken이 구현
		//반환한 OAuth2User를 Spring이 OAuth2LoginAuthenticationToken으로 변환
		//OAuth2LoginAuthenticationToken은 Authentication의 자식이므로 SecurityContextHolder에 저장가능
		//아래의 과정이 SecurityContextHolder에 우리가 반환한 소셜 계정 정보를 저장하는 장면
		System.out.println("로그인 정보를 저장완료하였습니다.");
		//SecurityContext에 인증 정보 저장
		return CustomUserDetails.builder()
								.mglgUser(mglgUser)
								.attributes(oAuth2User.getAttributes())
								.build();
	}
	
}
