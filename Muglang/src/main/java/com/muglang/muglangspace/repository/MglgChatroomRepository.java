package com.muglang.muglangspace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.muglang.muglangspace.entity.MglgChatroom;

public interface MglgChatroomRepository extends JpaRepository<MglgChatroom, String>{
	@Query(value=" SELECT *\n"
			+ " FROM T_MGLG_CHATROOM\n"
			+ " WHERE 1=0\n"
			+ " OR (PART1 = :userId1 AND PART2 = :userId2)\n"
			+ " OR (PART1 = :userId2 AND PART2 = :userId1)", nativeQuery = true)
	public Optional<MglgChatroom> checkRoom(@Param("userId1") int userId1, @Param("userId2") int userId2);
	
	@Query(value=" SELECT CHATROOM_ID"
			+ " FROM T_MGLG_CHATROOM"
			+ " WHERE 1=0"
			+ " OR (PART1 = :userId1 AND PART2 = :userId2)"
			+ " OR (PART1 = :userId2 AND PART2 = :userId1)", nativeQuery = true)
	int getRoomId(@Param("userId1") int userId1, @Param("userId2") int userId2);
}
