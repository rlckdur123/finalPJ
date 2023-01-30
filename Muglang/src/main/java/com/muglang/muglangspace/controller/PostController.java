package com.muglang.muglangspace.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.common.FileUtils;
import com.muglang.muglangspace.common.Load;
import com.muglang.muglangspace.dto.MglgPostDTO;
import com.muglang.muglangspace.dto.MglgPostFileDTO;
import com.muglang.muglangspace.dto.MglgResponseDTO;
import com.muglang.muglangspace.dto.MglgRestaurantDTO;
import com.muglang.muglangspace.dto.MglgUserProfileDTO;

import com.muglang.muglangspace.dto.MglgUserDTO;

import com.muglang.muglangspace.dto.ResponseDTO;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgPostLikes;
import com.muglang.muglangspace.entity.MglgRestaurant;
import com.muglang.muglangspace.entity.MglgUserProfile;
import com.muglang.muglangspace.service.mglgadmin.AdminService;
import com.muglang.muglangspace.service.mglgcomment.MglgCommentService;
import com.muglang.muglangspace.service.mglghotkeywords.MglgHotKeywordsService;
import com.muglang.muglangspace.service.mglgpost.MglgPostService;
import com.muglang.muglangspace.service.mglgpostfile.MglgPostFileService;
import com.muglang.muglangspace.service.mglgrestaurant.MglgRestaurantService;
import com.muglang.muglangspace.service.mglguser.MglgUserService;
import com.muglang.muglangspace.service.mglguserprofile.MglgUserProfileService;

@RestController
@RequestMapping("/post")
public class PostController {
	@Autowired
	private MglgPostService mglgPostService;

	@Autowired
	private MglgUserService mglgUserService;

	@Autowired
	private MglgCommentService mglgCommentService;

	@Autowired
	private MglgRestaurantService mglgRestaurantService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private MglgPostFileService mglgPostFileService;

	@Autowired
	private MglgUserProfileService mglgUserProfileService;
	
	@Autowired
	private MglgHotKeywordsService mglgHotKeywordsService;

