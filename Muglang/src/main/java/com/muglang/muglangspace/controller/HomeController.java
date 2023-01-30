package com.muglang.muglangspace.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.entity.CustomUserDetails;

//index.html에서 login과정을 자동으로 진행한뒤 이곳을 매핑하여 계정확인을 다시 수행함.
//로그인 하려하는 메인 페이지 이동.
@RestController
@RequestMapping("/home")
public class HomeController {
	//로그인 정보 확인하여 메인 페이지 표시 제어
	@RequestMapping("/home")
	public void home(HttpSession session, HttpServletResponse response) throws IOException {
		if(session.getAttribute("loginUser") == null) {	//세션정보가 없을 경우(로그아웃이 되어있는 경우)
			System.out.println("로그인을 하지 않았습니다. 로그인 페이지로 이동합니다.");
			response.sendRedirect("/user/login");
		} else {
			System.out.println("환영합니다. 메인 페이지로 이동합니다.");
			response.sendRedirect("/post/mainPost");
		}
	}
}
