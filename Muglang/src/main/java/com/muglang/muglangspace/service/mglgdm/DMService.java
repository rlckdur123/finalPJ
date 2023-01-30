package com.muglang.muglangspace.service.mglgdm;

import java.util.List;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgChatroom;

public interface DMService {
	
	List<CamelHashMap> getFollowBackList(int userId);

	MglgChatroom checkRoom(int userId, int userId2);
	
	//String createRoom(MglgChatroom room);
	

}