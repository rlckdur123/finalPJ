package com.muglang.muglangspace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.muglang.muglangspace.dto.MglgReportDTO;
import com.muglang.muglangspace.dto.MglgResponseDTO;
import com.muglang.muglangspace.dto.MglgUserDTO;

import com.muglang.muglangspace.common.CamelHashMap;

import com.muglang.muglangspace.entity.MglgReport;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.service.mglgadmin.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

//////////////////----------커멘트/유저/포스트 신고----------------------/////////////
	//리포트 - 커멘트 이동
	
	@GetMapping("/commentReport")
	public ModelAndView reportComment(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		int a = 1;
		Page<MglgReport> pageReportList = adminService.getReportComment(a,pageable);
		Page<MglgReportDTO> pageReportDTOList = pageReportList.map(pageReport -> 
												MglgReportDTO.builder()
												.reportId(pageReport.getReportId())
												.reportType(pageReport.getReportType())
												.sourceUserId(pageReport.getSourceUserId())
												.targetUserId(pageReport.getTargetUserId())
												.reportDate(pageReport.getReportDate() == null ?
															   	null :
															   		pageReport.getReportDate().toString())
												.postId(pageReport.getPostId())
												.commentId(pageReport.getCommentId())
												.build()
		);
		
				ModelAndView mv = new ModelAndView();
				mv.addObject("reportList",pageReportDTOList);
				mv.setViewName("/admin/commentReport.html");
				return mv;
	}

	//리포트 - 포스트 이동 및 포스트 조회 a = 번호
	@GetMapping("/postReport")
	public ModelAndView reportPost(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		int a = 2;
		//동일한 로직의 사용을 위해 getreportcomment 재사용
		Page<MglgReport> pageReportList = adminService.getReportComment(a,pageable);
		Page<MglgReportDTO> pageReportDTOList = pageReportList.map(pageReport -> 
												MglgReportDTO.builder()
												.reportId(pageReport.getReportId())
												.reportType(pageReport.getReportType())
												.sourceUserId(pageReport.getSourceUserId())
												.targetUserId(pageReport.getTargetUserId())
												.reportDate(pageReport.getReportDate() == null ?
															   	null :
															   		pageReport.getReportDate().toString())
												.postId(pageReport.getPostId())
												.build()
		);

				
				ModelAndView mv = new ModelAndView();
				mv.addObject("reportList",pageReportDTOList);
				mv.setViewName("/admin/postReport.html");
				return mv;
	}
	//리포트 - 유저 이동 및 유저 신고 조회 a = 번호
		@GetMapping("/userReport")
		public ModelAndView reportUser(@PageableDefault(page = 0, size = 10) Pageable pageable) {
			int a = 3;
			//동일한 로직의 사용을 위해 getreportcomment 재사용
			Page<MglgReport> pageReportList = adminService.getReportComment(a,pageable);
			Page<MglgReportDTO> pageReportDTOList = pageReportList.map(pageReport -> 
													MglgReportDTO.builder()
													.reportId(pageReport.getReportId())
													.reportType(pageReport.getReportType())
													.sourceUserId(pageReport.getSourceUserId())
													.targetUserId(pageReport.getTargetUserId())
													.reportDate(pageReport.getReportDate() == null ?
																   	null :
																   		pageReport.getReportDate().toString())
													.postId(pageReport.getPostId())
													.build()
			);

					
					ModelAndView mv = new ModelAndView();
					mv.addObject("reportList",pageReportDTOList);
					mv.setViewName("/admin/userReport.html");
					return mv;
		}

//////////////////----------커멘트/유저/포스트 신고끝----------------------/////////////
/// 오더 윈도우 -------------------------
		//유저 오더 윈도우 
		@GetMapping("/orderWindow")
		public ResponseEntity<?> orderWindow(@PageableDefault(page = 0, size = 10)Pageable pageable) {
			//동일한 로직의 사용을 위해 getreportcomment 재사용
			MglgResponseDTO<MglgReportDTO> response = new MglgResponseDTO<>();
			try {

				Page<CamelHashMap> reportedUserList = adminService.reportedUser(pageable);
				Page<MglgReportDTO> pageReportDTOList = reportedUserList.map(reportUser -> 
														MglgReportDTO.builder()
														.count(Integer.valueOf(String.valueOf(reportUser.get("count"))))
														.targetUserId(Integer.valueOf(String.valueOf(reportUser.get("targetUserId"))))
														.build()											
				);
				response.setPageItems(pageReportDTOList);
				return ResponseEntity.ok().body(response);
			} catch (Exception e) {
				response.setErrorMessage(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
			

					
				
				
		}

/// 오더 윈도우 끝 ------------------------
//---------------------------------윈도우 오픈---------------------------------

		//커멘트윈도우 오픈
		@GetMapping("/commentWindow")
		public ModelAndView commentWindow(@RequestParam("commentId") int commentId) {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/admin/commentWindow.html");
			return mv;
		}
		//포스트윈도우 오픈
		@GetMapping("/postWindow")
		public ModelAndView postWindow(@RequestParam("postId") int postId) {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/admin/postWindow.html");
			return mv;
		}
		
		//유저 오더 윈도우 오픈
		@GetMapping("/userOrderWindow")
		public ModelAndView userOrderWindow() {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/admin/userOrderWindow.html");
			return mv;
		}
		//faq 윈도우 오픈
		@GetMapping("/adminFAQWindow")
		public ModelAndView adminFAQWindow(@RequestParam("boardId") int boardId) {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/admin/adminFAQWindow.html");
			return mv;
		}
		//faq 인서트 윈도우 오픈
		@GetMapping("/adminFAQInsert")
		public ModelAndView adminFAQInsert() {
			System.out.println("어드민faq윈도우");
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/admin/adminFAQInsert.html");
			return mv;
		}
		

		////-----------------------------------------////
	//밴 유저 yn 변경
		@PutMapping("banUser")
		public ResponseEntity<?> banUser(MglgUserDTO userDTO) {
			MglgResponseDTO<MglgUserDTO> response = new MglgResponseDTO<>();
			
			try {
				MglgUser user = MglgUser.builder()
						.userId(userDTO.getUserId())
						.userBanYn(userDTO.getUserBanYn())
						.build();
				user = adminService.uptUserBan(user);
				MglgUserDTO returnUserDTO = MglgUserDTO.builder()
													   .userId(user.getUserId())
													   .userBanYn(user.getUserBanYn())
													   .build();
					
				response.setItem(returnUserDTO);
				return ResponseEntity.ok().body(response);
			} catch (Exception e) {
				response.setErrorMessage(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
			
			
			

			
			
			
		}



}

