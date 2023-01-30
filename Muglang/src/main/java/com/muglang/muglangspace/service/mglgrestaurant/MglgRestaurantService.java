package com.muglang.muglangspace.service.mglgrestaurant;

import com.muglang.muglangspace.entity.MglgRestaurant;

public interface MglgRestaurantService {
	public void insertRestaurant(MglgRestaurant mglgRestaurant);
	
	public MglgRestaurant selectRes(int postId);
	
	public String countRes(int userId, String resName);
	
	public void deleteRes(int postId);
}
