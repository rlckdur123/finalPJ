package com.muglang.muglangspace.service.mglgrestaurant.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.entity.MglgRestaurant;
import com.muglang.muglangspace.repository.MglgRestaurantRepository;
import com.muglang.muglangspace.service.mglgrestaurant.MglgRestaurantService;

@Service
public class MglgRestaurantServiceImpl implements MglgRestaurantService {
	@Autowired
	private MglgRestaurantRepository mglgRestaurantRepository;

	//식당 등록
	@Override
	public void insertRestaurant(MglgRestaurant mglgRestaurant) {
		mglgRestaurantRepository.save(mglgRestaurant);

		mglgRestaurantRepository.flush();
		
	}

	//게시글에서 해당 id의 식당을 가져옴
	@Override
	public MglgRestaurant selectRes(int postId) {
		return mglgRestaurantRepository.selectRes(postId);
	}

	@Override
	public String countRes(int userId, String resName) {
		String countRes = mglgRestaurantRepository.countRes(userId, resName);
		if(countRes == null) {
			return "0";
		}
		else {
			return countRes;
		}
	}

	//게시글을 지울 시 사용하는 삭제 쿼리 호출하는 구문.
	@Override
	public void deleteRes(int postId) {
		// TODO Auto-generated method stub
		mglgRestaurantRepository.deleteRes(postId);
	}
}
