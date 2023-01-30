package com.muglang.muglangspace.service.mglghotkeywords;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgHotKeywords;

public interface MglgHotKeywordsService {
	
	// 키워드 검색 시 DB에 키워드가 없을 경우에는 INSERT 있을 경우에는 UPDATE 쿼리를 보냄
	public void insrtOrUpdte(String searchKeyword);

	public Page<CamelHashMap> getKeywords(@PageableDefault(page=0, size=20) Pageable pageable);

	public void delKeywords(String[] delWords);

	public void udtKeywords(List<MglgHotKeywords> mglgHotKeywordsList);

	public List<CamelHashMap> getHotKeywords();
}
