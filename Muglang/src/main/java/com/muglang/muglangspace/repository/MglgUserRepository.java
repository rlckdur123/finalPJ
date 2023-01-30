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

public interface MglgUserRepository extends JpaRepository<MglgUser, Integer> {



   MglgUser findByUserNameContaining(MglgUser user);

   @Modifying // 데이터의 변경이 일어나는 @Query을 사용할 때는 @Modifying을 붙여준다.
   @Query(value = "UPDATE T_MGLG_USER SET USER_BAN_YN = :userBanYn WHERE USER_ID = :userId", nativeQuery = true)
   void uptUserBan(@Param("userBanYn") String userBanYn, @Param("userId") int userId);

   MglgUser findById(@Param("userId") int userId);

   // Sns계정이 가입 되어있는지 여부를 판단하는 간단한 검색쿼리
   @Query(value = "SELECT * FROM T_MGLG_USER WHERE USER_SNS_ID = :userSnsId", nativeQuery = true)
   MglgUser findByUserSnsId(@Param("userSnsId") String userSnsId);

//--------------------어드민 관련///   
   @Query(value = "SELECT A.*" + "    , IFNULL(B.REPORT_CNT, 0) AS REPORT_CNT" + "   FROM T_MGLG_USER A"
         + "    LEFT OUTER JOIN (" + "                  SELECT C.TARGET_USER_ID"
         + "                      , COUNT(C.TARGET_USER_ID) AS REPORT_CNT"
         + "                            FROM T_MGLG_REPORT C"
         + "                            GROUP BY C.TARGET_USER_ID" + "                ) B"
         + "   ON A.USER_ID = B.TARGET_USER_ID", countQuery = "SELECT COUNT(*) FROM (" + " SELECT A.*"
               + "    , IFNULL(B.REPORT_CNT, 0) AS REPORT_CNT" + "   FROM T_MGLG_USER A" + "    LEFT OUTER JOIN ("
               + "                  SELECT C.TARGET_USER_ID"
               + "                      , COUNT(C.TARGET_USER_ID) AS REPORT_CNT"
               + "                            FROM T_MGLG_REPORT C"
               + "                            GROUP BY C.TARGET_USER_ID" + "                ) B"
               + "   ON A.USER_ID = B.TARGET_USER_ID) D", nativeQuery = true)
   Page<CamelHashMap> searchDefault(Pageable pageable);

   // 이름으로 검색
   @Query(value = "SELECT A.*" + "    , IFNULL(B.REPORT_CNT, 0) AS REPORT_CNT" + "   FROM T_MGLG_USER A"
         + "    LEFT OUTER JOIN (" + "                  SELECT C.TARGET_USER_ID"
         + "                      , COUNT(C.TARGET_USER_ID) AS REPORT_CNT"
         + "                            FROM T_MGLG_REPORT C"
         + "                            GROUP BY C.TARGET_USER_ID" + "                ) B"
         + "   ON A.USER_ID = B.TARGET_USER_ID"
         + " WHERE USER_NAME LIKE %:searchKeyword%",
         countQuery =""
               + "SELECT COUNT(*) "
               + "             FROM T_MGLG_USER A "
               + "            WHERE USER_NAME LIKE %:searchKeyword% ",
          nativeQuery = true)
   Page<CamelHashMap> searchName(@Param("searchKeyword") String searchKeyword, Pageable pageable);

   // 아이디로 검색
   @Query(value = "SELECT A.*" + "    , IFNULL(B.REPORT_CNT, 0) AS REPORT_CNT" + "   FROM T_MGLG_USER A"
         + "    LEFT OUTER JOIN (" + "                  SELECT C.TARGET_USER_ID"
         + "                      , COUNT(C.TARGET_USER_ID) AS REPORT_CNT"
         + "                            FROM T_MGLG_REPORT C"
         + "                            GROUP BY C.TARGET_USER_ID" + "                ) B"
         + "   ON A.USER_ID = B.TARGET_USER_ID"
         + " WHERE USER_ID LIKE %:searchKeyword%",
         countQuery =""
               + "SELECT COUNT(*) "
               + "             FROM T_MGLG_USER A "
               + "            WHERE USER_ID LIKE %:searchKeyword% ",  nativeQuery = true)
   Page<CamelHashMap> searchId(@Param("searchKeyword") String searchKeyword, Pageable pageable);

