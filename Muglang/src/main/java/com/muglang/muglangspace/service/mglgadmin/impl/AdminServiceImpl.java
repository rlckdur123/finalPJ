package com.muglang.muglangspace.service.mglgadmin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgReport;
import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.repository.MglgPostRepository;
import com.muglang.muglangspace.repository.MglgReportRepository;
import com.muglang.muglangspace.repository.MglgUserRepository;
import com.muglang.muglangspace.service.mglgadmin.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
		@Autowired
		private MglgReportRepository mglgReportRepository;
		@Autowired
		private MglgUserRepository mglgUserRepository;
		@Autowired
		private MglgPostRepository mglgPostRepository;
		
		@Override
		public MglgUser searchBan(MglgUser user) {
			
			return mglgUserRepository.findByUserNameContaining(user);
		}

		@Override
		public MglgUser uptUserBan(MglgUser user) {
			String userBanYn = user.getUserBanYn();
			int userId = user.getUserId();
			
			
			mglgUserRepository.uptUserBan(userBanYn,userId);
			mglgUserRepository.flush();
			
			return user;
		}

		@Override
		public Page<MglgReport> getReportComment(int a,Pageable pageable) {
			
			return mglgReportRepository.findByReportType(a,pageable);
		}
		@Override
		public Page<CamelHashMap> reportedUser(Pageable pageable){
			return mglgReportRepository.reportedUser(pageable);
		}

		@Override
		public void deleteReport(int commentId, int postId) {

			mglgReportRepository.deleteReport(commentId,postId);

		}

		
}
