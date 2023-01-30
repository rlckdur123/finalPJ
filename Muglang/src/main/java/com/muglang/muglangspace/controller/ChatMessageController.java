package com.muglang.muglangspace.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import com.muglang.muglangspace.dto.ChatMessage;
import com.muglang.muglangspace.dto.MessageType;
import com.muglang.muglangspace.entity.MglgChatMembers;
import com.muglang.muglangspace.entity.MglgChatMessage;
import com.muglang.muglangspace.entity.MglgChatroom;
import com.muglang.muglangspace.repository.ChatMembersRepository;
import com.muglang.muglangspace.service.mglgchat.ChatService;
import com.muglang.muglangspace.service.mglgdm.DMService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

	@Autowired
	private ChatService chatService;
	
	@Autowired
	private DMService dmService;
	
	@Autowired
	public ChatMembersRepository chatMembersRespository;

	private final SimpMessageSendingOperations sendingOperations;
 
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		String chatRoomId = message.getChatRoomId();
		
		
		int userId = message.getUserId();
		int joinYn = chatService.getMember(chatRoomId, userId);
		
		MglgChatMembers members = MglgChatMembers.builder()
												 .chatRoomId(chatRoomId)
												 .userId(userId)
												 .build();
		chatService.insertMember(members);
		System.out.println(joinYn);
		
		if (MessageType.JOIN.equals(message.getType())) {
			if(joinYn == 0) {
				message.setMessage(message.getWriter() + "님이 입장하였습니다.");
			} else {
				message.setMessageList(chatService.getPastMsg(members, "O"));
			}
		} else {
			MglgChatMessage messageParam = MglgChatMessage.builder()
					 								 .chatRoomId(message.getChatRoomId())
					 								 .userId(message.getUserId())
					 								 .chatContent(message.getMessage())
					 								 .roomType("O")
					 								 .build();
			LocalTime currentTime = LocalTime.now();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
			String chatTime = currentTime.format(df);
			message.setChatTime(chatTime);
			
			message.setProfile(chatService.getUserProfile(message.getUserId()));
			
			chatService.insertMsg(messageParam);
		}
		sendingOperations.convertAndSend("/topic/chat/room/" + message.getChatRoomId(), message);

	}
	
	@MessageMapping("/chat/dMessage")
	public void dMessage(ChatMessage message) {
		String chatroomId = message.getChatRoomId();
		
		if (MessageType.JOIN.equals(message.getType())) {
			MglgChatMembers members = MglgChatMembers.builder()
					 								 .chatRoomId(chatroomId)
					 								 .userId(message.getLoginUserId())
					 								 .build();
			
			message.setMessageList(chatService.getPastDM(members, "D"));
		} else {
			System.out.println("chatroomI============================ "  + Integer.parseInt(chatroomId));
			MglgChatMessage messageParam = MglgChatMessage.builder()
					 								 .chatRoomId(message.getChatRoomId())
					 								 .userId(message.getLoginUserId())
					 								 .chatContent(message.getMessage())
					 								 .roomType("D")
					 								 .build();
			LocalTime currentTime = LocalTime.now();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
			String chatTime = currentTime.format(df);
			message.setChatTime(chatTime);
			
			chatService.insertMsg(messageParam);
		}
		
		sendingOperations.convertAndSend("/topic/chat/dRoom/" + chatroomId, message);
	}
}
