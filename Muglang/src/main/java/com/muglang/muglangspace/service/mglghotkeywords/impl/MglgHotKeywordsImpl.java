package com.muglang.muglangspace.service.mglghotkeywords.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgHotKeywords;
import com.muglang.muglangspace.repository.MglgHotKeywordsRepository;
import com.muglang.muglangspace.service.mglghotkeywords.MglgHotKeywordsService;

@Service
public class MglgHotKeywordsImpl implements MglgHotKeywordsService {
	
	// DB에 데이터를 CRUD할 일꾼
	@Autowired MglgHotKeywordsRepository mglgHotKeywordsRepository;
	
	// 키워드 검색 시 DB에 키워드가 없을 경우에는 INSERT 있을 경우에는 UPDATE 쿼리를 보냄
	@Override
	public void insrtOrUpdte(String searchKeyword) {
		mglgHotKeywordsRepository.insrtOrUpdte(searchKeyword);
	}
	
	// 인기 검색어 받아오기
	@Override
	public Page<CamelHashMap> getKeywords(@PageableDefault(page=0, size=20) Pageable pageable) {
		Page<CamelHashMap> hotKeywords = mglgHotKeywordsRepository.getKeywords(pageable);
		return hotKeywords;
	}

	@Override
	public void delKeywords(String[] delWords) {	
		for(String i : delWords) {
			mglgHotKeywordsRepository.deleteById(i);
		}
		mglgHotKeywordsRepository.flush();
	}

	@Override
	public void udtKeywords(List<MglgHotKeywords> mglgHotKeywordsList) {
		
		for(MglgHotKeywords m : mglgHotKeywordsList) {
			mglgHotKeywordsRepository.udtKeywords(m);
		}
		mglgHotKeywordsRepository.flush();
	}

	@Override
	public List<CamelHashMap> getHotKeywords() {
		return mglgHotKeywordsRepository.getHotKeywords();
		
	}

}
