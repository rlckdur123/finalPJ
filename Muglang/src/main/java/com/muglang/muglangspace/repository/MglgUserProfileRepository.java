package com.muglang.muglangspace.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.entity.MglgUser;
import com.muglang.muglangspace.entity.MglgUserProfile;
import com.muglang.muglangspace.entity.MglgUserProfileId;
import com.muglang.muglangspace.service.mglguserprofile.MglgUserProfileService;

@Transactional
public interface MglgUserProfileRepository extends JpaRepository<MglgUserProfile, MglgUserProfileId>{
	
	@Modifying
	@Query(value = "INSERT INTO t_mglg_user_profile "
			+ "VALUES(:userId,'png','defaultImg.png',"
			+ ":attachPath,'defaultImg.png')",
	nativeQuery = true)
	public int insertDefault(@Param("userId") int userId,@Param("attachPath") String attachPath);
	@Query(value = "SELECT * FROM T_MGLG_USER_PROFILE WHERE USER_ID=:userId",
	nativeQuery = true)
	MglgUserProfile getUserImg(@Param("userId") int userId);
	
	
		@Modifying
		@Query(value = ""
				+ "UPDATE T_MGLG_USER_PROFILE "
				+ "SET USER_PROFILE_NM = :profileNm, "
				+ "	   USER_PROFILE_CATE = :profileCate,"
				+ "	   USER_PROFILE_ORIGIN_NM = :originNm,"
				+ "	   USER_PROFILE_path = :path "
				+ "WHERE USER_ID = :userId",nativeQuery = true)
		public void updateProfile(@Param("userId") int userId,@Param("profileNm") String profileNm,
							      @Param("profileCate") String profileCate,@Param("originNm") String originNm,@Param("path") String path);	
		
		@Query(value = "SELECT * FROM T_MGLG_USER_PROFILE WHERE USER_ID=:eachUserId",nativeQuery = true)
		public MglgUserProfile followerProfile(@Param("eachUserId") int eachUserId);

		@Modifying
		@Query(value = ""
				+ "UPDATE T_MGLG_USER_PROFILE "
				+ "SET USER_PROFILE_NM = 'defaultImg.png' , "
				+ "	   USER_PROFILE_CATE = 'png',"
				+ "	   USER_PROFILE_ORIGIN_NM = 'defaultImg.png', "
				+ "	   USER_PROFILE_path = 'C:\\Springboot\\PROJECT\\PROJECT\\Muglang\\src\\main\\webapp\\/upload/' "
				+ " WHERE USER_ID = :userId",nativeQuery = true)
		public void changeDefaultImg(@Param("userId") int userId) ;

}


