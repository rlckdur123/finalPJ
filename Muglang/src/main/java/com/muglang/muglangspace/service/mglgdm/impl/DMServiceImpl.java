package com.muglang.muglangspace.service.mglgdm.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgChatroom;
import com.muglang.muglangspace.repository.ChatMembersRepository;
import com.muglang.muglangspace.repository.MglgChatroomRepository;
import com.muglang.muglangspace.repository.MglgUserRelationRepository;
import com.muglang.muglangspace.service.mglgdm.DMService;

@Service
public class DMServiceImpl implements DMService {
	
	@Autowired
	private MglgUserRelationRepository userRelationRepository;
	
	@Autowired
	private MglgChatroomRepository mglgChatroomRepository;
	
	@Autowired
	private ChatMembersRepository chatMembersRepository;
	
	
	public List<CamelHashMap> getFollowBackList(int userId) {
		
		List<CamelHashMap> followList = userRelationRepository.getFollowBackList(userId);
		
		for(int i=0; i<followList.size(); i++) {
			if(followList.get(i).get("chatroomId").toString().equals("0")) {
				int chatroomId = chatMembersRepository.getNextChatroomId();
				chatMembersRepository.createRoom(String.valueOf(chatroomId), userId, Integer.parseInt(followList.get(i).get("userId").toString()));
				chatMembersRepository.flush();
				followList.get(i).replace("chatroomId", String.valueOf(chatroomId));
			}
		}
		
		return followList;
	}


	@Override
	public MglgChatroom checkRoom(int userId, int userId2) {
		// TODO Auto-generated method stub
		if(mglgChatroomRepository.checkRoom(userId, userId2).isEmpty()) {
			return null;
		} else {
			return mglgChatroomRepository.checkRoom(userId, userId2).get();
		}
	}
	
//	@Override
//	public String createRoom(MglgChatroom room) {
//		int part1 = room.getPart1();
//		int part2 = room.getPart2();
//		
//		chatMembersRepository.createRoom(part1, part2);
//		chatMembersRepository.flush();
//		return room.getChatroomId();
//	}

}