	// 글쓰기 버튼으로 적용되는 글 새로 작성, 새로 작성되는 글에 파일을 같이 넣음.
	@PostMapping("/insertPost")
	public ResponseEntity<?> insertPost(MglgPostDTO mglgPostDTO, MglgRestaurantDTO mglgRestaurantDTO,
			MultipartFile[] uploadFiles, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		System.out.println(mglgPostDTO);
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		int inputFileSize;
		if (uploadFiles == null) {
			inputFileSize = 0;
		} else {
			inputFileSize = uploadFiles.length;
		}
		//해쉬 맵의 사용.	
		CamelHashMap inputInfo = new CamelHashMap();
//		List<MglgPostFileDTO> originFileList = new ObjectMapper().readValue(originFiles,
//													new TypeReference<List<MglgPostFileDTO>>() {});
		// MultipartFile[]의 length는 final이라 처리를 하려면 따로 처리해야함.
		System.out.println("가져온 내용 : " + mglgPostDTO);
		System.out.println("가져온 파일의 정보 : " + uploadFiles);

		System.out.println("식당정보 : " + mglgRestaurantDTO);

		System.out.println("게시글 등록 시작.");
		MglgPost mglgPost = MglgPost.builder().postId(mglgPostDTO.getPostId()).mglgUser(loginUser.getMglgUser())
				.postContent(mglgPostDTO.getPostContent()).restNm(mglgPostDTO.getRestNm()).postDate(LocalDateTime.now())
				.postRating(mglgPostDTO.getPostRating()).restRating(mglgPostDTO.getRestRating())
		        .hashTag1(mglgPostDTO.getHashTag1() == null || mglgPostDTO.getHashTag1().equals("")  ? "": mglgPostDTO.getHashTag1())
				.hashTag2(mglgPostDTO.getHashTag2() == null || mglgPostDTO.getHashTag2().equals("")  ? "": mglgPostDTO.getHashTag2())
				.hashTag3(mglgPostDTO.getHashTag3() == null || mglgPostDTO.getHashTag3().equals("")  ? "": mglgPostDTO.getHashTag3())
				.hashTag4(mglgPostDTO.getHashTag4() == null || mglgPostDTO.getHashTag4().equals("")  ? "": mglgPostDTO.getHashTag4())
				.hashTag5(mglgPostDTO.getHashTag5() == null || mglgPostDTO.getHashTag5().equals("")  ? "": mglgPostDTO.getHashTag5())
				.build();

		mglgPost = mglgPostService.insertPost(mglgPost);
		System.out.println("파일과 식당을 제외한 게시글 내용만 확인 :" + mglgPost);

		try {

			System.out.println("파일의 개수를 확인하고 있습니다. " + inputFileSize);
			System.out.println("파일 정보를 확인하고 있습니다." + uploadFiles);

			// 파일 리스트를 생성하여 해당 게시글의 파일들을 등록하기 위한 사전작업을 함.
			List<MglgPostFile> uploadFileList = new ArrayList<MglgPostFile>();
			// 파일의 개수가 0개일 경우 파일 입출력은 생략하고 다음 작업으로 이동한다.
			if (inputFileSize > 0) {
				String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";

				System.out.println("attachPath====================!====" + attachPath);

				File directory = new File(attachPath);

				if (!directory.exists()) {
					directory.mkdir();
				}
				// 파일의 개수 만큼 하나씩 파일을 DB에 저장함.
				for (int i = 0; i < uploadFiles.length; i++) {
					MultipartFile file = uploadFiles[i];

					if (!file.getOriginalFilename().equals("") && file.getOriginalFilename() != null) {
						MglgPostFile mglgPostFile = FileUtils.parseFileInfo(file, attachPath);
						mglgPostFile.setMglgPost(mglgPost); // 파일의 게시글 id 정보를 담음.
						System.out.println("등록하려는 파일의 원래 이름 : " + mglgPostFile.getPostFileOriginNm());
						uploadFileList.add(mglgPostFile);
						mglgPostFileService.insertPostFile(mglgPostFile); // 파일이 파일을 한개씩 넣고 다 넣으면 끝냄.
					}

				}

			}
			// 입력 작업 수행후 출력할 파일들의 정보를 가져오는 리스트 생성
			List<MglgPostFile> completeFile = mglgPostFileService.getPostFileList(mglgPost.getPostId()); // 기본 배열 미리 생성후
																											// 가져온 배열 값을
																											// 가공함.
			System.out.println("파일 조회 완료#%$^&파일 개수 : " + completeFile.size());

			// DTO로 변경하여 ResponseDTO로 옮김.
			List<MglgPostFileDTO> completeFileDTO = new ArrayList<MglgPostFileDTO>();
			for (int i = 0; i < completeFile.size(); i++) {
				completeFileDTO.add(Load.toHtml(completeFile.get(i)));
				completeFileDTO.get(i).setPostId(mglgPost.getPostId());
			}

			if (completeFileDTO.size() > 0) {
				System.out.println("파일 자료 입력 완료 1번째 아이디 : " + completeFileDTO.get(0).getPostFileId());
			}

			//파일 정보를 담은 리스트를 맵에 담음.
			inputInfo.put("post_file_list", completeFileDTO);

			//화면단으로 넘길 DTO를 생성
			MglgPostDTO returnDTO = Load.toHtml(mglgPost, loginUser.getMglgUser());
			System.out.println("게시글 정보 확인 : " + returnDTO);
			//해당 게시글의 정보를 hashMap에 저장.
			inputInfo.put("insert_post", returnDTO);
			
			//returnDTO의 postId를 이용해서 restaurant 테이블에 내용 insert
			MglgRestaurant mglgRes = MglgRestaurant.builder()
					.mglgPost(mglgPost)
					.resName(mglgRestaurantDTO.getResName())
					.resAddress(mglgRestaurantDTO.getResAddress())
					.resRoadAddress(mglgRestaurantDTO.getResRoadAddress())
					.resPhone(mglgRestaurantDTO.getResPhone())
					.resCategory(mglgRestaurantDTO.getResCategory())
					.build();
			
			//쿼리문 실행 - 식당 정보를 입력하는 과정을 수행.
			mglgRestaurantService.insertRestaurant(mglgRes);

			// 식당정보 표출을 위한 dto를 가공하여 화면단으로 뿌릴 맵을 하나 생성함.
			mglgRes = mglgRestaurantService.selectRes(returnDTO.getPostId());
			// 식당 정보를 DTO로 바꿔서 맵으로 출력함.
			MglgRestaurantDTO mglgResDTO = MglgRestaurantDTO.builder().postId(mglgRes.getMglgPost().getPostId())
					.resName(mglgRes.getResName()).resAddress(mglgRes.getResAddress())
					.resRoadAddress(mglgRes.getResRoadAddress()).resPhone(mglgRes.getResPhone())
					.resCategory(mglgRes.getResCategory()).build();

			System.out.println("식당 정보를 확인차 표시합니다." + mglgResDTO);

			//식당 정보를 맵에 담는작업.
			inputInfo.put("restaurant_info", mglgResDTO);
			
			//게시자의 정보
			inputInfo.put("login_user", Load.toHtml(loginUser.getMglgUser()));
			System.out.println("게시자의 정보를 확인차 표시합니다." + loginUser.getMglgUser());
			
			//작성자의 프로필 사진을 가져오기 위한 로딩 작업.
			MglgUserProfile userProfile = mglgUserProfileService.getUserImg(loginUser.getMglgUser().getUserId());
			inputInfo.put("profile", Load.toHtml(userProfile));

			
			//지도를 표시할지 말지의 여부를 확인하는 정보를 추가한다.
			if(!mglgResDTO.getResName().equals("") || mglgResDTO.getResName() != null) {
				inputInfo.put("restaurant", "Y");
			} else {
				inputInfo.put("restaurant", "N");
			}
			
			//작성글의 좋아요 정보를 가져와 로딩한다. 좋아요는 따로 like를 담는 메소드를 만들지 않고, 해쉬맵에 값을 직접 대입한다.
			//MglgPostLikes likes = mglgPostService.likePost(mglgPost);
			//처음 게시글을 작성할 때는 좋아요 포스트의 값을 N으로 초기화하고 사용할 것이다.
			inputInfo.put("post_like", "N");
			inputInfo.put("like_cnt", 0);
			inputInfo.put("res_cnt", 0);
			
			/*
			MglgPost inputPost = (MglgPost)inputInfo.get("insertPost");
			inputInfo.put("post_date",
					String.valueOf(inputPost.getPostDate()));
			*/
			//게시판에 보여주는 데이터의 값들을 모두 정의하여 기본 값으로 다시 던져 형식을 맞춘다.
			/*
			//가져온 데이터 묶음들을 화면단으로 가져간다.
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("insertPost", returnDTO);
			returnMap.put("loginUser", Load.toHtml(loginUser.getMglgUser()));
			returnMap.put("profile", Load.toHtml(userProfile));
			returnMap.put("postFileList", completeFileDTO);
			returnMap.put("restaurant", mglgResDTO);

			System.out.println("파일 리스트 : " + uploadFileList);
			responseDTO.setItem(returnMap);
			
			System.out.println("새로운 글을 추가합니다.");
			return ResponseEntity.ok().body(responseDTO); 
			*/
			return ResponseEntity.ok().body(inputInfo);
		} catch(Exception e) {
			System.out.println("맵핑에서 문제가 발생하였습니다." + inputInfo);
			return ResponseEntity.badRequest().body(inputInfo);
		}
	}

