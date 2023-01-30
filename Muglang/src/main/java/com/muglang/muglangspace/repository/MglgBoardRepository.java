package com.muglang.muglangspace.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.entity.MglgBoard;


@Transactional
public interface MglgBoardRepository extends JpaRepository<MglgBoard, Integer> {
	
	@Modifying
	void deleteByBoardId(int boardId); 
	
	@Modifying
	@Query(value=""
			+ "UPDATE T_MGLG_BOARD "
			+ "SET "
			+ "BOARD_TITLE = :boardTitle, "
			+ "BOARD_CONTENT = :boardContent "
			+ "WHERE BOARD_ID = :boardId"
			
			,nativeQuery = true)
	void updateBoard(@Param("boardTitle") String boardTitle,@Param("boardContent") String boardContent,
			@Param("boardId") int boardId);
	
	@Modifying
	@Query(value=""
			+ "DELETE FROM T_MGLG_BOARD "
			+ "WHERE BOARD_ID = :boardId"			
			,nativeQuery = true)
	void deleteBoard(@Param("boardId") int boardId);
	
	@Modifying
	@Query(value=""
			+ "INSERT INTO T_MGLG_BOARD "
			+ "VALUES("
			+ "(SELECT IFNULL(MAX(A.BOARD_ID)+1,0) FROM T_MGLG_BOARD A), "
			+ ":boardContent, "
			+ "0, "
			+ "now(), "
			+ ":boardTitle, "
			+ "0"
			+ ")"
			,nativeQuery = true)
	void insertBoard(@Param("boardTitle") String boardTitle,@Param("boardContent") String boardContent);
	
	MglgBoard findByBoardId(MglgBoard board);

}
