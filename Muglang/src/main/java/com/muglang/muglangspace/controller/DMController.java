package com.muglang.muglangspace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.dto.ResponseDTO;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgChatroom;
import com.muglang.muglangspace.service.mglgdm.DMService;

@RestController
@RequestMapping("/dm")
public class DMController {
	
	@Autowired
	private DMService dmService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@GetMapping("/show")
	public ModelAndView dmInterface(@AuthenticationPrincipal CustomUserDetails loginUser) {
		
		int loginUserId = loginUser.getMglgUser().getUserId();
		
		ModelAndView mv = new ModelAndView();
		
		List<CamelHashMap> followBackList = dmService.getFollowBackList(loginUserId);
				
		mv.addObject("followBackList", followBackList);
		mv.setViewName("chat/dmint.html");
		return mv;
	}
	
	@GetMapping("/checkRoom")
	public ResponseEntity<?> checkRoom(@RequestParam("userId") int userId, @AuthenticationPrincipal CustomUserDetails loginUser) {
		
		ResponseDTO<MglgChatroom> responseDTO = new ResponseDTO<>();
		
		try {
			MglgChatroom mglgChatroom = dmService.checkRoom(userId, loginUser.getMglgUser().getUserId());
			
			if(mglgChatroom != null) {
				responseDTO.setItem(mglgChatroom);	
			}
			
			return ResponseEntity.ok().body(responseDTO);
			
		} catch(Exception e) {
			System.out.println("error=====================" + e.getMessage());
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
			
		}
	}
	
	
}
