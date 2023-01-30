package com.muglang.muglangspace.service.mglgcomment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgComment;

public interface MglgCommentService {
	MglgComment getComment(MglgComment comment);

//	Optional<MglgComment> getCommentList(MglgPost mglgPost);

	void deleteComment(int commentId, int postId);

	String reportComment(int postId,int commentId,int postUserId,int userId);
	
	void insertComment(MglgComment comment);
	
	void updateComment(int commentId, int postId, String commentContent);

	void insertComment(int userId, int postId, String commentContent);

	Page<MglgComment> getPageCommentList(Pageable pageable, int postId);
	//카멜 맵으로 댓글 데이터를 받고 그 데이터에 추가정보를 담게 수정함.
	Page<CamelHashMap> getPageCommentCamelList(Pageable pageable, int postId);

	//모든 게시물의 전체 댓글 리스트. 사실상 쓸수 없는 쿼리
	//Page<MglgComment> getPageCommentList(Pageable pageable);
}
