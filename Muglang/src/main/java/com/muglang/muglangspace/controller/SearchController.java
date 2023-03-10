package com.muglang.muglangspace.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.common.Load;
import com.muglang.muglangspace.dto.MglgPostFileDTO;
import com.muglang.muglangspace.dto.MglgUserProfileDTO;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgUserProfile;
import com.muglang.muglangspace.service.mglghotkeywords.MglgHotKeywordsService;
import com.muglang.muglangspace.service.mglgpost.MglgPostService;
import com.muglang.muglangspace.service.mglgpostfile.MglgPostFileService;
import com.muglang.muglangspace.service.mglgrestaurant.MglgRestaurantService;
import com.muglang.muglangspace.service.mglguserprofile.MglgUserProfileService;

@RestController
@RequestMapping("/search")
public class SearchController {
	
	@Autowired MglgPostService mglgPostService; 
	@Autowired MglgRestaurantService mglgRestaurantService;
	@Autowired MglgPostFileService mglgPostFileService;
	@Autowired MglgHotKeywordsService mglgHotKeywordsService;
	@Autowired MglgUserProfileService mglgUserProfileService;
	
	// ????????? ??? ??????????????? ???????????? ??????
	@GetMapping("/search")
	public ModelAndView searchByPost(@RequestParam("searchKeyword") String searchKeyword,
									 @PageableDefault(page = 0, size = 5) Pageable pageable,
									 @AuthenticationPrincipal CustomUserDetails loginUser,
									 HttpServletResponse response, HttpSession session) throws IOException {
		
		// ???????????? ????????? ?????????
		int userId = loginUser.getMglgUser().getUserId();
		

		// ????????? ?????? ??? DB??? ???????????? ?????? ???????????? INSERT ?????? ???????????? UPDATE ????????? ??????
		mglgHotKeywordsService.insrtOrUpdte(searchKeyword);
		
		// ?????? ????????? ????????????
		List<CamelHashMap> hotKeywords = mglgHotKeywordsService.getHotKeywords();
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/search/search.html");
		mv.addObject("loginUser", Load.toHtml(loginUser.getMglgUser()));
		mv.addObject("searchKeyword", searchKeyword);
		mv.addObject("hotKeywords", hotKeywords);
		
		return mv;
		
	}
	
