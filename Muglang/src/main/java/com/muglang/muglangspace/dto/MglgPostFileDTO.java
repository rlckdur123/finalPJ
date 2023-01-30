package com.muglang.muglangspace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MglgPostFileDTO {
	private int postId;
	private int postFileId;
	private String postFileNm;
	private String postFileOriginNm;
	private String postFilePath;
	private String postFileRegdate;
	private String postFileCate;
	private String postFileStatus;
	private String newFileNm;
}
