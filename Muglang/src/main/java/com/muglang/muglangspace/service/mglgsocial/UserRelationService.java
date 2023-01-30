package com.muglang.muglangspace.service.mglgsocial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.entity.MglgUserRelation;

public interface UserRelationService {

	int cntFollow(MglgUserRelation follow);
	
	int cntFollowing(MglgUserRelation following);

	Page<CamelHashMap> followList(MglgUser user,Pageable pageable);
	Page<CamelHashMap> followingList(MglgUser user,Pageable pageable);
	Page<CamelHashMap> requestFollowList(int userId,Pageable pageable);
	void followUser(int followId, int userId);
	void unFollowUser(int userId,int loginUser);
	int followingOrNot(int userId, int loginUserId);

}
