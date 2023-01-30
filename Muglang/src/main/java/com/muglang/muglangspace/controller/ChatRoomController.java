package com.muglang.muglangspace.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muglang.muglangspace.dto.ChatRoom;
import com.muglang.muglangspace.dto.MglgChatMessageDTO;
import com.muglang.muglangspace.entity.CustomUserDetails;
import com.muglang.muglangspace.entity.MglgChatMembers;
import com.muglang.muglangspace.entity.MglgChatMessage;
import com.muglang.muglangspace.repository.ChatRoomRepository;
import com.muglang.muglangspace.service.mglgchat.ChatService;


@Controller
@RequestMapping("/chat")
public class ChatRoomController {
	@Autowired
	private ChatService chatService;
	
	
    private final ChatRoomRepository repository;
    private final String listViewName;
    private final String detailViewName;

    @Autowired
    public ChatRoomController(ChatRoomRepository repository, 
    							@Value("${viewname.chatroom.list}") String listViewName, 
    							@Value("${viewname.chatroom.detail}") String detailViewName) {
        this.repository = repository;
        this.listViewName = listViewName;
        this.detailViewName = detailViewName;
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
    	model.addAttribute("rooms", chatService.getChatRooms());
        return listViewName;
    }

    @PostMapping("/rooms")
    public String room(@RequestParam("id") String id, @AuthenticationPrincipal CustomUserDetails loginUser, Model model) {
        ChatRoom room = repository.getChatRoom(id);
        int userId = loginUser.getMglgUser().getUserId();
        MglgChatMembers members = MglgChatMembers.builder()
        											 .chatRoomId(id)
        											 .userId(userId)
        											 .build();
        model.addAttribute("room", room);
        
        //chatService.insertMember(members);        	
        
        return detailViewName;
        
    }
    
    @GetMapping("/exit/{id}")
    public void exit(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails loginUser,
    		HttpServletResponse response) throws IOException {
    	
    	int userId = loginUser.getMglgUser().getUserId();
    	MglgChatMembers members = MglgChatMembers.builder()
    											 .chatRoomId(id)
    											 .userId(userId)
    											 .build();
    	
    	chatService.deleteMember(members);
    	
    	response.sendRedirect("/chat/rooms");
    	
    }
    
    @PostMapping("/insertMsg")
    public void insertMsg(MglgChatMessageDTO messageDTO) {
    	MglgChatMessage message = MglgChatMessage.builder()
    											 .chatRoomId(messageDTO.getChatRoomId())
    											 .userId(messageDTO.getUserId())
    											 .chatContent(messageDTO.getChatContent())
    											 .build();
    	chatService.insertMsg(message);
    }
    
    @GetMapping("/leave/{id}")
    public void leave(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails loginUser, 
    		HttpServletResponse response) throws IOException {
    	
    	int userId = loginUser.getMglgUser().getUserId();
    	MglgChatMembers members = MglgChatMembers.builder()
				 								 .chatRoomId(id)
				 								 .userId(userId)
				 								 .build();
    	
    	chatService.leaveRoom(members);
    	
    	response.sendRedirect("/chat/rooms");
    }
}
