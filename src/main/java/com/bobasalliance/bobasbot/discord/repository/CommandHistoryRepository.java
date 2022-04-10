package com.bobasalliance.bobasbot.discord.repository;

import java.sql.Timestamp;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandHistoryRepository extends CrudRepository<CommandHistoryDao, String> {
	@Query("INSERT INTO commandHistory VALUES (:command, :timestamp, :userId, :userName, :serverId, :serverName, " +
			":serverRegion, :executionTime)")
	@Modifying
	void insert(@Param("command") String command, @Param("timestamp") Timestamp timestamp, @Param("userId") String userId,
			@Param("userName") String userName, @Param("serverId") String serverId, @Param("serverName") String serverName,
			@Param("serverRegion") String serverRegion, @Param("executionTime") long executionTime);
}
