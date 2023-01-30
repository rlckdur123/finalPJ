package com.muglang.muglangspace.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgComment;
import com.muglang.muglangspace.entity.MglgCommentId;

@Transactional
public interface MglgCommentRepository extends JpaRepository<MglgComment, MglgCommentId> {

//	Optional<MglgComment> findById(MglgCommentId commentIds);
//	
//	Optional<MglgComment> findAllByCommentId(MglgPost mglgPost);
	//카멜 해쉬맵이 아닌 엔티티 단위 그대로 불러오기
	//해당 게시글의 댓글 리스트 불러오기
	@Query(value = "SELECT * FROM T_MGLG_COMMENT WHERE POST_ID = :postId ORDER BY COMMENT_DATE DESC", nativeQuery = true)
	Page<MglgComment> getCommentList(Pageable pageable, @Param("postId") int postId);

	//카멜 해쉬맵으로 반환 받고, 추가적인 정보를 받을 수 있게 처리하는 쿼리
	//해당 게시글의 댓글 리스트를 키, 값으로 받아 모음.
	@Query(value = "SELECT * FROM T_MGLG_COMMENT WHERE POST_ID = :postId ORDER BY COMMENT_DATE DESC", nativeQuery = true)
	Page<CamelHashMap> getCommentCamelList(Pageable pageable, @Param("postId") int postId);
	
	// 댓글 리스트 불러오기 2. 조인 쿼리로 해당 댓글의 유저 정보까지 같이 가져옴. MglgComment형만 가져오므로 사용 안됨.
//	@Query(value = "SELECT * FROM T_MGLG_COMMENT A, T_MGLG_USER B"
//			+ " WHERE A.USER_ID = B.USER_ID AND "
//			+ " POST_ID = :postId", nativeQuery = true)
//	Page<MglgComment> getCommentList(Pageable pageable, @Param("postId") int postId);
	
	// 댓글 작성하기
	@Modifying
	@Query(value = "INSERT INTO T_MGLG_COMMENT" + "(COMMENT_ID, POST_ID, COMMENT_CONTENT, COMMENT_DATE, USER_ID)"
			+ "VALUES((SELECT IFNULL(MAX(A.COMMENT_ID), 0) + 1 FROM T_MGLG_COMMENT A WHERE A.POST_ID = :postId),"
			+ ":postId, :commentContent, NOW(), :userId)", nativeQuery = true)
	void insertComment(@Param("userId") int userId, @Param("postId") int postId,
			@Param("commentContent") String commentContent);

	@Modifying
	@Query(value = "DELETE FROM T_MGLG_COMMENT WHERE COMMENT_ID = :commentId AND POST_ID = :postId", nativeQuery = true)
	void deleteComment(@Param("commentId") int commentId, @Param("postId") int postId);

	// 댓글 업데이트
	@Modifying
	@Query(value = "UPDATE T_MGLG_COMMENT SET COMMENT_CONTENT = :commentContent "
			+ "WHERE COMMENT_ID = :commentId AND POST_ID = :postId", nativeQuery = true)
	void updateComment(@Param("commentId") int commentId, @Param("postId") int postId,
			@Param("commentContent") String commentContent);

	//커멘트 신고 로직
	@Modifying
	@Query(value = ""
			+ "INSERT INTO T_MGLG_REPORT VALUES("
			+ "(SELECT IFNULL(MAX(A.REPORT_ID), 0) + 1 FROM T_MGLG_REPORT A),1,:commentId,:postId,NOW(),:userId,:postUserId"
			+ ")", nativeQuery = true)
	void reportComment(@Param("postId") int postId,@Param("commentId") int commentId,@Param("postUserId") int postUserId,@Param("userId") int userId );		
	
	//커멘트 다중 신고 방지 로직
	@Query(value = ""
			+ "SELECT COUNT(*) FROM T_MGLG_REPORT "
			+ "WHERE SOURCE_USER_ID= :userId AND POST_Id = :postId AND COMMENT_Id = :commentId", nativeQuery = true)
	int reportCommentCheck(@Param("postId")int postId,@Param("commentId")int commentId,@Param("userId")int userId);	

	
//	@Modifying
//	@Query(value="UPDATE T_MGLG_COMMENT SET COMMENT_CONTENT = :#{#mglgComment.commentContent}"
//			+ " WHERE COMMENT_ID = :#{#mglgComment.commentId}", nativeQuery=true)
//	void updateComment(@Param("mglgComment") MglgComment mglgComment);
}
