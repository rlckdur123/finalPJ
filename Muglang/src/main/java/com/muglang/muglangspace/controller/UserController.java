package com.muglang.muglangspace.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.common.FileUtils;
import com.muglang.muglangspace.common.Load;
import com.muglang.muglangspace.dto.MglgResponseDTO;
import com.muglang.muglangspace.dto.MglgUserDTO;
import com.muglang.muglangspace.dto.MglgUserProfileDTO;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.entity.MglgUserProfile;
import com.muglang.muglangspace.service.mglghotkeywords.MglgHotKeywordsService;
import com.muglang.muglangspace.service.mglgpost.MglgPostService;
import com.muglang.muglangspace.service.mglgsocial.UserRelationService;
import com.muglang.muglangspace.service.mglguser.MglgUserService;
import com.muglang.muglangspace.service.mglguserprofile.MglgUserProfileService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private MglgUserService mglgUserService;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private MglgUserProfileService mglgUserProfileService;
	//?????? ?????? ?????????
	@Autowired
	private MglgPostService mglgPostService;
	
	@Autowired
	private MglgHotKeywordsService mglgHotKeywordsService;

	@GetMapping("/profile")
	public ModelAndView profileView(@AuthenticationPrincipal CustomUserDetails customUser,@PageableDefault(page = 0, size = 5) Pageable pageable) {
		int userId = customUser.getMglgUser().getUserId();
		Page<CamelHashMap> requestFollowList = userRelationService.requestFollowList(userId,pageable);
		int i =0;
		for(CamelHashMap a : requestFollowList) {
			int getUserId = (int) requestFollowList.getContent().get(i).get("userId");
			MglgUserProfile profileImg = mglgUserProfileService.getUserImg(getUserId);
			requestFollowList.getContent().get(i).put("profileImg", profileImg);
			if(i==5) {
				i=0;
			}else {
				i++;
			}
		}
		long followCnt = requestFollowList.getTotalElements();
		

		ModelAndView mv = new ModelAndView();
		MglgUserDTO user = MglgUserDTO.builder()
								.userNick(customUser.getMglgUser().getUserNick())
								.userName(customUser.getMglgUser().getUserName())
								.email(customUser.getMglgUser().getEmail())
								.firstName(customUser.getMglgUser().getFirstName())
								.lastName(customUser.getMglgUser().getLastName())
								.address(customUser.getMglgUser().getAddress())
								.phone(customUser.getMglgUser().getPhone())
								.userId(customUser.getMglgUser().getUserId())
								.bio(customUser.getMglgUser().getBio())
								.regDate(customUser.getMglgUser().getRegDate().toString())
								.userSnsId(customUser.getMglgUser().getUserSnsId())
								.build();
		//???????????? ???????????? ????????????
		

		mv.addObject("followCnt", followCnt);
		mv.addObject("requestList", requestFollowList);
		mv.addObject("user", user);
		mv.setViewName("profile.html");
		return mv;
	}
	//??????????????? ??????
	@GetMapping("/profileAjax")
	public ResponseEntity<?> profileAjax(@AuthenticationPrincipal CustomUserDetails customUser,@PageableDefault(page = 0, size = 5)Pageable pageable) {
		int userId = customUser.getMglgUser().getUserId();
		MglgResponseDTO<CamelHashMap> response = new MglgResponseDTO<>();
		try {
			int i =0;
			Page<CamelHashMap> requestFollowList = userRelationService.requestFollowList(userId,pageable);
			for(CamelHashMap a : requestFollowList) {
				int getUserId = (int) requestFollowList.getContent().get(i).get("userId");
				MglgUserProfile profileImg = mglgUserProfileService.getUserImg(getUserId);
				requestFollowList.getContent().get(i).put("profileImg", profileImg);
				if(i==5) {
					i=0;
				}else {
					i++;
				}
			}
			
			
			
			response.setPageItems(requestFollowList);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	
	//??????????????? ???????????? ?????? ??????
	//???????????? ????????? post??????????????? ?????????.
	@GetMapping("/myBoard")
	public ModelAndView myBoardList(@AuthenticationPrincipal CustomUserDetails loginUser) {
			ModelAndView mv = new ModelAndView();
			MglgUserDTO loginUserDTO = Load.toHtml(loginUser.getMglgUser());
			
			// ?????? ????????? ????????????
			List<CamelHashMap> hotKeywords = mglgHotKeywordsService.getHotKeywords();
			mv.addObject("hotKeywords", hotKeywords);
			mv.addObject("loginUser", loginUserDTO);
			mv.setViewName("/user/myBoard.html");
			System.out.println("????????? ?????? ?????? ?????????--------------!!!!!!!!!!!!!!");
			return mv;
	}
	
	// ???????????? ??????
	// ?????? ?????? ???????????? + ?????????
	@GetMapping("/follower")
	public ModelAndView followList(@AuthenticationPrincipal CustomUserDetails loginUser) {
			int userId = loginUser.getMglgUser().getUserId();
			ModelAndView mv = new ModelAndView();

			mv.addObject("userId", userId);
			mv.setViewName("/user/follower.html");
			
			return mv;
	}

	// ??????????????? ??????
	@GetMapping("/following")
	public ModelAndView followingList(@AuthenticationPrincipal CustomUserDetails loginUser) {
		int userId = loginUser.getMglgUser().getUserId();
		ModelAndView mv = new ModelAndView();

		//mv.setViewName("/user/follower.html");
		mv.addObject("userId", userId);
		mv.setViewName("/user/following.html");
			
			
		return mv;
	}



		//?????? ????????? ??????
		// ?????? ?????? ???????????? + ?????????
		@GetMapping("/getAdminUserList")
		public ModelAndView getAdminUserList(MglgUserDTO userDTO, @PageableDefault(page = 0, size = 10) Pageable pageable) {

			MglgUser user = MglgUser.builder()
						   .searchCondition(userDTO.getSearchCondition())
						   .searchKeyword(userDTO.getSearchKeyword())
						   .build();
			
			Page<CamelHashMap> pageUserList = mglgUserService.getAdminUserList(user, pageable);
			Page<MglgUserDTO> pageUserDTOList = pageUserList.map(pageUser -> 
														MglgUserDTO.builder()
																	.userId((int)pageUser.get("userId"))
																	.userName(String.valueOf(pageUser.get("userName")))
																	.password(String.valueOf(pageUser.get("password")))
																	.firstName(String.valueOf(pageUser.get("firstName")))
																	.lastName(String.valueOf(pageUser.get("lastName")))
																	.phone(String.valueOf(pageUser.get("phone")))
																	.email(String.valueOf(pageUser.get("email")))
																	.address(String.valueOf(pageUser.get("address")))
																	.bio(String.valueOf(pageUser.get("bio")))
																	.userBanYn(String.valueOf(pageUser.get("userBanYn")))
																	.regDate(String.valueOf(pageUser.get("regDate")) == null ?
																		   	null :
																		   		String.valueOf(pageUser.get("regDate")))
																	.reportCnt(Integer.valueOf(String.valueOf(pageUser.get("reportCnt"))))
																	.build()
															);

						ModelAndView mv = new ModelAndView();
						
						mv.setViewName("/admin/adminUser.html");
						
						mv.addObject("getUserList", pageUserDTOList);
						
						if(userDTO.getSearchCondition() != null && !userDTO.getSearchCondition().equals("")) {
							mv.addObject("searchCondition", userDTO.getSearchCondition());
						}
						
						if(userDTO.getSearchKeyword() != null && !userDTO.getSearchKeyword().equals("")) {
							mv.addObject("searchKeyword", userDTO.getSearchKeyword());
						}
						
						return mv;
		}//getUserList???
	
	
	//????????? ??? ??????
	@GetMapping("/login")
	public ModelAndView loginView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/login.html");
		return mv;
	}
	
	//?????? ???????????? ???????????????.
	//?????? ????????? ???????????????, ???????????? ????????? ????????? ???????????? ???????????? ???????????????.
	//??????????????? api?????? ????????? ??? ??? ???????????? ???????????? ????????? ????????? ???????????????.
	@RequestMapping("/socialLoginPage")
	public ModelAndView socialLoginInput(HttpServletResponse response, HttpSession session, SecurityContextHolder security) throws IOException {

		System.out.println(SecurityContextHolder.getContext());
		
		ModelAndView mv = new ModelAndView();
		
		CustomUserDetails userInfo = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//???????????? ?????? ?????? ???????????? ????????? ???????????? ??????.
		if(userInfo.getMglgUser().getRegDate() != null) {
			System.out.println("?????? ????????? ??????????????????.");
			System.out.println("????????? ???????????? ?????? : " + userInfo.getMglgUser().getUserId() + ", " + userInfo.getMglgUser().getEmail());
			MglgUser loginUser = mglgUserService.loginUser(userInfo.getMglgUser());
			MglgUserDTO loginUserDTO = MglgUserDTO.builder()
												  .userId(loginUser.getUserId())
												  .userName(loginUser.getUserName())
												  .userSnsId(loginUser.getUserSnsId())
												  .regDate(loginUser.getRegDate().toString())
												  .email(loginUser.getEmail())
												  .userRole(loginUser.getUserRole())
												  .build();
			
			session.setAttribute("loginUser", loginUserDTO);
			response.sendRedirect("/post/mainPost");
			//mv.setViewName("post/post.html");
		} else { //?????? ????????? ?????? ??????
			System.out.println("?????????????????????.");
			mv.setViewName("user/socialLoginPage.html");
		}
		return mv;
	}
	
	//??????????????? ????????? ????????? ???????????? ?????????
	//????????? ????????? ????????? ??????????????? ????????? ???????????? ???????????? ????????? ???????????? ?????? ???????????? ??????.
	//?????? ????????? ???????????? ????????? ????????? ?????? ????????? ???????????? ?????????????????????.
	@PostMapping("/socialNewLogin")
	public void socialLoginView(MglgUserDTO mglgUserDTO, HttpSession session,
			HttpServletResponse response,HttpServletRequest request) throws IOException {
		CustomUserDetails userInfo = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
//		System.out.println("?????????????????? ?????? ?????? : " + userInfo.getAttributes().keySet());
		
		MglgUser newUser = MglgUser.builder()
						   .userName(userInfo.getMglgUser().getUserName())
						   .userSnsId(userInfo.getMglgUser().getUserSnsId())
						   .userBanYn("N")
						   //.phone(userInfo.getAttributes().) //?????? ???????????? ???????????? ??????
						   .firstName(mglgUserDTO.getFirstName())
						   .lastName(mglgUserDTO.getLastName())
						   .email(userInfo.getMglgUser().getEmail())
						   .userNick(mglgUserDTO.getUserNick())
						   .regDate(LocalDateTime.now())
						   .userRole(userInfo.getMglgUser().getUserRole())
						   .build();

		newUser = mglgUserService.socialLoginProcess(newUser);
		
		//????????? ????????? ????????? ?????? ??????
		String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";
		 mglgUserProfileService.insertDefault(newUser.getUserId(),attachPath);
		
		
		System.out.println("??????????????? ??????????????????. ??????????????? ???????????????.");
		//???????????? ????????? ?????? ????????? ???????????? ?????? DTO??? ?????? ???????????? ???????????????.
//		MglgUser loginUser = mglgUserService.socialLoginUser(newUser);
//		MglgUserDTO loginUserDTO = MglgUserDTO.builder()
//											  .userId(loginUser.getUserId())
//											  .userName(loginUser.getUserName())
//											  .userSnsId(loginUser.getUserSnsId())
//											  .regDate(loginUser.getRegDate().toString())
//											  .email(loginUser.getEmail())
//											  .userRole(loginUser.getUserRole())
//											  .build();
//		session.setAttribute("loginUser", loginUserDTO);
		//???????????? ??????????????? ?????? ???(?????? ??? ?????? ???????????? ?????? )
		CustomUserDetails customUserDetails = mglgUserService.loadByUserId(newUser.getUserId());
		
		Authentication authetication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		securityContext.setAuthentication(authetication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		
		response.sendRedirect("/post/mainPost");

	}
	//?????? ?????? -----?????? ??????????????? ??????
	@PostMapping("reportUser")
	public void reportUser(String msg, String url,HttpServletResponse response,@RequestParam("userId") int postUserId,@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		url = "/post/mainPost";
		reportUserBase(msg,url,response,postUserId,loginUser);
	}
	//?????? ?????? ----- ???????????? ??????????????? ??????
	@PostMapping("reportUserFollow")
	public void reportUserFollow(String msg, String url,HttpServletResponse response,@RequestParam("userId") int postUserId,@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		url = "/social/otherUser?userId="+postUserId;
		reportUserBase(msg,url,response,postUserId,loginUser);
	}
	//?????? ?????? ????????? ?????????
	public void reportUserBase(String msg, String url,HttpServletResponse response,int postUserId,@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		int userId = loginUser.getMglgUser().getUserId();
		msg = mglgUserService.reportUser(postUserId,userId);
		if(msg.equals("self")) {
			msg="?????? ????????? ????????? ??? ????????????.";
		}else if(msg.equals("success")) {
			msg= postUserId+"??? ????????? ??????????????????.";
		}else {
			msg= "??? ????????? ???????????? ????????? ??? ????????????.";
		}
	    try {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter w = response.getWriter();
	        w.write("<script>alert('"+msg+"');location.href='"+url+"';</script>");
			w.flush();
			w.close();
	    } catch(Exception e) {
			e.printStackTrace();
	    }
	}
	//??????????????? ?????? ?????? ?????????(???????????? ????????? securityFilter??? ?????? ?????? ??????.)
	@GetMapping("/logout")
	public void logout() {
		
	}
	//?????? ?????? ???????????? ???
	@PostMapping("/updateUser")
	public void updateUser(MglgUserDTO mglgUserDTO, HttpSession session,HttpServletRequest request,
		HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser,MultipartFile[] profileUpload) throws IOException {
		System.out.println("???????????? ==="+profileUpload);
		MglgUser user = MglgUser.builder()
						   .userNick(mglgUserDTO.getUserNick())
						   .userName(customUser.getMglgUser().getUserName())
						   .bio(mglgUserDTO.getBio())
						   .address(mglgUserDTO.getAddress())
						   .phone(mglgUserDTO.getPhone())
						   .email(customUser.getMglgUser().getEmail())
						   .firstName(customUser.getMglgUser().getFirstName())
						   .lastName(customUser.getMglgUser().getLastName())
						   .userId(customUser.getMglgUser().getUserId())
						   .userSnsId(customUser.getMglgUser().getUserSnsId())
						   .regDate(customUser.getMglgUser().getRegDate())
						   .userRole(customUser.getMglgUser().getUserRole())
						   .userBanYn(customUser.getMglgUser().getUserBanYn())				
						   .build();

		
		List<MglgUserProfile> uploadFileList = new ArrayList<MglgUserProfile>();		
		if(profileUpload.length > 0) {
			String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";
			
			File directory = new File(attachPath);
			
			if(!directory.exists()) {
				directory.mkdir();
			}
			//????????? ?????? ?????? ????????? ????????? DB??? ?????????.
			for(int i=0; i < profileUpload.length; i++) {
				MultipartFile file = profileUpload[i];
				
				if(!file.getOriginalFilename().equals("") && file.getOriginalFilename() != null) {
					MglgUserProfile mglgUserProfile = FileUtils.parseProfileInfo(file, attachPath);
					mglgUserProfile.setMglgUser(user);	//????????? ????????? id ????????? ??????.
					System.out.println("p----------------------="+mglgUserProfile);
					uploadFileList.add(mglgUserProfile);
					mglgUserProfileService.updateProfileFile(mglgUserProfile);	//????????? ????????? ????????? ?????? ??? ????????? ??????.
				}
			}
			
		}
		System.out.println("?????? ?????? ?????? ??????");
		
		
		MglgUser updateUser = mglgUserService.updateUser(user);

		CustomUserDetails customUserDetails = mglgUserService.loadByUserId(updateUser.getUserId());
		
		Authentication authetication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		securityContext.setAuthentication(authetication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		
		response.sendRedirect("/user/profile");

	}
	
	
	@GetMapping("/changeDefaultImg")
	public void changeDefaultImg(HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		int userId = customUser.getMglgUser().getUserId();
		mglgUserProfileService.changeDefaultImg(userId);
		
		response.sendRedirect("/user/profile");

	}
	
	
	//???????????? ????????? ?????? ???????????? ??????
	@GetMapping("getUserImg")
	public ResponseEntity<?> orderWindow(@AuthenticationPrincipal CustomUserDetails loginUser) {
		int userId = loginUser.getMglgUser().getUserId();
		MglgResponseDTO<MglgUserProfileDTO> response = new MglgResponseDTO<>();
		try {
			MglgUserProfile userProfile = mglgUserProfileService.getUserImg(userId);
			MglgUserProfileDTO userProfileDTO = MglgUserProfileDTO.builder()
																  .userId(userProfile.getMglgUser().getUserId())
																  .userProfileNm(userProfile.getUserProfileNm())
																  .userProfilePath(userProfile.getUserProfilePath())
																  .userProfileCate(userProfile.getUserProfileCate())
																  .userProfileOriginNm(userProfile.getUserProfileOriginNm())
																  .build();
			response.setItem(userProfileDTO);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		

				
			
			
	}
	//???????????? ?????? ????????????
	@GetMapping("eatFriend")
	public ResponseEntity<?> eatFriend(String resName){
		MglgResponseDTO<CamelHashMap> response = new MglgResponseDTO<>();
		
		try {
			List<CamelHashMap> getUser = mglgUserService.getEatUser(resName);
			int i =0;
			for(CamelHashMap a : getUser) {
				int getUserId = (int) getUser.get(i).get("userId");
				MglgUserProfile profileImg = mglgUserProfileService.getUserImg(getUserId);
				getUser.get(i).put("profileImg", profileImg);
				if(i==5) {
					i=0;
				}else {
					i++;
				}
			}
			

			response.setItems(getUser);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	
	

}//????????? ???

