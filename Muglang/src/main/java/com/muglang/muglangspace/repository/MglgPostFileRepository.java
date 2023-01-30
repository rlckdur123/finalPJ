package com.muglang.muglangspace.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgPostFileId;

@Transactional
public interface MglgPostFileRepository extends JpaRepository<MglgPostFile, MglgPostFileId> {

	//프로필 사진을 정하기위해 사용하는 파일 찾기 쿼리 메소드 (유저 프로필 DB를 만든뒤 따로 생성하는 게 좋을 같다.)
	//public MglgPostFile findById(MglgUser mglgUser);
	//해당 포스트의 파일을 불러오는 JPA쿼리 메소드임.
	@Query(value="SELECT * FROM T_MGLG_POST_FILE WHERE POST_ID = :postId", nativeQuery=true)
	public List<MglgPostFile> findAllByMglgPost(@Param("postId") int postId);
	
	
	//해당 게시글을 삭제하기전 외래키로 사용하고 있는 파일 리스트를 모두 삭제.
	@Modifying
	@Query(value="DELETE FROM T_MGLG_POST_FILE WHERE POST_ID = :postId", nativeQuery=true)
	public void deleteAllByPostId(@Param("postId") int postId);
	
	//게시글의 파일 삭제를 반영하는 쿼리문
	@Modifying
	@Query(value="DELELTE FROM T_MGLG_POST_FILE WHERE POST_FILE_ID =  :postFileId", nativeQuery=true)
	public void deletePostFile(@Param("postFileId") int postFileId);
	
}