   // 둘다 검색
   @Query(value = "SELECT A.*" + "    , IFNULL(B.REPORT_CNT, 0) AS REPORT_CNT" + "   FROM T_MGLG_USER A"
         + "    LEFT OUTER JOIN (" + "                  SELECT C.TARGET_USER_ID"
         + "                      , COUNT(C.TARGET_USER_ID) AS REPORT_CNT"
         + "                            FROM T_MGLG_REPORT C"
         + "                            GROUP BY C.TARGET_USER_ID" + "                ) B"
         + "   ON A.USER_ID = B.TARGET_USER_ID"
         + " WHERE USER_NAME LIKE %:searchKeyword1% OR EMAIL LIKE %:searchKeyword2%", nativeQuery = true)
   Page<CamelHashMap> searchAll(@Param("searchKeyword1") String searchKeyword1,
         @Param("searchKeyword2") String searchKeyword2, Pageable pageable);
//--------------------어드민 관련 끝///   

   // 팔로워 리스트
   @Query(value = "SELECT * FROM T_MGLG_USER WHERE USER_ID IN (SELECT FOLLOWER_ID FROM t_mglg_user_relation WHERE USER_ID= :userId)", nativeQuery = true)
   Page<CamelHashMap> followList(@Param("userId") int userId, Pageable pageable);

   // 팔로워 서치
   @Query(value = "SELECT A.* FROM T_MGLG_USER A WHERE "
         + "      A.USER_NICK LIKE CONCAT('%',:searchKeyword,'%') AND " 
         + "      A.USER_ID IN (SELECT B.FOLLOWER_ID FROM T_MGLG_USER_RELATION B WHERE B.USER_ID= :userId) "
            ,  
         countQuery ="SELECT COUNT(*) FROM  (SELECT A.* FROM T_MGLG_USER A WHERE A.USER_ID"
               + "         IN (SELECT B.FOLLOWER_ID FROM T_MGLG_USER_RELATION B WHERE B.USER_ID= :userId)"
               + "         AND A.USER_NAME LIKE CONCAT('%',:searchKeyword,'%')) C",
         nativeQuery = true)
   Page<CamelHashMap> searchFollowList(@Param("searchKeyword") String searchKeyword, @Param("userId") int userId,
         Pageable pageable);

   // 팔로잉 리스트
      @Query(value = "SELECT * FROM T_MGLG_USER WHERE USER_ID IN "
            + "(SELECT USER_ID FROM t_mglg_user_relation WHERE FOLLOWER_ID= :userId)", nativeQuery = true)
      Page<CamelHashMap> followingList(@Param("userId") int userId, Pageable pageable);

      // 팔로잉 서치
      @Query(value = "SELECT A.* FROM T_MGLG_USER A WHERE "
            + "      A.USER_NICK LIKE CONCAT('%',:searchKeyword,'%') AND " 
            + "      A.USER_ID IN (SELECT USER_ID FROM T_MGLG_USER_RELATION B WHERE B.FOLLOWER_ID= :userId) "
               ,  
            countQuery ="SELECT COUNT(*) FROM  (SELECT A.* FROM T_MGLG_USER A WHERE A.USER_ID"
                  + "         IN (SELECT B.USER_ID FROM T_MGLG_USER_RELATION B WHERE B.FOLLOWER_ID= :userId)"
                  + "         AND A.USER_NAME LIKE CONCAT('%',:searchKeyword,'%')) C",
            nativeQuery = true)
      Page<CamelHashMap> searchFollowingList(@Param("searchKeyword") String searchKeyword, @Param("userId") int userId,
            Pageable pageable);

   //유저 신고 로직
   @Modifying
   @Query(value = ""
         + "INSERT INTO T_MGLG_REPORT VALUES("
         + "(SELECT IFNULL(MAX(A.REPORT_ID), 0) + 1 FROM T_MGLG_REPORT A),1,0,0,NOW(),:userId,:postUserId"
         + ")", nativeQuery = true)
   void reportUser(@Param("postUserId") int postUserId,@Param("userId") int userId);      
   
   
   @Query(value = ""
         + "SELECT COUNT(*) FROM T_MGLG_REPORT "
         + "WHERE SOURCE_USER_ID= :userId AND TARGET_USER_ID = :postUserId", nativeQuery = true)
   int reportUserCheck(@Param("postUserId") int postUserId,@Param("userId") int userId);   
   
   
   @Query(value = "SELECT A.* FROM t_mglg_user A "
         + "     WHERE A.USER_ID IN "
         + "     (SELECT B.USER_ID FROM T_MGLG_POST B "
         + "     WHERE B.POST_ID IN "
         + "     (SELECT C.POST_ID FROM t_mglg_restaurant C WHERE C.RES_NAME=:resName))", nativeQuery = true)
   List<CamelHashMap> getEatUser(@Param("resName") String resName);

}