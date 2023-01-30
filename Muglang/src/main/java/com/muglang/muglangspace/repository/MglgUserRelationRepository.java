package com.muglang.muglangspace.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgUser;

@Transactional
public interface MglgUserRelationRepository extends JpaRepository<MglgUser, Integer> {

	@Query(value = "SELECT COUNT(*) AS followCount FROM T_MGLG_USER_RELATION WHERE USER_ID = :userId", nativeQuery = true)
	public int cntFollow(@Param("userId") int userId);

	@Query(value = "SELECT COUNT(*) AS followingCount FROM T_MGLG_USER_RELATION WHERE FOLLOWER_ID = :userId", nativeQuery = true)
	public int cntFollowing(@Param("userId") int userId);

	@Query(
		value = "	                  "
		+ "			SELECT A.*\r\n" 
		+ "               FROM T_MGLG_USER A\r\n"
		+ "               WHERE A.USER_ID \r\n" 
		+ "               NOT IN \r\n"
		+ "               (SELECT B.USER_ID FROM T_MGLG_USER_RELATION B WHERE B.FOLLOWER_ID = :userId)\r\n"
		+ "               AND A.USER_ID IN \r\n"
		+ "				  (SELECT C.FOLLOWER_ID FROM T_MGLG_USER_RELATION C WHERE C.USER_ID = :userId)",
		countQuery = "SELECT COUNT(*) FROM ("
		+ "			SELECT A.*\r\n" 
		+ "               FROM T_MGLG_USER A\r\n"
		+ "               WHERE A.USER_ID \r\n" 
		+ "               NOT IN \r\n"
		+ "               (SELECT B.USER_ID FROM T_MGLG_USER_RELATION B WHERE B.FOLLOWER_ID = :userId)\r\n"
		+ "               AND A.USER_ID IN \r\n"
		+ "				  (SELECT C.FOLLOWER_ID FROM T_MGLG_USER_RELATION C WHERE C.USER_ID = :userId)"
		+ " 		) F",
		nativeQuery = true)
	public Page<CamelHashMap> requestFollowList(@Param("userId") int userId, Pageable pageable);

	/// 맞팔
	@Modifying
	@Query(value = "INSERT INTO T_MGLG_USER_RELATION (FOLLOW_DATE, FOLLOWER_ID, USER_ID)" + "	  VALUES(now(),  :userId, :followId)", nativeQuery = true)
	public void followUser(@Param("followId") int followId, @Param("userId") int userId);
	/// 언팔
	@Modifying
	@Query(value = "DELETE FROM T_MGLG_USER_RELATION WHERE USER_ID= :userId AND FOLLOWER_ID= :loginUser", nativeQuery = true)
	public void unFollowUser(@Param("userId") int userId, @Param("loginUser") int loginUser);

	@Query(value = "SELECT COUNT(*) FROM T_MGLG_USER_RELATION WHERE USER_ID= :userId AND FOLLOWER_ID= :loginUserId", nativeQuery = true)	
	public int followingOrNot(@Param("userId")int userId,@Param("loginUserId") int loginUserId); 
	
	@Query(value = "WITH AA AS (\n"
			+ "	SELECT A.USER_ID\n"
			+ "		 , B.USER_NAME\n"
			+ "		 , C.user_profile_nm\n"
			+ "		FROM (\n"
			+ "				SELECT D.USER_ID \n"
			+ "					FROM T_MGLG_USER_RELATION D\n"
			+ "					   , (\n"
			+ "							SELECT E.FOLLOWER_ID \n"
			+ "								FROM T_MGLG_USER_RELATION E\n"
			+ "								WHERE E.USER_ID = :userId\n"
			+ "						 ) F\n"
			+ "					WHERE D.FOLLOWER_ID = :userId\n"
			+ "					  AND D.USER_ID = F.FOLLOWER_ID\n"
			+ "			 ) A\n"
			+ "		   , T_MGLG_USER B\n"
			+ "		   LEFT OUTER JOIN t_mglg_user_profile C\n"
			+ "		   ON B.USER_ID = C.USER_ID\n"
			+ "		WHERE A.USER_ID = B.USER_ID\n"
			+ ")\n"
			+ "SELECT AA.*\n"
			+ "	 , IFNULL(BB.CHATROOM_ID, 0) AS CHATROOM_ID\n"
			+ "	FROM AA\n"
			+ "	LEFT OUTER JOIN (\n"
			+ "						SELECT H.CHATROOM_ID\n"
			+ "							 , H.PART1\n"
			+ "							 , H.PART2\n"
			+ "							FROM T_MGLG_CHATROOM H\n"
			+ "							WHERE 1 = 0\n"
			+ "							   OR H.PART1 = :userId\n"
			+ "							   OR H.PART2 = :userId\n"
			+ "					 ) BB\n"
			+ "	ON CASE\n"
			+ "			WHEN BB.PART2 = :userId\n"
			+ "            THEN BB.PART1 = AA.USER_ID\n"
			+ "			ELSE BB.PART2 = AA.USER_ID\n"
			+ "		END;", nativeQuery = true)
	public List<CamelHashMap> getFollowBackList(@Param("userId") int userId);
}
