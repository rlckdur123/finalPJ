package com.muglang.muglangspace.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgHotKeywords;

@Transactional
public interface MglgHotKeywordsRepository extends JpaRepository<MglgHotKeywords, String> {
	
	/*
	 1. When I do search, the sequence generator was not working for primary key ID in T_MGLG_HOT_KEYWORDS table.
	 2. Whem I do update the colums which is passed by ajax as List<
	 */
	
	@Modifying
	@Query(value=""
			+ " INSERT INTO T_MGLG_HOT_KEYWORDS\r\n"
			+ " (HOT_KEYWORD, CONFIRM_YN, NUM_OF_TIME)\r\n"
			+ " VALUES(:searchKeyword, 'N', 1)\r\n"
			+ " ON DUPLICATE KEY UPDATE NUM_OF_TIME = NUM_OF_TIME + 1", nativeQuery=true)
	void insrtOrUpdte(@Param("searchKeyword") String searchKeyword);
	
	@Query(value=""
			+ "SELECT * FROM T_MGLG_HOT_KEYWORDS\r\n"
			+ "ORDER BY NUM_OF_TIME DESC", 
			countQuery="SELECT COUNT(*) FROM T_MGLG_HOT_KEYWORDS", nativeQuery=true)
	Page<CamelHashMap> getKeywords(@PageableDefault(page=0, size=20) Pageable pageable);

	@Modifying
	@Query(value=""
			+ "UPDATE T_MGLG_HOT_KEYWORDS\r\n"
			+ "SET HOT_KEYWORD = :#{#m.hotKeyword}, CONFIRM_YN = :#{#m.confirmYn}, NUM_OF_TIME = :#{#m.numOfTime}\r\n"
			+ "WHERE HOT_KEYWORD = :#{#m.bfHotKeyword}", nativeQuery=true)
	void udtKeywords(@Param("m") MglgHotKeywords m);

	@Query(value=""
			+ "SELECT * FROM T_MGLG_HOT_KEYWORDS\r\n"
			+ "WHERE CONFIRM_YN = 'Y'\r\n"
			+ "ORDER BY NUM_OF_TIME DESC\r\n"
			+ "LIMIT 10", 
			countQuery="SELECT COUNT(10) FROM T_MGLG_HOT_KEYWORDS", nativeQuery=true)
	List<CamelHashMap> getHotKeywords();
	
	
}
