package com.muglang.muglangspace.service.mglgadmin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgReport;
import com.muglang.muglangspace.entity.MglgUser;

public interface AdminService {

	MglgUser searchBan(MglgUser user);
	
	MglgUser uptUserBan(MglgUser user);
	
	Page<MglgReport> getReportComment(int a,Pageable pageable);


	Page<CamelHashMap> reportedUser(Pageable pageable);
	
	 void deleteReport(int commentId,int postId);
	
	
}
