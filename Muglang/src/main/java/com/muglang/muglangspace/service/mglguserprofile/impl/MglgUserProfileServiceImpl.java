package com.muglang.muglangspace.service.mglguserprofile.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.entity.MglgUserProfile;
import com.muglang.muglangspace.repository.MglgUserProfileRepository;
import com.muglang.muglangspace.service.mglguserprofile.MglgUserProfileService;

@Service
public class MglgUserProfileServiceImpl implements MglgUserProfileService{
	@Autowired
	MglgUserProfileRepository mglgUserProfileRepository;

	@Override
	public void insertDefault(int userId, String attachPath) {
		mglgUserProfileRepository.insertDefault(userId,attachPath);
	}

	@Override
	public MglgUserProfile getUserImg(int userId) {
		return mglgUserProfileRepository.getUserImg(userId);

	}

	@Override
	public void updateProfileFile(MglgUserProfile mglgUserProfile) {
		int userId = mglgUserProfile.getMglgUser().getUserId();
		String profileNm = mglgUserProfile.getUserProfileNm();
		String profileCate = mglgUserProfile.getUserProfileCate();
		String originNm = mglgUserProfile.getUserProfileOriginNm();
		String path = mglgUserProfile.getUserProfilePath();
	
		System.out.println(mglgUserProfile);
		
		mglgUserProfileRepository.updateProfile(userId, profileNm, profileCate, originNm, path);
		
		mglgUserProfileRepository.flush();
		
	}

	@Override
	public MglgUserProfile followerProfile(int eachUserId) {
		
		return mglgUserProfileRepository.followerProfile(eachUserId);
	}

	@Override
	public MglgUserProfile followingProfile(int eachUserId) {
		
		return mglgUserProfileRepository.followerProfile(eachUserId);
	}

	@Override
	public void changeDefaultImg(int userId) {
		mglgUserProfileRepository.changeDefaultImg(userId);
		
		mglgUserProfileRepository.flush();

	}
	
	
}
