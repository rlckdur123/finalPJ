package com.muglang.muglangspace.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgReport;

@Transactional
public interface MglgReportRepository extends JpaRepository<MglgReport, Integer>{

	Page<MglgReport> findByReportType(int a,Pageable pageable);
	

	@Query(value="SELECT TARGET_USER_ID, REPORT_DATE, COUNT(*) AS count"
			+ " FROM T_MGLG_REPORT "
			+ " GROUP BY TARGET_USER_ID "
			+ " ORDER BY count desc"
			, nativeQuery =true)
	Page<CamelHashMap> reportedUser(Pageable pageable);
	
	@Modifying
	@Query(value="DELETE FROM T_MGLG_REPORT"
			+ " WHERE COMMENT_ID = :commentId AND POST_ID =:postId",nativeQuery = true)
	void deleteReport(@Param("commentId") int commentId, @Param("postId") int postId);

}
