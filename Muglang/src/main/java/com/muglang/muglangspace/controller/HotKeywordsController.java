package com.muglang.muglangspace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.dto.MglgHotKeywordsDTO;
import com.muglang.muglangspace.entity.MglgHotKeywords;
import com.muglang.muglangspace.service.mglghotkeywords.MglgHotKeywordsService;

@RestController
@RequestMapping("/hotKeywords")
public class HotKeywordsController {
	
	@Autowired MglgHotKeywordsService mglgHotKeywordsService;
	
	@GetMapping("/getKeywords")
	public ModelAndView getKeywords(@PageableDefault(page=0, size=20) Pageable pageable) {
		
		Page<CamelHashMap> hotKeywords = mglgHotKeywordsService.getKeywords(pageable);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/hotwords/hotwords.html");
		mv.addObject("hotKeywords", hotKeywords);
		
		return mv;
	}
	
	@DeleteMapping("/delKeywords")
	@ResponseBody
	public ResponseEntity<?> delKeywords(@RequestParam(value="delWords") String[] delWords) {
		try {
			mglgHotKeywordsService.delKeywords(delWords);
			return ResponseEntity.status(HttpStatus.OK).body(1);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		}
	}
	
	@PutMapping("/udtKeywords")
	public ResponseEntity<?> udtKeywords(@RequestBody List<MglgHotKeywordsDTO> udtWords) {
		List<MglgHotKeywords> mglgHotKeywordsList = new ArrayList<MglgHotKeywords>();
		
		for(int i=0; i<udtWords.size(); i++) {
			System.out.println(udtWords.get(i).getHotKeyword());
		}
		
		for(int i=0; i<udtWords.size(); i++) {
			MglgHotKeywords hotKeywords = MglgHotKeywords.builder()
					                                     .hotKeyword(udtWords.get(i).getHotKeyword())
					                                     .confirmYn(udtWords.get(i).getConfirmYn())
					                                     .numOfTime(udtWords.get(i).getNumOfTime())
					                                     .bfHotKeyword(udtWords.get(i).getBfHotKeyword())
					                                     .build();
			mglgHotKeywordsList.add(hotKeywords);
		}
		mglgHotKeywordsService.udtKeywords(mglgHotKeywordsList);
		
		return ResponseEntity.status(HttpStatus.OK).body(1);
	}
}