	@Transactional // 쿼리가 실행된 후 바로 트랜잭션을 호출
	@PutMapping("/updatePost")
	public ResponseEntity<?> updatePost(MglgPostDTO mglgPostDTO, MultipartFile[] uploadFiles,
			MultipartFile[] changedFiles, HttpServletRequest request, @RequestParam("originFiles") String originFiles,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		int inputFileSize;
		// 파일 개수에 대한 예외 처리
		if (uploadFiles == null) {
			inputFileSize = 0;
		} else {
			inputFileSize = uploadFiles.length;
		}

		// 파일의 내용은 하나의 게시글을 가져오므로 1차원 배열을 가져오게됨.
		// JSON string으로 파일의 원래 있던 파일을 가져오는 형식임.
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

		System.out.println("수정 작업 할 맵 DTO 생성 완료" + uploadFiles);
		System.out.println("가져온 게시글의 정보 : " + mglgPostDTO);

		List<MglgPostFileDTO> originFileList = new ObjectMapper().readValue(originFiles,
				new TypeReference<List<MglgPostFileDTO>>() {
				});

		System.out.println("수정 작업을 실행합니다. 원래 파일의 목록을 불러옵니다." + originFileList);

		String attachPath = request.getSession().getServletContext().getRealPath("/") + "/upload/";

		File directory = new File(attachPath);

		if (!directory.exists()) {
			directory.mkdir();
		}

		List<MglgPostFile> uploadFileList = new ArrayList<MglgPostFile>();

		try {
			System.out.println("파일들을 수정할 작업을 시작합니다.");
			// 수정될 게시글의 정보를 모두 담아 갱신하려는 객체 생성.
			MglgPost mglgPost = MglgPost.builder().postId(mglgPostDTO.getPostId()).mglgUser(loginUser.getMglgUser())
					.postContent(mglgPostDTO.getPostContent()).restNm(mglgPostDTO.getRestNm())
					.postDate(LocalDateTime.parse(mglgPostDTO.getPostDate())).postRating(mglgPostDTO.getPostRating())
					.restRating(mglgPostDTO.getRestRating())
					.hashTag1(mglgPostDTO.getHashTag1() == null || mglgPostDTO.getHashTag1().equals("")  ? "": mglgPostDTO.getHashTag1())
					.hashTag2(mglgPostDTO.getHashTag2() == null || mglgPostDTO.getHashTag2().equals("")  ? "": mglgPostDTO.getHashTag2())
					.hashTag3(mglgPostDTO.getHashTag3() == null || mglgPostDTO.getHashTag3().equals("")  ? "": mglgPostDTO.getHashTag3())
					.hashTag4(mglgPostDTO.getHashTag4() == null || mglgPostDTO.getHashTag4().equals("")  ? "": mglgPostDTO.getHashTag4())
					.hashTag5(mglgPostDTO.getHashTag5() == null || mglgPostDTO.getHashTag5().equals("")  ? "": mglgPostDTO.getHashTag5())
					.build();
			System.out.println("파일이 아닌 게시글의 내용을 수정할 정보를 가져옵니다.");
			MglgPost updateMglgPost = mglgPostService.updatePost(mglgPost);

			System.out.println("파일 정보를 확인하고 있습니다." + uploadFiles);
			System.out.println("수정된 파일 정보를 확인 : " + changedFiles);
			System.out.println(updateMglgPost);
			MglgPostDTO updateMglgPostDTO = Load.toHtml(updateMglgPost, loginUser.getMglgUser());

			// 파일 정보를 바꾸거나, 삭제하고 난 정보를 갱신하는 부분.
			// 변경하기전의 파일리스트들을 모두 돌아서 해당 파일의 상태에 따라 삭제와 수정을 처리함.
			for (int i = 0; i < originFileList.size(); i++) {
				// 수정되는 파일 처리
				if (originFileList.get(i).getPostFileStatus().equals("U")) {
					for (int j = 0; j < changedFiles.length; j++) {
						if (originFileList.get(i).getNewFileNm().equals(changedFiles[j].getOriginalFilename())) {
							MglgPostFile targetFile = new MglgPostFile();

							MultipartFile file = changedFiles[j];

							targetFile = FileUtils.parseFileInfo(file, attachPath);

							targetFile.setMglgPost(mglgPost);
							targetFile.setPostFileId(originFileList.get(i).getPostFileId());
							targetFile.setPostFileStatus("U");

							uploadFileList.add(targetFile);
							System.out.println("해당 파일의 상태 : " + targetFile.getPostFileStatus());
							System.out.println("수정된 파일들을 적용 완료 하였습니다." + targetFile);
						}
					}

				}
				// 삭제되는 파일 처리
				else if (originFileList.get(i).getPostFileStatus().equals("D")) {
					MglgPostFile targetFile = new MglgPostFile();

					targetFile.setMglgPost(mglgPost);
					targetFile.setPostFileId(originFileList.get(i).getPostFileId());
					targetFile.setPostFileStatus("D");

					File deleteFile = new File(attachPath + originFileList.get(i).getPostFileNm());
					deleteFile.delete();

					uploadFileList.add(targetFile);
					System.out.println("해당 파일의 상태 : " + targetFile.getPostFileStatus());
					System.out.println("삭제된 파일들을 적용 완료 하였습니다." + targetFile);
				}
			}
			// 수정, 삭제 처리를 완료하고 더 추가된 파일을 적용한 뒤 마지막으로 다시 파일들을 업로드한다.
			// 파일의 개수 만큼 하나씩 파일을 DB에 저장함.
			if (inputFileSize > 0) {
				for (int i = 0; i < inputFileSize; i++) {
					MultipartFile file = uploadFiles[i];

					if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
						MglgPostFile uploadFile = new MglgPostFile();

						uploadFile = FileUtils.parseFileInfo(file, attachPath);

						uploadFile.setMglgPost(mglgPost);
						uploadFile.setPostFileStatus("I");
						uploadFileList.add(uploadFile);
						System.out.println("해당 파일의 상태 : " + uploadFile.getPostFileStatus());
						System.out.println("새로 등록하려는 파일의 원래 이름 : " + uploadFile.getPostFileOriginNm());
						// mglgPostFileService.insertPostFile(uploadFile); //파일이 파일을 한개씩 넣고 다 넣으면 끝냄.
					}
				}
			}
			// 최종적으로 변경을 완료한 파일리스트를 반영하는 서비스 호출
			mglgPostFileService.updatePostFileList(uploadFileList);
			for (int i = 0; i < uploadFileList.size(); i++) {
				System.out.println("파일 DTO 넣기전 : " + uploadFileList.get(i));
			}

			// 파일의 DTO 처리를 위한 갱신한 파일을 다시 조회
			List<MglgPostFile> newUploadFileList = mglgPostFileService.getPostFileList(mglgPostDTO.getPostId());

			// 파일 처리한 데이터를 화면단에 처리하기위한 DTO
			// 새롭게 변경된 파일의 정보를 다시 불러와 DTO로 저장한다.
			// 파일의 상태를 임시로 저장하는 정보도 화면단에 다시 뿌리기 위해 정보를 다시 담음.
			List<MglgPostFileDTO> uploadFileListDTO = new ArrayList<MglgPostFileDTO>();
			for (int i = 0; i < newUploadFileList.size(); i++) {
				MglgPostFileDTO fileDTO = Load.toHtml(newUploadFileList.get(i));
				fileDTO.setPostFileId(newUploadFileList.get(i).getPostFileId());
				fileDTO.setPostId(newUploadFileList.get(i).getMglgPost().getPostId());
				fileDTO.setPostFileStatus("N");
				uploadFileListDTO.add(fileDTO);
			}

			System.out.println("수정된 파일 목록: " + uploadFileListDTO);
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("getPost", updateMglgPostDTO);
			returnMap.put("updateFileList", uploadFileListDTO);
			returnMap.put("fileSize", newUploadFileList.size());
			responseDTO.setItem(returnMap);
			System.out.println("수정 작업 마무리단계");
			return ResponseEntity.ok().body(responseDTO);
		} catch (Exception e) {
			responseDTO.setErrorMessage(e.getMessage());

			return ResponseEntity.badRequest().body(responseDTO);
		}

	}

	// 삭제 작업 수행하기 - 삭제 후 넘어가는 페이지 리다이렉트가 두개가 겹쳐서 안되는거 같다.
	@Transactional // 쿼리가 실행된 후 바로 트랜잭션을 호출
	@PostMapping("/deletePost")
	public void deletePost(MglgPostDTO mglgPostDTO, HttpServletResponse response,
			@AuthenticationPrincipal CustomUserDetails loginUser, @RequestParam("fileSize") int fileSize)
			throws IOException {

		// 외래키로 가지고 있는 파일들을 모두 삭제
		// 식당의 정보도 이다음에 삭제하여 게시글에 해당하는 모든 데이터를 먼저 삭제하는 작업을 수행하면된다.
		if (fileSize > 0) {
			mglgPostFileService.deletePostFileList(mglgPostDTO.getPostId());
			System.out.println("파일들을 삭제 완료 하였습니다..");
		} else {
			System.out.println("게시글에 파일이 없었습니다.");
		}
		/*
		 * //식당의 정보를 조회하여 null이 아니면 삭제 수행. MglgRestaurant tempRes =
		 * mglgRestaurantService.selectRes(mglgPostDTO.getPostId());
		 * System.out.println("삭제할 식당 정보 확인하기 : " + tempRes); if(tempRes != null) {
		 * mglgRestaurantService.deleteRes(mglgPostDTO.getPostId()); } //외래키로 지정은 안되어
		 * 있어서 삭제가 없어도 된다. 추후에 이것을 사용하지 않을 수도 있음.
		 */

		// 게시글 삭제 수행.
		MglgPost mglgPost = MglgPost.builder().postId(mglgPostDTO.getPostId()).mglgUser(loginUser.getMglgUser())
				.build();

		mglgPostService.deletePost(mglgPost);

		response.sendRedirect("/post/mainPost");
	}

	@GetMapping("/mainPost")
	// 로그인후 메인페이지로 이동하여 게시글의 내용을 최종적으로 html화면단에 넘기는 메소드, 게시글의 파일은 따로 테이블에서 가져옴.
	// 메인 페이지도 파일 목록을 불러서 같이 화면에 표시해야하므로, 두개 이상의 객체를 같이 보냄.
	public ModelAndView getPostList(@PageableDefault(page = 0, size = 5) Pageable pageable,
			HttpServletResponse response, HttpSession session, @AuthenticationPrincipal CustomUserDetails loginUser)
			throws IOException {

		int userId = loginUser.getMglgUser().getUserId();

		Page<CamelHashMap> pagePostList = mglgPostService.getPagePostList(pageable, userId);

		System.out.println("찾은 데이터의 형태를 확인합니다." + pagePostList.getContent());

		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			pagePostList.getContent().get(i).put("between_date",
					Duration.between(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
							LocalDateTime.now()).getSeconds());

			pagePostList.getContent().get(i).put("post_date",
					String.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));

			// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
			pagePostList.getContent().get(i).put("res_cnt",
					mglgRestaurantService.countRes(userId, (String) pagePostList.getContent().get(i).get("resName")));
			// 이미 쿼리 카멜 해쉬에서 가져와서 쓸필요 없음.
			// pagePostList.getContent().get(i).put("res_name",
			// mglgRestaurantService.selectRes((Integer)pagePostList.getContent().get(i).get("postId")).getResName());
			pagePostList.getContent().get(i).put("index", i);
		}
		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열처럼 사용됨.
		// 반복문을 통해 한 게시글의 전체 정보를 담을 맵을 반복자로 잡아서 반복문을 돌림.
		for (CamelHashMap file : pagePostList) {
			// 파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
			// 해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//			System.out.println("변경된 맵 : " + file);
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 로드함.
			// 파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
			// fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			// System.out.println("파일의 개수 : " + fileList.size());
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					// 리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
					fileListDTO.add(Load.toHtml(fileList.get(j))); // DTO로 바꾸는 메소드 수행.
					fileListDTO.get(j).setPostId(findId); // 바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
					System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
				}
			}
			file.put("file_length", fileList.size()); // 방금 작업 완료한 게시글의 파일 개수 저장.
			file.put("file_list", fileListDTO); // 키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감.
			// camel형으로 키값을 자동으로 바꿈.
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (Integer) post.get("userId");
			System.out.println("----!!!!!!! 유저  아이디 : " + writerId);
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}
		
		// 인기 검색어 불러오기
		List<CamelHashMap> hotKeywords = mglgHotKeywordsService.getHotKeywords();

		// 화면단에 뿌려줄 정보를 반환하는 객체 생성. 로그인한 유저의 정보와 게시글의 정보를 담고있다.
		ModelAndView mv = new ModelAndView();
		mv.setViewName("post/post.html");
		mv.addObject("postList", pagePostList);
		mv.addObject("hotKeywords", hotKeywords);
		// 세션 대신 유저 인증 유저 토큰의 정보 추출하여 화면단으로 표시
		mv.addObject("loginUser", Load.toHtml(loginUser.getMglgUser()));

		return mv;
	}

	// 메인 포스트 스크롤 로직
	@PostMapping("/mainPost")
	public ResponseEntity<?> getPostListScroll(Pageable pageable, @RequestParam("page_num") int page_num,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {

		int userId = loginUser.getMglgUser().getUserId();
		pageable = PageRequest.of(page_num, 5);

		Page<CamelHashMap> pagePostList = mglgPostService.getPagePostList(pageable, userId);

		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			pagePostList.getContent().get(i).put("between_date",
					Duration.between(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
							LocalDateTime.now()).getSeconds());

			pagePostList.getContent().get(i).put("post_date",
					String.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));
			// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
			pagePostList.getContent().get(i).put("res_cnt",
					mglgRestaurantService.countRes(userId, (String) pagePostList.getContent().get(i).get("resName")));

			pagePostList.getContent().get(i).put("index", i);
		}

		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열
		for (CamelHashMap file : pagePostList) {
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 생성함.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					fileListDTO.add(Load.toHtml(fileList.get(j)));
					fileListDTO.get(j).setPostId(findId);
				}
			}
			file.put("file_length", fileList.size());
			file.put("file_list", fileListDTO);
			System.out.println("확장 페이지의 파일의 개수 : " + file);
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (int) post.get("userId");
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}

		return ResponseEntity.ok().body(pagePostList);
	}

	public List<MglgPost> getYourPost() {
		return null;
	}

	// 포스트 단건 조회
	@GetMapping("/post")
	public ResponseEntity<?> getPost(@RequestParam("postId") int postId) {
		MglgResponseDTO<MglgPostDTO> response = new MglgResponseDTO<>();

		try {
			MglgPost post = MglgPost.builder().postId(postId).build();

			post = mglgPostService.getPost(postId);
			MglgPostDTO returnPostDTO = MglgPostDTO.builder().postId(post.getPostId())
					.postContent(post.getPostContent()).postDate(post.getPostDate().toString())
					// 아래줄 원래 .postId(post.getMglgUser().getUserId()) 2022/12/21 19:09
					.userId(post.getMglgUser().getUserId()).restNm(post.getRestNm()).build();

			response.setItem(returnPostDTO);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/getPost")
	public ResponseEntity<?> getPost(@PageableDefault(page = 0, size = 1) Pageable pageable,@RequestParam("postId") int postId) {
		Page<CamelHashMap> pagePostList = mglgPostService.getPost(pageable, postId);
		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			
			pagePostList.getContent().get(i).put("index", i);
		}

		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열
		for (CamelHashMap file : pagePostList) {
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 생성함.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					fileListDTO.add(Load.toHtml(fileList.get(j)));
					fileListDTO.get(j).setPostId(findId);
				}
			}
			file.put("file_length", fileList.size());
			file.put("file_list", fileListDTO);
			System.out.println("확장 페이지의 파일의 개수 : " + file);
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (int) post.get("userId");
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}

		return ResponseEntity.ok().body(pagePostList);
	}

	// 내 게시물으로 이동
	// 내가 쓴 게시글들만 필터링하여 ajax처리후 게시글을 다 불러오게됨.
	@GetMapping("/myBoard")
	public ResponseEntity<?> myBoard(@PageableDefault(page = 0, size = 5) Pageable pageable,
			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails loginUser) {
		try {
			// 로그인한 유저의 게시글만 표시함.
			int userId = loginUser.getMglgUser().getUserId();

			Page<CamelHashMap> pagePostList = mglgPostService.getPagePersonalPostList(pageable, userId);

			for (int i = 0; i < pagePostList.getContent().size(); i++) {
				pagePostList.getContent().get(i).put("between_date",
						Duration.between(
								((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
								LocalDateTime.now()).getSeconds());

				pagePostList.getContent().get(i).put("post_date", String
						.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));

				// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
				pagePostList.getContent().get(i).put("res_cnt", mglgRestaurantService.countRes(userId,
						(String) pagePostList.getContent().get(i).get("resName")));
				// 이미 쿼리 카멜 해쉬에서 가져와서 쓸필요 없음.
				// pagePostList.getContent().get(i).put("res_name",
				// mglgRestaurantService.selectRes((Integer)pagePostList.getContent().get(i).get("postId")).getResName());
				pagePostList.getContent().get(i).put("index", i);
			}
			// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열처럼 사용됨.
			// 반복문을 통해 한 게시글의 전체 정보를 담을 맵을 반복자로 잡아서 반복문을 돌림.
			for (CamelHashMap file : pagePostList) {
				// 파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
				// 해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//				System.out.println("변경된 맵 : " + file);
				int findId = (int) file.get("postId");
				// 한 게시글의 모든 파일들을 로드함.
				// 파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
				// fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
				List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
				// System.out.println("파일의 개수 : " + fileList.size());
				List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
				if (!fileList.isEmpty()) {
					for (int j = 0; j < fileList.size(); j++) {
						// 리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
						fileListDTO.add(Load.toHtml(fileList.get(j))); // DTO로 바꾸는 메소드 수행.
						fileListDTO.get(j).setPostId(findId); // 바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
						System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
					}
				}
				file.put("file_length", fileList.size()); // 방금 작업 완료한 게시글의 파일 개수 저장.
				file.put("file_list", fileListDTO); // 키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감.
				// camel형으로 키값을 자동으로 바꿈.
			}

			// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
			for (CamelHashMap post : pagePostList) {
				int writerId = (Integer) post.get("userId");
				System.out.println("----!!!!!!! 유저  아이디 : " + writerId);
				MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

				MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
						.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
						.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
						.build();
				post.put("profile", profileDTO);

			}

			// 인기 검색어 받아오기
			// List<MglgShowHotKeywordsDTO> mglgShowHotKeywordsList =
			// mglgPostService.getShowHotKeywords();

			return ResponseEntity.ok().body(pagePostList);
		} catch (Exception e) {
			return ResponseEntity.ok().body(response);
		}
	}

	// 내 게시물으로 이동
	// 내가 쓴 게시글들만 필터링하여 ajax처리후 확장된 게시글을 더 불러오게됨.
	@PostMapping("/myBoard")
	public ResponseEntity<?> myExtendBoard(Pageable pageable, @RequestParam("page_num") int page_num,
			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		System.out.println("!@#$(!@#$@!#$ 페이지 번호 : " + page_num);
		pageable = PageRequest.of(page_num, 5);
		try {
			// 로그인한 유저의 게시글만 표시함.
			int userId = loginUser.getMglgUser().getUserId();

			Page<CamelHashMap> pagePostList = mglgPostService.getPagePersonalPostList(pageable, userId);

			for (int i = 0; i < pagePostList.getContent().size(); i++) {
				pagePostList.getContent().get(i).put("between_date",
						Duration.between(
								((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
								LocalDateTime.now()).getSeconds());

				pagePostList.getContent().get(i).put("post_date", String
						.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));

				// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
				pagePostList.getContent().get(i).put("res_cnt", mglgRestaurantService.countRes(userId,
						(String) pagePostList.getContent().get(i).get("resName")));
				// 이미 쿼리 카멜 해쉬에서 가져와서 쓸필요 없음.
				// pagePostList.getContent().get(i).put("res_name",
				// mglgRestaurantService.selectRes((Integer)pagePostList.getContent().get(i).get("postId")).getResName());
				pagePostList.getContent().get(i).put("index", i);
			}
			// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열처럼 사용됨.
			// 반복문을 통해 한 게시글의 전체 정보를 담을 맵을 반복자로 잡아서 반복문을 돌림.
			for (CamelHashMap file : pagePostList) {
				// 파일 정보를 화면단에 출력하기위해서 DTO를 담을 때 엔티티에서는 있지만, DTO에서는 이것을 따로 담아서 사용한다.
				// 해당 포스팅 게시글에 해당하는 파일들을 모두 검색하기 위해 포스트 ID를 따로 가져와서 쿼리로 검색한다.
//				System.out.println("변경된 맵 : " + file);
				int findId = (int) file.get("postId");
				// 한 게시글의 모든 파일들을 로드함.
				// 파일을 등록하지 않은 경우 파일 없이 수행. if문의 조건에 만족하지 못하면 file 데이터는 없는것.
				// fileList 객체의 결과가 없으면 if문을 들어가지 않고 바로 빠져나와 파일의 개수가 0이고, 내용은 비어있음.
				List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
				// System.out.println("파일의 개수 : " + fileList.size());
				List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
				if (!fileList.isEmpty()) {
					for (int j = 0; j < fileList.size(); j++) {
						// 리스트에 먼저 추가를하고 키 값을 그후에 넣어야함. 없는 객체에 뭘 넣는건 불가능함.
						fileListDTO.add(Load.toHtml(fileList.get(j))); // DTO로 바꾸는 메소드 수행.
						fileListDTO.get(j).setPostId(findId); // 바로 직전 메소드에서 postID는 넣지 않음. 따로 넣는 과정.
						System.out.println(findId + "의 파일 목록 : " + fileListDTO.get(j));
					}
				}
				file.put("file_length", fileList.size()); // 방금 작업 완료한 게시글의 파일 개수 저장.
				file.put("file_list", fileListDTO); // 키값은 스네이크형으로 적고 오버라이딩된 camelHashMap클래스의 메소드를 따라감.
				// camel형으로 키값을 자동으로 바꿈.
			}

			// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
			for (CamelHashMap post : pagePostList) {
				int writerId = (Integer) post.get("userId");
				System.out.println("----!!!!!!! 유저  아이디 : " + writerId);
				MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

				MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
						.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
						.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
						.build();
				post.put("profile", profileDTO);

			}

			// 인기 검색어 받아오기
			// List<MglgShowHotKeywordsDTO> mglgShowHotKeywordsList =
			// mglgPostService.getShowHotKeywords();

			return ResponseEntity.ok().body(pagePostList);
		} catch (Exception e) {
			return ResponseEntity.ok().body(response);
		}
	}

	// 팔로우 하고 있는사람 포스트만 불러오는 로직
	@GetMapping("/getFollowerPost")
	public ResponseEntity<?> getFollowerPost(@PageableDefault(page = 0, size = 5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails loginUser) {
		MglgResponseDTO<CamelHashMap> response = new MglgResponseDTO<>();
		
		int userId = loginUser.getMglgUser().getUserId();

		Page<CamelHashMap> pagePostList = mglgPostService.getFollowerPost(userId, pageable);

		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			pagePostList.getContent().get(i).put("between_date",
					Duration.between(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
							LocalDateTime.now()).getSeconds());

			pagePostList.getContent().get(i).put("post_date",
					String.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));
			// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
			pagePostList.getContent().get(i).put("res_cnt",
					mglgRestaurantService.countRes(userId, (String) pagePostList.getContent().get(i).get("resName")));

			pagePostList.getContent().get(i).put("index", i);
		}

		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열
		for (CamelHashMap file : pagePostList) {
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 생성함.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					fileListDTO.add(Load.toHtml(fileList.get(j)));
					fileListDTO.get(j).setPostId(findId);
				}
			}
			file.put("file_length", fileList.size());
			file.put("file_list", fileListDTO);
			System.out.println("확장 페이지의 파일의 개수 : " + file);
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (int) post.get("userId");
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}

		return ResponseEntity.ok().body(pagePostList);
	}

	// 팔로우 하고 있는사람 포스트만 불러오는 로직 - 팔로잉
	@GetMapping("/getFollowingPost")
	public ResponseEntity<?> getFollowingPost(@PageableDefault(page = 0, size = 5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails loginUser) {
		MglgResponseDTO<CamelHashMap> response = new MglgResponseDTO<>();
		int userId = loginUser.getMglgUser().getUserId();
		
		// 팔로잉 하고 있는 상태의 유저들의 게시글만 표시함.
		Page<CamelHashMap> pagePostList = mglgPostService.getFollowingPost(userId, pageable);
		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			pagePostList.getContent().get(i).put("between_date",
					Duration.between(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
							LocalDateTime.now()).getSeconds());

			pagePostList.getContent().get(i).put("post_date",
					String.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));
			// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
			pagePostList.getContent().get(i).put("res_cnt",
					mglgRestaurantService.countRes(userId, (String) pagePostList.getContent().get(i).get("resName")));

			pagePostList.getContent().get(i).put("index", i);
		}

		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열
		for (CamelHashMap file : pagePostList) {
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 생성함.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					fileListDTO.add(Load.toHtml(fileList.get(j)));
					fileListDTO.get(j).setPostId(findId);
				}
			}
			file.put("file_length", fileList.size());
			file.put("file_list", fileListDTO);
			System.out.println("확장 페이지의 파일의 개수 : " + file);
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (int) post.get("userId");
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}

		return ResponseEntity.ok().body(pagePostList);
	}

	// 한 사람의 포스트만 불러오는 로직
	@GetMapping("/otherUserPost")
	public ResponseEntity<?> otherUserPost(@PageableDefault(page = 0, size = 5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails loginUser, @RequestParam("otherUserId") int otherUserId) {
		MglgResponseDTO<CamelHashMap> response = new MglgResponseDTO<>();
		int userId = loginUser.getMglgUser().getUserId();

		Page<CamelHashMap> pagePostList = mglgPostService.otherUserPost(userId, otherUserId, pageable);

		for (int i = 0; i < pagePostList.getContent().size(); i++) {
			pagePostList.getContent().get(i).put("between_date",
					Duration.between(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime(),
							LocalDateTime.now()).getSeconds());

			pagePostList.getContent().get(i).put("post_date",
					String.valueOf(((Timestamp) pagePostList.getContent().get(i).get("postDate")).toLocalDateTime()));
			// 해당 글에 등록된 식당이 팔로우 하고있는 유저의 포스트에 몇개 등록되어 있는지
			pagePostList.getContent().get(i).put("res_cnt",
					mglgRestaurantService.countRes(userId, (String) pagePostList.getContent().get(i).get("resName")));

			pagePostList.getContent().get(i).put("index", i);
		}

		// 파일의 내용을 맵으로 입력하고, 해당 파일의 정보를 불러오게됨. 2차원 배열
		for (CamelHashMap file : pagePostList) {
			int findId = (int) file.get("postId");
			// 한 게시글의 모든 파일들을 생성함.
			List<MglgPostFile> fileList = mglgPostFileService.getPostFileList(findId);
			List<MglgPostFileDTO> fileListDTO = new ArrayList<MglgPostFileDTO>();
			if (!fileList.isEmpty()) {
				for (int j = 0; j < fileList.size(); j++) {
					fileListDTO.add(Load.toHtml(fileList.get(j)));
					fileListDTO.get(j).setPostId(findId);
				}
			}
			file.put("file_length", fileList.size());
			file.put("file_list", fileListDTO);
			System.out.println("확장 페이지의 파일의 개수 : " + file);
		}

		// 프로필의 정보를 가져오는 부분(가져온 정보는 표시만을 하기위해 사용)
		for (CamelHashMap post : pagePostList) {
			int writerId = (int) post.get("userId");
			MglgUserProfile profile = mglgUserProfileService.getUserImg(writerId);

			MglgUserProfileDTO profileDTO = MglgUserProfileDTO.builder().userId(writerId)
					.userProfileNm(profile.getUserProfileNm()).userProfileOriginNm(profile.getUserProfileOriginNm())
					.userProfilePath(profile.getUserProfilePath()).userProfileCate(profile.getUserProfileCate())
					.build();
			post.put("profile", profileDTO);

		}

		return ResponseEntity.ok().body(pagePostList);
	}

	// 좋아요 눌렀을때 ajax
	@GetMapping("likeUp")
	public ResponseEntity<?> likeUp(@RequestParam("userId") int userId, @RequestParam("postId") int postId) {
		try {
			int likeCnt = mglgPostService.likeUp(userId, postId);
			return ResponseEntity.ok().body(likeCnt);

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok().body(0);
		}

	}

	@GetMapping("likeDown")
	public ResponseEntity<?> likeDown(@RequestParam("userId") int userId, @RequestParam("postId") int postId)
			throws IOException {
		try {
			int likeCnt = mglgPostService.likeDown(userId, postId);
			return ResponseEntity.ok().body(likeCnt);

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok().body(0);
		}
	}

	@PostMapping("reportPost")
	public void reportPost(String msg, String url, HttpServletResponse response, @RequestParam("postId") int postId,
			@AuthenticationPrincipal CustomUserDetails loginUser) throws IOException {
		int userId = loginUser.getMglgUser().getUserId();
		msg = mglgPostService.reportPost(postId, userId);
		url = "/post/mainPost";

		if (msg.equals("self")) {
			msg = "자신의 글은 신고할 수 없습니다.";
		} else if (msg.equals("success")) {
			msg = postId + "번 포스트를 신고했습니다.";
		} else {
			msg = "중복해서 신고할 수 없습니다.";
		}
		try {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter w = response.getWriter();
			w.write("<script>alert('" + msg + "');location.href='" + url + "';</script>");
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("map")
	public ModelAndView map() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("post/kakaoMap.html");

		return mv;
	}

	@GetMapping("PostMap")
	public ResponseEntity<?> PostMap(@RequestParam("postId") int postId) {
		try {
			MglgRestaurant mglgRes = mglgRestaurantService.selectRes(postId);

			return ResponseEntity.ok().body(mglgRes);

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.ok().body(e);
		}
	}
}
