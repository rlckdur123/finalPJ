package com.muglang.muglangspace.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.muglang.muglangspace.oauth.Oauth2UserService;

@Configuration //세큐리티 환경설정 표시 - 필터 체인 구현 - 사용자 역할(관리자, 사용자)에 따른 접근 권한 설정
@EnableWebSecurity //웹 세큐리티 실행가능 표시 (WebSecurityConfiguration, SpringWebMvcImportSelector, OAuth2ImportSelector 클래스 사용가능 하게 해줌)
public class SecurityConfiguration {
	
	@Autowired
	private Oauth2UserService oauth2UserService;
	
	
	//필터 체인 구현(HttpSecurity 객체 사용)
	@Bean //외부객체를 끌어올 때는 Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//권한에 따른 요청주소 매핑
		http.authorizeRequests()
								.antMatchers("/**").permitAll() 			//들어오는 모든 사용자 허용
								.antMatchers("/user/**").permitAll()	//"/user"(시작 페이지)으로 시작하는 요청 리소스, 모든 사용자 허용
								//정적 리소스 권한처리 추후 수정(22/12/19)
								.antMatchers("/css/**").permitAll()		//css
								.antMatchers("/js/**").permitAll()		//자바스크립트
								.antMatchers("/images/**").permitAll()	//이미지들
								.antMatchers("/upload/**").permitAll()	//사용자가 올린 이미지
								.antMatchers("/post/**").permitAll()	//포스트 관련
								.antMatchers("/assets/**").permitAll()  //부트스트랩 관련
								.antMatchers("/home/**").permitAll()
								//권한을 가진 유저들만 접근 할 수 있는 요청 리소드들

								.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")//관리자 페이지는 관리자만 조회가능
//								.antMatchers("/index/**").access("hasAnyRole('ROLE_USER', ROLE_ADMIN')")//index = main 페이지는 사용자, 관리자 모두 조회가능
//								.antMatchers("/profile/**").access("hasAnyRole('ROLE_USER', ROLE_ADMIN')")//profile 페이지는 사용자, 관리자 모두 조회가능

									
								//나머지 요청 리소스들은 인정된 사용자만 접근 할 수 있게 설정
								.anyRequest().authenticated();
		
		//로그인 설정
		//회원가입이 없으므로 바로 OAUTH2기반 로그인 처리
		http.oauth2Login()
			.loginPage("/user/login")
			.defaultSuccessUrl("/user/socialLoginPage")	//이 url은 templates 폴더 하위의 링크
			.userInfoEndpoint() //업체로 부터 받은 사용자 정보를 다 받아온 포인트
			.userService(oauth2UserService);

//		//회원가입을 대신하는 추가정보 입력후 로그인.
//		http.oauth2Login()
//			.loginPage("/user/socialLogin")
//			.defaultSuccessUrl("/post/mainPost")
//			.userInfoEndpoint()
//			.userService(oauth2NewUserService);
		
		//로그아웃 설정
		http.logout()
			.logoutUrl("/user/logout")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/user/login");
			
		http.csrf().disable();
			
		return http.build();	
	}
}
