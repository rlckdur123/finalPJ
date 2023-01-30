package com.muglang.muglangspace.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgChatMembers;

@Transactional
public interface ChatMembersRepository extends JpaRepository<MglgChatMembers, Integer> {

	@Modifying
	@Query(value=""
			+ "INSERT INTO T_MGLG_CHAT_MEMBERS "
			+ "VALUES( "
			+ ":chatRoomId,"
			+ ":userId,"
			+ " NOW(),"
			+ " NULL)", nativeQuery = true)
	void insertMember(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId);
	
	@Query(value=""
			+ "SELECT COUNT(*) FROM T_MGLG_CHAT_MEMBERS "
			+ "WHERE CHAT_ROOM_ID = :chatRoomId "
			+ "AND USER_ID = :userId", nativeQuery = true)
	int getMember(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId);
	
	@Modifying
	@Query(value=""
			+ "DELETE FROM T_MGLG_CHAT_MEMBERS "
			+ "WHERE CHAT_ROOM_ID = :chatRoomId "
			+ "AND USER_ID = :userId", nativeQuery = true)
	void deleteMember(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId);
	
	@Query(value=""
			+ "SELECT C.USER_NAME AS WRITER\n"
			+ "	 , A.CHAT_CONTENT AS MESSAGE\n"
			+ "     , DATE_FORMAT(A.CHAT_TIME, '%m-%d') AS CHAT_DATE\n"
			+ "     , DATE_FORMAT(A.CHAT_TIME, '%H:%i') AS CHAT_TIME\n"
			+ ", CASE\n"
			+ "		WHEN A.CHAT_TIME <= B.LEAVE_DATE\n"
			+ "        THEN 'R'\n"
			+ "        ELSE 'N'\n"
			+ "	   END AS READ_YN"
			+ "		, D.USER_PROFILE_NM "
			+ "	FROM T_MGLG_CHAT_MSG A\n"
			+ "	   , T_MGLG_CHAT_MEMBERS B\n"
			+ "       , T_MGLG_USER C\n"
			+ "		, T_MGLG_USER_PROFILE D\n"
			+ "    WHERE A.CHAT_ROOM_ID = :chatRoomId\n"
			+ "      AND A.CHAT_ROOM_ID = B.CHAT_ROOM_ID\n"
			+ "      AND B.USER_ID = :userId\n"
			+ "      AND A.CHAT_TIME >= B.ENTER_DATE\n"
			+ "      AND A.USER_ID = C.USER_ID\n"
			+ "		 AND A.ROOM_TYPE = :roomType"
			+ "		 AND B.USER_ID = D.USER_ID"
			+ "	ORDER BY A.CHAT_MSG_ID", nativeQuery = true)
	List<CamelHashMap> getPastMsg(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId, @Param("roomType") String roomType);
	
	
	@Modifying
	@Query(value=""
			+ "INSERT INTO T_MGLG_CHAT_MSG " 
			+ " VALUES( "
			+ " :chatRoomId,"
			+ " (SELECT IFNULL(MAX(A.CHAT_MSG_ID),0)+1 FROM T_MGLG_CHAT_MSG A WHERE A.CHAT_ROOM_ID = :chatRoomId),"
			+ " :userId,"
			+ " :chatContent,"
			+ " NOW(),"
			+ " :roomType)", nativeQuery = true)
	void insertMsg(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId, @Param("chatContent") String chatContent, @Param("roomType") String roomType);
	
	
	@Modifying
	@Query(value=""
			+ " UPDATE T_MGLG_CHAT_MEMBERS "
			+ " SET LEAVE_DATE = NOW()"
			+ " WHERE CHAT_ROOM_ID = :chatRoomId"
			+ " AND USER_ID = :userId", nativeQuery = true)
	void leaveRoom(@Param("chatRoomId") String chatRoomId, @Param("userId") int userId);
	
	@Modifying
	@Query(value=""
			+ " INSERT INTO T_MGLG_CHATROOM ("
			+ "	CHATROOM_ID,"
			+ " PART1,"
			+ " PART2,"
			+ " ROOM_DATETIME,"
			+ " PART1LEAVE_DATE_TIME,"
			+ " PART2LEAVE_DATE_TIME)"
			+ " VALUES ( "
			+ " :chatroomId, "
			+ " :part1, "
			+ " :part2, "
			+ " NOW(),"
			+ " NOW(),"
			+ " NOW()) ", nativeQuery = true)
	void createRoom(@Param("chatroomId") String chatroomId, @Param("part1") int part1, @Param("part2") int part2);
	
	@Query(value="SELECT IFNULL(MAX(A.CHATROOM_ID),0)+1 FROM T_MGLG_CHATROOM A", nativeQuery=true)
	int getNextChatroomId();
	
	@Query(value=""
			+ " SELECT A.CHAT_CONTENT AS MESSAGE\n"
			+ "	 , B.USER_NAME AS WRITER\n"
			+ "  , DATE_FORMAT(A.CHAT_TIME, '%H:%i') AS CHAT_TIME\n"
			+ "	FROM T_MGLG_CHAT_MSG A\n"
			+ "	   , T_MGLG_USER B\n"
			+ "	WHERE A.CHAT_ROOM_ID = :chatRoomId\n"
			+ "      AND A.USER_ID = B.USER_ID\n"
			+ "      AND A.ROOM_TYPE = :roomType", nativeQuery = true)
	List<CamelHashMap> getPastDM(@Param("chatRoomId") String chatRoomId, @Param("roomType") String roomType);
	
	@Query(value=""
			+ " SELECT USER_PROFILE_NM"
			+ " FROM T_MGLG_USER_PROFILE"
			+ " WHERE USER_ID = :userId", nativeQuery = true)
	String getUserProfile(@Param("userId") int userId);
}