	@PostMapping("/searchPost")
	public ResponseEntity<?> searchPost(@RequestParam("searchKeyword") String searchKeyword,
			Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		// **????????? ????????? ???????????? ???????????? ?????????
				Page<CamelHashMap> postsContentList = mglgPostService.searchByPost(searchKeyword, pageable, userId);
				
				
				for(int i=0; i<postsContentList.getContent().size(); i++) {
					postsContentList.getContent().get(i).put(
							"between_date", Duration.between(
									((Timestamp) postsContentList.getContent().get(i).get("postDate")).toLocalDateTime(),
									LocalDateTime.now()).getSeconds()
					);
					
					postsContentList.getContent().get(i).put(
							"post_date",
							String.valueOf(
									((Timestamp) postsContentList.getContent().get(i).get("postDate")).toLocalDateTime()
							)
					);
					
					postsContentList.getContent().get(i).put(
							"res_cnt",
							mglgRestaurantService.countRes(userId, (String)postsContentList.getContent().get(i).get("resName"))
					);
					
					postsContentList.getContent().get(i).put("index", i);
				}
				
				for(CamelHashMap file : postsContentList) {
					//?????? ????????? ???????????? ????????????????????? DTO??? ?????? ??? ?????????????????? ?????????, DTO????????? ????????? ?????? ????????? ????????????.
					//?????? ????????? ???????????? ???????????? ???????????? ?????? ???????????? ?????? ????????? ID??? ?????? ???????????? ????????? ????????????.
//					System.out.println("????????? ??? : " + file);
					int findId = (int)file.get("postId");
					//??? ???????????? ?????? ???????????? ?????????.
					//????????? ???????????? ?????? ?????? ?????? ?????? ??????. if?????? ????????? ???????????? ????????? file ???????????? ?????????.
					//fileList ????????? ????????? ????????? if?????? ???????????? ?????? ?????? ???????????? ????????? ????????? 0??????, ????????? ????????????.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("????????? ?????? : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//???????????? ?????? ??????????????? ??? ?????? ????????? ????????????. ?????? ????????? ??? ????????? ????????????.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO??? ????????? ????????? ??????.
							fileListDTO.get(j).setPostId(findId);	//?????? ?????? ??????????????? postID??? ?????? ??????. ?????? ?????? ??????.
							System.out.println(findId + "??? ?????? ?????? : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//?????? ?????? ????????? ???????????? ?????? ?????? ??????.
					file.put("file_list", fileListDTO);	//????????? ????????????????????? ?????? ?????????????????? camelHashMap???????????? ???????????? ?????????. 
					//camel????????? ????????? ???????????? ??????.
				} 
				
				for (CamelHashMap post : postsContentList) {
					int writerId = (int) post.get("userId");
					MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

					MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
							.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
							.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
							.build();
					post.put("profile", profileDTO);
				}
				
				
				// ????????? ????????? ???????????? ???????????? ?????????**	

		return ResponseEntity.ok().body(postsContentList);
	}
	
	@PostMapping("/searchTag")
	public ResponseEntity<?> searchTag(@RequestParam("searchKeyword") String searchKeyword,
			Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		// ** ?????? ????????? ???????????? ???????????? ?????????
				Page<CamelHashMap> postsHashtagList = mglgPostService.searchByHashtag(searchKeyword, pageable, userId);
				
				for(int i=0; i<postsHashtagList.getContent().size(); i++) {
					postsHashtagList.getContent().get(i).put(
							"between_date", Duration.between(
									((Timestamp) postsHashtagList.getContent().get(i).get("postDate")).toLocalDateTime(),
									LocalDateTime.now()).getSeconds()
					);
					
					postsHashtagList.getContent().get(i).put(
							"post_date",
							String.valueOf(
									((Timestamp) postsHashtagList.getContent().get(i).get("postDate")).toLocalDateTime()
							)
					);
					
					postsHashtagList.getContent().get(i).put(
							"res_cnt",
							mglgRestaurantService.countRes(userId, (String)postsHashtagList.getContent().get(i).get("resName"))
					);
					
					postsHashtagList.getContent().get(i).put("index", i);
				}
				
				for(CamelHashMap file : postsHashtagList) {
					//?????? ????????? ???????????? ????????????????????? DTO??? ?????? ??? ?????????????????? ?????????, DTO????????? ????????? ?????? ????????? ????????????.
					//?????? ????????? ???????????? ???????????? ???????????? ?????? ???????????? ?????? ????????? ID??? ?????? ???????????? ????????? ????????????.
//					System.out.println("????????? ??? : " + file);
					int findId = (int)file.get("postId");
					//??? ???????????? ?????? ???????????? ?????????.
					//????????? ???????????? ?????? ?????? ?????? ?????? ??????. if?????? ????????? ???????????? ????????? file ???????????? ?????????.
					//fileList ????????? ????????? ????????? if?????? ???????????? ?????? ?????? ???????????? ????????? ????????? 0??????, ????????? ????????????.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("????????? ?????? : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//???????????? ?????? ??????????????? ??? ?????? ????????? ????????????. ?????? ????????? ??? ????????? ????????????.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO??? ????????? ????????? ??????.
							fileListDTO.get(j).setPostId(findId);	//?????? ?????? ??????????????? postID??? ?????? ??????. ?????? ?????? ??????.
							System.out.println(findId + "??? ?????? ?????? : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//?????? ?????? ????????? ???????????? ?????? ?????? ??????.
					file.put("file_list", fileListDTO);	//????????? ????????????????????? ?????? ?????????????????? camelHashMap???????????? ???????????? ?????????. 
					//camel????????? ????????? ???????????? ??????.
				} 
				for (CamelHashMap post : postsHashtagList) {
					int writerId = (int) post.get("userId");
					MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

					MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
							.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
							.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
							.build();
					post.put("profile", profileDTO);
				}//?????? ????????? ???????????? ???????????? ?????????**

		return ResponseEntity.ok().body(postsHashtagList);
	}
	
	@PostMapping("/searchNick")
	public ResponseEntity<?> searchNick(@RequestParam("searchKeyword") String searchKeyword,
			Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		// **????????? ???????????? ???????????? ???????????? ?????????
				Page<CamelHashMap> postsNickList = mglgPostService.searchByNick(searchKeyword, pageable, userId);
				
				for(int i=0; i<postsNickList.getContent().size(); i++) {
					postsNickList.getContent().get(i).put(
							"between_date", Duration.between(
									((Timestamp) postsNickList.getContent().get(i).get("postDate")).toLocalDateTime(),
									LocalDateTime.now()).getSeconds()
					);
					
					postsNickList.getContent().get(i).put(
							"post_date",
							String.valueOf(
									((Timestamp) postsNickList.getContent().get(i).get("postDate")).toLocalDateTime()
							)
					);
					
					postsNickList.getContent().get(i).put(
							"res_cnt",
							mglgRestaurantService.countRes(userId, (String)postsNickList.getContent().get(i).get("resName"))
					);
					
					postsNickList.getContent().get(i).put("index", i);
				}
				
				for(CamelHashMap file : postsNickList) {
					//?????? ????????? ???????????? ????????????????????? DTO??? ?????? ??? ?????????????????? ?????????, DTO????????? ????????? ?????? ????????? ????????????.
					//?????? ????????? ???????????? ???????????? ???????????? ?????? ???????????? ?????? ????????? ID??? ?????? ???????????? ????????? ????????????.
//					System.out.println("????????? ??? : " + file);
					int findId = (int)file.get("postId");
					//??? ???????????? ?????? ???????????? ?????????.
					//????????? ???????????? ?????? ?????? ?????? ?????? ??????. if?????? ????????? ???????????? ????????? file ???????????? ?????????.
					//fileList ????????? ????????? ????????? if?????? ???????????? ?????? ?????? ???????????? ????????? ????????? 0??????, ????????? ????????????.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("????????? ?????? : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//???????????? ?????? ??????????????? ??? ?????? ????????? ????????????. ?????? ????????? ??? ????????? ????????????.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO??? ????????? ????????? ??????.
							fileListDTO.get(j).setPostId(findId);	//?????? ?????? ??????????????? postID??? ?????? ??????. ?????? ?????? ??????.
							System.out.println(findId + "??? ?????? ?????? : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//?????? ?????? ????????? ???????????? ?????? ?????? ??????.
					file.put("file_list", fileListDTO);	//????????? ????????????????????? ?????? ?????????????????? camelHashMap???????????? ???????????? ?????????. 
					//camel????????? ????????? ???????????? ??????.
				} 
				for (CamelHashMap post : postsNickList) {
					int writerId = (int) post.get("userId");
					MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

					MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
							.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
							.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
							.build();
					post.put("profile", profileDTO);
				}
				// ????????? ???????????? ???????????? ???????????? ?????????**

		return ResponseEntity.ok().body(postsNickList);
	}
	
}
