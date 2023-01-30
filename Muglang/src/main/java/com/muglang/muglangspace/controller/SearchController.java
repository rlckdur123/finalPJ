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
	
	// 포스트 닉 해쉬태그를 기준으로 검색
	@GetMapping("/search")
	public ModelAndView searchByPost(@RequestParam("searchKeyword") String searchKeyword,
									 @PageableDefault(page = 0, size = 5) Pageable pageable,
									 @AuthenticationPrincipal CustomUserDetails loginUser,
									 HttpServletResponse response, HttpSession session) throws IOException {
		
		// 로그인한 유저의 아이디
		int userId = loginUser.getMglgUser().getUserId();
		

		// 키워드 검색 시 DB에 키워드가 없을 경우에는 INSERT 있을 경우에는 UPDATE 쿼리를 보냄
		mglgHotKeywordsService.insrtOrUpdte(searchKeyword);
		
		// 인기 검색어 불러오기
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

		// **포스트 내용을 기준으로 받아오는 정보들
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
					//파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
					//해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//					System.out.println("변경된 맵 : " + file);
					int findId = (int)file.get("postId");
					//한 게시글의 모든 파일들을 로드함.
					//파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
					//fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("파일의 개수 : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO로 바꾸는 메소드 수행.
							fileListDTO.get(j).setPostId(findId);	//바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
							System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//방금 작업 완료한 게시글의 파일 개수 저장.
					file.put("file_list", fileListDTO);	//키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감. 
					//camel형으로 키값을 자동으로 바꿈.
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
				
				
				// 포스트 내용을 기준으로 받아오는 정보들**	

		return ResponseEntity.ok().body(postsContentList);
	}
	
	@PostMapping("/searchTag")
	public ResponseEntity<?> searchTag(@RequestParam("searchKeyword") String searchKeyword,
			Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		// ** 해쉬 태그를 기준으로 받아오는 정보들
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
					//파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
					//해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//					System.out.println("변경된 맵 : " + file);
					int findId = (int)file.get("postId");
					//한 게시글의 모든 파일들을 로드함.
					//파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
					//fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("파일의 개수 : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO로 바꾸는 메소드 수행.
							fileListDTO.get(j).setPostId(findId);	//바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
							System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//방금 작업 완료한 게시글의 파일 개수 저장.
					file.put("file_list", fileListDTO);	//키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감. 
					//camel형으로 키값을 자동으로 바꿈.
				} 
				for (CamelHashMap post : postsHashtagList) {
					int writerId = (int) post.get("userId");
					MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

					MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
							.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
							.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
							.build();
					post.put("profile", profileDTO);
				}//해쉬 태그를 기준으로 받아오는 정보들**

		return ResponseEntity.ok().body(postsHashtagList);
	}
	
	@PostMapping("/searchNick")
	public ResponseEntity<?> searchNick(@RequestParam("searchKeyword") String searchKeyword,
			Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		// **포스트 닉네임을 기준으로 받아오는 정보들
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
					//파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
					//해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//					System.out.println("변경된 맵 : " + file);
					int findId = (int)file.get("postId");
					//한 게시글의 모든 파일들을 로드함.
					//파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
					//fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
					List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
					//System.out.println("파일의 개수 : " + fileList.size());
					List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
					if(!fileList.isEmpty()) {
						for(int j = 0; j < fileList.size(); j++) {
							//리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
							fileListDTO.add(Load.toHtml(fileList.get(j)));	//DTO로 바꾸는 메소드 수행.
							fileListDTO.get(j).setPostId(findId);	//바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
							System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
						}
					}
					file.put("file_length", fileList.size());	//방금 작업 완료한 게시글의 파일 개수 저장.
					file.put("file_list", fileListDTO);	//키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감. 
					//camel형으로 키값을 자동으로 바꿈.
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
				// 포스트 닉네임을 기준으로 받아오는 정보들**

		return ResponseEntity.ok().body(postsNickList);
	}
	
}
