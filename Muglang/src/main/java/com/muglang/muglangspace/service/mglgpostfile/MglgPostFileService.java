package com.muglang.muglangspace.service.mglgpostfile;

import java.util.List;

import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgPostFile;


public interface MglgPostFileService {
	//게시글의 파일들을 수정할 수 있게 로드하는 메소드
	public List<MglgPostFile> getPostFileList(int postId);
	
	//게시글의 파일 1개를 불러오는 메소드.
	public MglgPostFile getPostFile(MglgPost mglgPost, MglgPostFile MglgPostFile);
	
	//게시글의 파일1개를 등록하는 메소드.
	public void insertPostFile(MglgPostFile file);
	
	//게시글에 올린 파일 목록을 수정하는 메소드.
	public void updatePostFileList(List<MglgPostFile> uploadFileList);
	
	//게시글의 파일을 삭제하는 메소드를 사용한다. 게시글을 지우기전 파일들을 모두 삭제후 게시글을 지울 때 필요함.
	public void deletePostFileList(int postId);
}
