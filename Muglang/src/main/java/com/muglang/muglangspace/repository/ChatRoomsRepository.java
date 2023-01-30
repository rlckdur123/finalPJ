package com.muglang.muglangspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muglang.muglangspace.entity.MglgChatrooms;

public interface ChatRoomsRepository extends JpaRepository<MglgChatrooms, Integer> {
//	@Query(value="query", nativeQuery=true)
//	List<MglgChatRooms> getChatRoomList();
}
