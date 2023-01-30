package com.muglang.muglangspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgHotKeywordsDTO {
	private String hotKeyword;
	private String confirmYn;
	private int numOfTime;
	private String bfHotKeyword;
}
