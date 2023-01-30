package com.muglang.muglangspace.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.entity.MglgRestaurant;
import com.muglang.muglangspace.entity.MglgRestaurantId;

@Transactional
public interface MglgRestaurantRepository extends JpaRepository<MglgRestaurant, MglgRestaurantId>{
	
	@Query(value="SELECT * FROM T_MGLG_RESTAURANT WHERE POST_ID = :postId", nativeQuery=true)
	public MglgRestaurant selectRes(@Param("postId") int postId);
	
	@Query(value="      SELECT COUNT(*) FROM t_mglg_user_relation\r\n"
			+ "      WHERE follower_id = :userId\r\n"
			+ "      AND USER_ID IN\r\n"
			+ "      (SELECT USER_ID FROM T_MGLG_POST\r\n"
			+ "        WHERE POST_ID IN\r\n"
			+ "        (SELECT POST_ID \r\n"
			+ "      FROM t_mglg_restaurant WHERE RES_NAME=:resName) GROUP BY USER_ID)",
			nativeQuery=true)
	public String countRes(@Param("userId") int userId, @Param("resName") String resName);
	
	@Modifying
	@Query(value="DELETE FROM T_MGLG_RESTAURANT WHERE POST_ID = :postId", nativeQuery=true)
	public void deleteRes(@Param("postId") int postId);
}
